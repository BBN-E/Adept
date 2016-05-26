/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

package adept.kbapi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import adept.common.Chunk;
import adept.common.KBID;
import adept.common.MentalStateType;
import adept.common.OntType;
import adept.common.Pair;
import adept.kbapi.KBRelation.AbstractInsertionBuilder;
import adept.kbapi.sparql.SparqlQueryBuilder;
import adept.kbapi.sql.QuickJDBC;
import adept.kbapi.sql.SqlQueryBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.update.Update;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;


/**
 * The most important class for using the KB API is
 * adept.kbapi.KB.  This class contains connections to both
 * the RDF triplestore instance and the metadata database. 
 * Calls to insert on InsertionBuilders and update on
 * UpdateBuilders require a reference to this class.  All
 * query and delete methods are called from this class.
 * 
 */
public class KB {

	private final QuickJDBC quickJDBC;
	private final SparqlQueryBuilder sparqlQueryBuilder;
	private final KBOntologyModel kbOntologyModel;
	private final SPARQLService sparqlService;

	private LRUCache<String, Boolean> insertedSourceDocuments;
	private LRUCache<String, Boolean> insertedChunks;
	private LRUCache<String, Boolean> insertedCorpora;
	private LRUCache<String, Boolean> insertedSourceAlgorithms;

	private static final int CACHE_SIZE = 500;

	public KB(KBParameters kbParameters) throws KBConfigurationException {
		this(kbParameters, null);
	}

	public KB(KBParameters kbParameters, String defaultSchema) throws KBConfigurationException {
		this(kbParameters, defaultSchema, new RemoteSPARQLService(kbParameters));
	}

	public KB(KBParameters kbParameters, String defaultSchema, SPARQLService sparqlService)
			throws KBConfigurationException {
		quickJDBC = new QuickJDBC(kbParameters, defaultSchema);
		sparqlQueryBuilder = new SparqlQueryBuilder();
		kbOntologyModel = KBOntologyModel.instance();
		this.sparqlService = sparqlService;
		insertedSourceAlgorithms = new LRUCache<String, Boolean>(CACHE_SIZE);
		insertedSourceDocuments = new LRUCache<String, Boolean>(CACHE_SIZE);
		insertedChunks = new LRUCache<String, Boolean>(CACHE_SIZE);
		insertedCorpora = new LRUCache<String, Boolean>(CACHE_SIZE);
	}

	/**
	 * <p>
	 * Query KB entity given corresponding Adept KBID object.
	 * 
	 * <p>
	 * Note that this method does not return any information about whether this
	 * entity maps to any external KB entity. You must separately query for this
	 * information using the getExternalKBIDs() API method.
	 * </p>
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return Adept Entity object
	 * @throws adept.kbapi.KBQueryException
	 */
	public KBEntity getEntityById(KBID kbId) throws KBQueryException {
		try {
			return getEntitiesByIds(Arrays.asList(kbId.getObjectID())).get(0);
		} catch (Exception ex) {
			throw new KBQueryException(
					"Failed to query for entity with ID = " + kbId.getObjectID(), ex);
		}
	}

	private List<KBEntity> getEntitiesByIds(List<String> kbUris) throws KBQueryException {
		if (kbUris.isEmpty()) {
			return new ArrayList<KBEntity>();
		}
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetInformationForMultipleEntitiesQuery(kbUris);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBEntity> entities = new ArrayList<KBEntity>();
			List<IntermediateEntity> intermediateEntities = new ArrayList<IntermediateEntity>();

			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode idNode = item.get("?id");
				String id = idNode.asResource().getURI().split("#")[1];

				RDFNode confidenceNode = item.get("?confidence");
				float entityConfidence = confidenceNode.asLiteral().getFloat();

				RDFNode canonicalMentionNode = item.get("?canonicalMention");
				String canonicalMention = canonicalMentionNode.asLiteral().getString();

				RDFNode canonicalMentionConfidenceNode = item.get("?canonicalMentionConfidence");
				float canonicalMentionConfidence = canonicalMentionConfidenceNode.asLiteral()
						.getFloat();

				RDFNode typeNode = item.get("?type");

				RDFNode typeConfidenceNode = item.get("?typeConfidence");
				float typeConfidence = typeConfidenceNode.asLiteral().getFloat();

				IntermediateEntity intermediateEntity = null;
				for (IntermediateEntity exisitingIntermediateEntity : intermediateEntities) {
					if (exisitingIntermediateEntity.id.equals(id)) {
						intermediateEntity = exisitingIntermediateEntity;
						break;
					}
				}
				if (intermediateEntity == null) {
					intermediateEntity = new IntermediateEntity();
					intermediateEntity.id = id;
					intermediateEntity.entityConfidence = entityConfidence;
					intermediateEntity.canonicalMention = canonicalMention;
					intermediateEntity.canonicalMentionConfidence = canonicalMentionConfidence;
					intermediateEntities.add(intermediateEntity);
				}
				intermediateEntity.types.put(new OntType(typeNode.asResource()), typeConfidence);
			}

			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
					kbUris, sqlConnection);

			for (IntermediateEntity intermediateEntity : intermediateEntities) {
				KBTextProvenance.InsertionBuilder canonicalMention = null;
				List<KBTextProvenance.InsertionBuilder> provenances = textProvenancesMap
						.get(intermediateEntity.id);
				for (KBTextProvenance.InsertionBuilder provenance : provenances) {
					if (provenance.getKBID().getObjectID()
							.equals(intermediateEntity.canonicalMention)) {
						canonicalMention = provenance;
						break;
					}
				}
				KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(
						intermediateEntity.types, canonicalMention,
						intermediateEntity.entityConfidence,
						intermediateEntity.canonicalMentionConfidence);
				entityBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(provenances));
				entityBuilder.setKBID(new KBID(intermediateEntity.id,
						KBOntologyModel.DATA_INSTANCES_PREFIX));
				entities.add(entityBuilder.build());
			}

			return entities;

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for entities with IDs = " + kbUris, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * <p>
	 * Query KB relation given corresponding KBID.
	 * </p>
	 * 
	 * <p>
	 * Note that this method does not return any information about whether this
	 * entity maps to any external KB relation. You must separately query for
	 * this information using the getExternalKBIDs() API method.
	 * </p>
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return Adept DocumentRelation object
	 */

	public KBRelation getRelationById(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBRelation> relations = this.<KBRelation> getRelationsFromResultSet(
					kbId.getObjectID(), resultSet, sqlConnection, KBRelation.class);
			if (relations.isEmpty()) {
				throw new KBQueryException("Could not find Relation for id: " + kbId.getObjectID());
			}
			return relations.get(0);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for relation with ID = "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * TODO
	 * 
	 * This is a temporary method. It should be removed when queries are folded
	 * together.
	 * 
	 * This method should look up the type of the object passed in as an
	 * argument, then call the appropriate method based on type to query and
	 * create the object. Then the object should be returned.
	 * 
	 * @param argument
	 * @return
	 */
	private KBPredicateArgument getPredicateArgumentByID(String argumentKbUri,
			Connection sqlConnection) throws KBQueryException {
		String argumentType = getLeafTypeFromId(argumentKbUri);

		KBID predicateArgumentKBID = new KBID(argumentKbUri, KBOntologyModel.DATA_INSTANCES_PREFIX);
		if (kbOntologyModel.getRelationArgumentTypes().keySet().contains(argumentType)) {
			return getRelationById(predicateArgumentKBID);
		} else if (argumentType.equals("Sentiment")) {
			return getSentimentById(predicateArgumentKBID);
		} else if (argumentType.equals("Belief")) {
			return getBeliefById(predicateArgumentKBID);
		} else if (argumentType.equals("Number")) {
			return getNumberValueByID(predicateArgumentKBID);
		} else if (argumentType.equals("Date")) {
			return getKBDateByDateId(predicateArgumentKBID);
		} else if (argumentType.equals("TemporalSpan")) {
			return getTemporalSpanByID(predicateArgumentKBID);
		} else if (argumentType.equals("Statement")) {
			return getArgumentRelationArgumentById(predicateArgumentKBID.getObjectID(),
					sqlConnection);
		} else if (kbOntologyModel.getEntityTypes().contains(argumentType)) {
			return getEntityById(predicateArgumentKBID);
		}
		throw new KBQueryException("Could not load KBPredicateArgument for id: " + argumentKbUri);
	}

	/**
	 * <p>
	 * Query KB sentiment given corresponding KBID.
	 * </p>
	 * 
	 * 
	 * <p>
	 * Note that this method does not return any information about whether this
	 * entity maps to any external KB sentiment. You must separately query for
	 * this information using the getExternalKBIDs() API method.
	 * </p>
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return Adept DocumentSentiment object
	 */
	public KBSentiment getSentimentById(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBSentiment> sentiments = this.<KBSentiment> getRelationsFromResultSet(
					kbId.getObjectID(), resultSet, sqlConnection, KBSentiment.class);
			if (sentiments.isEmpty()) {
				throw new KBQueryException("Could not find Sentiment for id: " + kbId.getObjectID());
			}
			return sentiments.get(0);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for sentiment with ID = "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * <p>
	 * Query KB belief given corresponding KBID.
	 * </p>
	 * 
	 * 
	 * <p>
	 * Note that this method does not return any information about whether this
	 * entity maps to any external KB belief. You must separately query for this
	 * information using the getExternalKBIDs() API method.
	 * </p>
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return KBBelief object
	 */

	public KBBelief getBeliefById(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBBelief> beliefs = this.<KBBelief> getRelationsFromResultSet(kbId.getObjectID(),
					resultSet, sqlConnection, KBBelief.class);
			if (beliefs.isEmpty()) {
				throw new KBQueryException("Could not find Belief for id: " + kbId.getObjectID());
			}
			return beliefs.get(0);
		} catch (SQLException ex) {
			throw new KBQueryException(
					"Failed to query for belief with ID = " + kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * <p>
	 * Query KB TemporalSpan given corresponding KBID.
	 * </p>
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return Adept TemporalSpan object
	 */
	public KBTemporalSpan getTemporalSpanByID(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBTemporalSpan> spans = this.<KBTemporalSpan> getRelationsFromResultSet(
					kbId.getObjectID(), resultSet, sqlConnection, KBTemporalSpan.class);
			if (spans.isEmpty()) {
				throw new KBQueryException("Could not find TemporalSpan for id: "
						+ kbId.getObjectID());
			}
			return spans.get(0);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for temporal span with ID = "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Query KB event by KBID
	 * 
	 * @return Adept Event object
	 */

	public KBEvent getEventById(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBEvent> events = this.<KBEvent> getRelationsFromResultSet(kbId.getObjectID(),
					resultSet, sqlConnection, KBEvent.class);
			if (events.isEmpty()) {
				throw new KBQueryException("Could not find Event for id: " + kbId.getObjectID());
			}
			return events.get(0);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for event with ID = " + kbId.getObjectID(),
					ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * @param kbId
	 * @return
	 */
	private Map<OntType, Float> getEventRealisTypes(KBID kbId) {
		Query query = sparqlQueryBuilder.createEventRealisTypesQuery(kbId);
		QueryExecution qexec = null;
		try{
			qexec = sparqlService.getQueryExecution(query);
			ResultSet results = qexec.execSelect();
			Map<OntType, Float> result = new HashMap<OntType, Float>();
			while (results.hasNext()) {
				QuerySolution solution = results.next();
				OntType realisType = new OntType(solution.getResource("?type"));
				float confidence = solution.getLiteral("?confidence").getFloat();
				result.put(realisType, confidence);
			}
			return result;
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * <p>
	 * Query entity objects by mention value. case insensitive exact string
	 * match.
	 * </p>
	 * 
	 * 
	 * @param value
	 * @return List of Adept KB entity IDs that have atleast one mention that
	 *         matches the argument.
	 * @throws adept.kbapi.KBQueryException
	 */

	public List<KBEntity> getEntitiesByStringReference(String value) throws KBQueryException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			return getEntitiesByStringReference(value, sqlConnection);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with reference " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private List<KBEntity> getEntitiesByStringReference(String value, Connection sqlConnection)
			throws SQLException, KBQueryException {
		PreparedStatement entitiesByStringReferenceQueryPreparedStmt = null;
		java.sql.ResultSet entitiesByStringReferenceQueryResult = null;

		try {
			entitiesByStringReferenceQueryPreparedStmt = SqlQueryBuilder
					.createEntityIdsByChunkValueQuery(value.toLowerCase(), sqlConnection);
			entitiesByStringReferenceQueryResult = entitiesByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredEntities = new ArrayList<String>();
			while (entitiesByStringReferenceQueryResult.next()) {
				String entityId = entitiesByStringReferenceQueryResult.getString("KBId");
				if (entityId != null && !discoveredEntities.contains(entityId)) {
					discoveredEntities.add(entityId);
				}
			}

			return getEntitiesByIds(discoveredEntities);

		} finally {
			try {
				if (entitiesByStringReferenceQueryResult != null)
					entitiesByStringReferenceQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (entitiesByStringReferenceQueryPreparedStmt != null)
					entitiesByStringReferenceQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * <p>
	 * Query entity objects by mention value. Regex match. Uses SQL regexes.
	 * </p>
	 * 
	 * @param regex
	 * @return List of Adept KB entity IDs that have atleast one mention that
	 *         matches the argument.
	 * @throws adept.kbapi.KBQueryException
	 */

	public List<KBEntity> getEntitiesByRegexMatch(String regex, boolean caseSensitive)
			throws KBQueryException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			return getEntitiesByRegexMatch(regex, sqlConnection, caseSensitive);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with reference " + regex, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private List<KBEntity> getEntitiesByRegexMatch(String regex, Connection sqlConnection,
			boolean caseSensitive) throws KBQueryException, SQLException {
		PreparedStatement entitiesByRegexQueryPreparedStmt = null;
		java.sql.ResultSet entitiesByRegexQueryResult = null;

		try {
			entitiesByRegexQueryPreparedStmt = SqlQueryBuilder.createEntityIdsByChunkRegexQuery(
					regex, sqlConnection, caseSensitive);
			entitiesByRegexQueryResult = entitiesByRegexQueryPreparedStmt.executeQuery();
			List<String> discoveredEntities = new ArrayList<String>();
			while (entitiesByRegexQueryResult.next()) {
				String entityId = entitiesByRegexQueryResult.getString("KBId");
				if (entityId != null && !discoveredEntities.contains(entityId)) {
					discoveredEntities.add(entityId);
				}
			}

			return getEntitiesByIds(discoveredEntities);

		} finally {
			try {
				if (entitiesByRegexQueryResult != null)
					entitiesByRegexQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (entitiesByRegexQueryPreparedStmt != null)
					entitiesByRegexQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * get Adept KB entities by type.
	 * 
	 * @return List<KBID>
	 */

	public List<KBEntity> getEntitiesByType(OntType type) throws KBQueryException {
		List<String> entities = new ArrayList<String>();

		String typeString = type.getType();

		Query query = sparqlQueryBuilder.createGetSubjectsByTypeQuery(typeString);
		QueryExecution qexec = null;
		
		try{ 
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
	
			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
	
				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();
	
				entities.add(subjectAsString.split("#")[1]);
			}
	
			return getEntitiesByIds(entities);
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB object corresponding to an external KB entity. The method
	 * will return an Adept KB object, given the external KB ID has already been
	 * stored in the database.
	 * 
	 * @return KBPredicateArgument
	 */
	public Optional<KBPredicateArgument> getKBObjectByExternalID(KBID externalKbId)
			throws KBQueryException {
		Preconditions.checkNotNull(externalKbId);

		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			sqlConnection = quickJDBC.getConnection();
			Query query = sparqlQueryBuilder.createGetAdeptIdByExternalIdAndName(
					externalKbId.getObjectID(), externalKbId.getKBNamespace());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();
				String kbObjectURI = subjectAsString.split("#")[1];

				return Optional.fromNullable(getPredicateArgumentByID(kbObjectURI, sqlConnection));
			} else {
                                return Optional.<KBPredicateArgument>absent();
			}
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for objects for external id: "
					+ externalKbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB entities by value and type.
	 * 
	 * 
	 * @param value
	 * @param type
	 * @return List<KBID>
	 * @throws adept.kbapi.KBQueryException
	 */

	public List<KBEntity> getEntitiesByValueAndType(String value, OntType type)
			throws KBQueryException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			return getEntitiesByValueAndType(value, type, sqlConnection);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with type " + type.getType()
					+ " and reference " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private List<KBEntity> getEntitiesByValueAndType(String value, OntType type,
			Connection sqlConnection) throws KBQueryException {
		try {
			List<KBEntity> entities = new ArrayList<KBEntity>();
			List<KBEntity> valueMatchedEntities = getEntitiesByStringReference(value, sqlConnection);

			for (KBEntity valueMatchedEntity : valueMatchedEntities) {
				if (valueMatchedEntity.getTypes().containsKey(type)) {
					entities.add(valueMatchedEntity);
				}
			}

			return entities;

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for entities with type " + type.getType()
					+ " and reference " + value, ex);
		}
	}

	/**
	 * get Adept KB Date IDs by XSD Date value.
	 * 
	 * Returns Optiona.absent if date not found.
	 * 
	 * @return KBID
	 */

	public Optional<KBDate> getDateByXSDDateValue(String value) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetDateByXSDValueQuery(value);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String dateURI = subjectAsString.split("#")[1];

				KBDate.InsertionBuilder dateBuilder = KBDate.xsdDateInsertionBuilder(value);
				List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
						dateURI, sqlConnection);
				for (KBTextProvenance.InsertionBuilder provenance : provenances) {
					dateBuilder.addProvenance(provenance);
				}

				dateBuilder.setKBID(new KBID(dateURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(dateBuilder.build());
			} else {
				return Optional.absent();
			}
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for date with Timex value = " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB Date IDs by value. Returns Optional.absent if date does not
	 * exist in kb.
	 * 
	 * @return KBDate
	 */

	public Optional<KBDate> getDateByTimex2Value(String value) throws KBQueryException {

		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetDateByTimexValueQuery(value);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String dateURI = subjectAsString.split("#")[1];

				KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(value);
				List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
						dateURI, sqlConnection);
				for (KBTextProvenance.InsertionBuilder provenance : provenances) {
					dateBuilder.addProvenance(provenance);
				}

				dateBuilder.setKBID(new KBID(dateURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(dateBuilder.build());
			} else {
				return Optional.absent();
			}
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for date with Timex value = " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * <p>
	 * Query KB Date given corresponding Adept KBID object.
	 * </p>
	 * 
	 * 
	 * @param kbId
	 *            KBID
	 * 
	 * @return Adept XSDDate object
	 */

	public KBDate getKBDateByDateId(KBID kbId) throws KBQueryException {
		Preconditions.checkNotNull(kbId);
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			String kbUri = kbId.getObjectID();

			Query query = sparqlQueryBuilder.createGetTimexValueForDateIDQuery(kbUri);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
			String timexString = null;
			if (resultSet.hasNext()) {
				timexString = resultSet.next().get(SparqlQueryBuilder.VALUE).asLiteral()
						.getLexicalForm();
			}
			KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(timexString);
			List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
					kbUri, sqlConnection);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				dateBuilder.addProvenance(provenance);
			}

			dateBuilder.setKBID(kbId);
			return dateBuilder.build();
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for date with ID = " + kbId.getObjectID(),
					ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB relations by KB argument URI.
	 * 
	 * @return List<KBRelation>
	 */
	public List<KBRelation> getRelationsByArg(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationsByArgumentQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with arg "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Get the KBRelation that owns the KBRelationArgument referred to by the
	 * passed KBID. If the KBID does not belong to a KBRelationArgument, a
	 * KBQueryException will be thrown.
	 * 
	 * @return List of document relation IDs containing input URI as argument
	 */

	public KBRelation getKBRelationByKBRelationArgument(KBID kbId) throws KBQueryException {
		QueryExecution qexec = null;
		try {
			String kbUri = kbId.getObjectID();

			Query query = sparqlQueryBuilder.createGetKBRelationByKBRelationArgumentQuery(kbUri);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				String relationURI = item.getResource("relationID").getURI();
				String relationID = relationURI.substring(relationURI.indexOf('#') + 1);
				KBID relationKBID = new KBID(relationID, KBOntologyModel.DATA_INSTANCES_PREFIX);
				return getRelationById(relationKBID);
			} else {
				throw new KBQueryException(
						"Could not find an owning Relation for KBRelationArgument id: "
								+ kbId.getObjectID());
			}

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with arg "
					+ kbId.getObjectID(), ex);
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB sentiments by KB argument URI.
	 * 
	 * @return List<KBSentiment>
	 */
	public List<KBSentiment> getSentimentsByArg(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetSentimentsByArgumentQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBSentiment> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBSentiment.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with arg "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB belief IDs by KB argument URI.
	 * 
	 * @return List<KBBelief>
	 */
	public List<KBBelief> getBeliefsByArg(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetBeliefsByArgumentQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBBelief> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBBelief.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with arg "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB relations by mention value.
	 * 
	 * @return List<KBRelation>
	 */

	public List<KBRelation> getRelationsByStringReference(String value) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			sqlConnection = quickJDBC.getConnection();

			List<String> validRelations = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByStringReference(value);
			for (String objectId : objectIds) {
				validRelations.add(objectId);
			}
			if (!validRelations.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetRelationsByIdsQuery(validRelations);
				qexec = sparqlService.getQueryExecution(query);
				com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				return this.<KBRelation> getRelationsFromResultSet(null, resultSet, sqlConnection,
						KBRelation.class);
			} else {
				return Collections.emptyList();
			}

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with reference " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB events by mention value.
	 * 
	 * @return List<KBEvent>
	 */

	public List<KBEvent> getEventsByStringReference(String value) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			sqlConnection = quickJDBC.getConnection();

			List<String> validEvents = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByStringReference(value);
			for (String objectId : objectIds) {
				validEvents.add(objectId);
			}

			if (!validEvents.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetEventsByIdsQuery(validEvents);
				qexec = sparqlService.getQueryExecution(query);
				com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				return this.<KBEvent> getRelationsFromResultSet(null, resultSet, sqlConnection,
						KBEvent.class);
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with reference " + value, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	private List<String> getObjectIdsByStringReference(String value) throws SQLException {
		Connection sqlConnection = null;
		PreparedStatement relationsByStringReferenceQueryPreparedStmt = null;
		java.sql.ResultSet relationsByStringReferenceQueryResult = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			relationsByStringReferenceQueryPreparedStmt = SqlQueryBuilder
					.createRelationIdsByChunkValueQuery(value, sqlConnection);
			relationsByStringReferenceQueryResult = relationsByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredObjects = new ArrayList<String>();
			while (relationsByStringReferenceQueryResult.next()) {
				String objectId = relationsByStringReferenceQueryResult.getString("KBId");
				discoveredObjects.add(objectId);
			}

			return discoveredObjects;
		} finally {
			try {
				if (relationsByStringReferenceQueryResult != null)
					relationsByStringReferenceQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (relationsByStringReferenceQueryPreparedStmt != null)
					relationsByStringReferenceQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * get Adept KB sentiments by mention value.
	 * 
	 * @return List<KBSentiment>
	 */
	public List<KBSentiment> getSentimentsByStringReference(String value) throws KBQueryException {
		Connection sqlConnection = null;
		PreparedStatement sentimentsByStringReferenceQueryPreparedStmt = null;
		java.sql.ResultSet sentimentsByStringReferenceQueryResult = null;
		QueryExecution qexec = null;
		try {
			sqlConnection = quickJDBC.getConnection();
			sentimentsByStringReferenceQueryPreparedStmt = SqlQueryBuilder
					.createRelationIdsByChunkValueQuery(value, sqlConnection);
			sentimentsByStringReferenceQueryResult = sentimentsByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredRelations = new ArrayList<String>();
			while (sentimentsByStringReferenceQueryResult.next()) {
				String sentimentId = sentimentsByStringReferenceQueryResult.getString("KBId");
				discoveredRelations.add(sentimentId);
			}

			if (!discoveredRelations.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetSentimentsByIdsQuery(discoveredRelations);
				qexec = sparqlService.getQueryExecution(query);
				com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				return this.<KBSentiment> getRelationsFromResultSet(null, resultSet, sqlConnection,
						KBSentiment.class);
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with reference " + value, ex);
		} finally {
			try {
				if (sentimentsByStringReferenceQueryResult != null)
					sentimentsByStringReferenceQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sentimentsByStringReferenceQueryPreparedStmt != null)
					sentimentsByStringReferenceQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB beliefs by mention value.
	 * 
	 * @return List<KBBelief>
	 */
	public List<KBBelief> getBeliefsByStringReference(String value) throws KBQueryException {
		Connection sqlConnection = null;
		PreparedStatement beliefsByStringReferenceQueryPreparedStmt = null;
		java.sql.ResultSet beliefsByStringReferenceQueryResult = null;
		QueryExecution qexec = null;
		try {
			sqlConnection = quickJDBC.getConnection();
			beliefsByStringReferenceQueryPreparedStmt = SqlQueryBuilder
					.createRelationIdsByChunkValueQuery(value, sqlConnection);
			beliefsByStringReferenceQueryResult = beliefsByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredRelations = new ArrayList<String>();
			while (beliefsByStringReferenceQueryResult.next()) {
				String beliefId = beliefsByStringReferenceQueryResult.getString("KBId");
				discoveredRelations.add(beliefId);
			}

			if (!discoveredRelations.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetBeliefsByIdsQuery(discoveredRelations);
				qexec = sparqlService.getQueryExecution(query);
				com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				return this.<KBBelief> getRelationsFromResultSet(null, resultSet, sqlConnection,
						KBBelief.class);
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for beliefs with reference " + value, ex);
		} finally {
			try {
				if (beliefsByStringReferenceQueryResult != null)
					beliefsByStringReferenceQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (beliefsByStringReferenceQueryPreparedStmt != null)
					beliefsByStringReferenceQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get Adept KB relations by Type.
	 * 
	 * @return List<KBRelation> corresponding to input type
	 */
	public List<KBRelation> getRelationsByType(OntType type) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			String typeString = type.getType();

			Query query = sparqlQueryBuilder.createGetRelationsByTypeQuery(typeString);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with type " + type.getType(),
					ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private <T extends KBRelation> List<T> getRelationsFromResultSet(String inputRelationId,
			com.hp.hpl.jena.query.ResultSet resultSet, Connection sqlConnection, Class<T> classType)
			throws KBQueryException, SQLException {
		List<T> kbRelations = new ArrayList<T>();
		List<T.AbstractInsertionBuilder<?, T>> relationBuilders = new ArrayList<T.AbstractInsertionBuilder<?, T>>();

		Map<String, List<QuerySolution>> entityArguments = new HashMap<String, List<QuerySolution>>();
		Map<String, Map<String, Map<OntType, Float>>> entityTypeMap = new HashMap<String, Map<String, Map<OntType, Float>>>();

		Map<String, List<QuerySolution>> relationArgumentArguments = new HashMap<String, List<QuerySolution>>();
		Map<String, List<Pair<OntType, QuerySolution>>> relationArguments = new HashMap<String, List<Pair<OntType, QuerySolution>>>();
		Map<String, List<QuerySolution>> numberArguments = new HashMap<String, List<QuerySolution>>();
		Map<String, List<QuerySolution>> temporalSpanArguments = new HashMap<String, List<QuerySolution>>();
		Map<String, List<QuerySolution>> dateArguments = new HashMap<String, List<QuerySolution>>();
		Map<String, List<QuerySolution>> genericThingArguments = new HashMap<String, List<QuerySolution>>();

		HashSet<String> idsToQueryForProvenances = new HashSet<String>();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();
			String relationId = inputRelationId;
			if (relationId == null) {
				relationId = item.get("?relationId").asResource().getURI().split("#")[1];
			}
			boolean doesRelationBuilderExist = false;
			for (T.AbstractInsertionBuilder<?, ?> relationInsertionBuilder : relationBuilders) {
				if (relationInsertionBuilder.getKBID().getObjectID().equals(relationId)) {
					doesRelationBuilderExist = true;
					break;
				}
			}
			if (!doesRelationBuilderExist) {
				T.AbstractInsertionBuilder<?, T> relationInsertionBuilder = null;
				if (classType.equals(KBSentiment.class)) {
					relationInsertionBuilder = (AbstractInsertionBuilder<?, T>) KBSentiment
							.sentimentInsertionBuilder(-1);
				} else if (classType.equals(KBBelief.class)) {
					relationInsertionBuilder = (AbstractInsertionBuilder<?, T>) KBBelief
							.beliefInsertionBuilder(-1);
				} else if (classType.equals(KBTemporalSpan.class)) {
					relationInsertionBuilder = (AbstractInsertionBuilder<?, T>) KBTemporalSpan
							.temporalSpanInsertionBuilder(-1);
				} else if (classType.equals(KBEvent.class)) {
					relationInsertionBuilder = (AbstractInsertionBuilder<?, T>) KBEvent
							.eventInsertionBuilder();
				} else {
					relationInsertionBuilder = (AbstractInsertionBuilder<?, T>) KBRelation
							.relationInsertionBuilder();
				}
				relationInsertionBuilder.setKBID(new KBID(relationId,
						KBOntologyModel.DATA_INSTANCES_PREFIX));
				relationInsertionBuilder.setRelationType(new OntType(item.get("?relationType")
						.asResource()));
				relationInsertionBuilder.setConfidence(item.get("?relationConfidence").asLiteral()
						.getFloat());
				relationBuilders.add(relationInsertionBuilder);
				idsToQueryForProvenances.add(relationId);
			}

			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			idsToQueryForProvenances.add(argumentId);

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			idsToQueryForProvenances.add(relationArgumentId);

			if (item.get("?entityCanonicalMention") != null) {
				List<QuerySolution> entities = entityArguments.get(relationId);
				if (entities == null) {
					entities = new ArrayList<QuerySolution>();
					entityArguments.put(relationId, entities);
				}

				Map<String, Map<OntType, Float>> relationEntityTypeMap = entityTypeMap
						.get(relationId);
				if (relationEntityTypeMap == null) {
					relationEntityTypeMap = new HashMap<String, Map<OntType, Float>>();
					entityTypeMap.put(relationId, relationEntityTypeMap);
				}
				Map<OntType, Float> entityTypes = relationEntityTypeMap.get(argumentId);
				if (entityTypes == null) {
					entities.add(item);
					entityTypes = new HashMap<OntType, Float>();
					relationEntityTypeMap.put(argumentId, entityTypes);
				}
				entityTypes.put(new OntType(item.get("?entityType").asResource()),
						item.get("?entityTypeConfidence").asLiteral().getFloat());
			} else if (item.get("?relationArgumentStatement") != null) {
				List<QuerySolution> relationArgumentList = relationArgumentArguments
						.get(relationId);
				if (relationArgumentList == null) {
					relationArgumentList = new ArrayList<QuerySolution>();
					relationArgumentArguments.put(relationId, relationArgumentList);
				}
				relationArgumentList.add(item);
			} else if (item.get("?numberValue") != null) {
				List<QuerySolution> numbers = numberArguments.get(relationId);
				if (numbers == null) {
					numbers = new ArrayList<QuerySolution>();
					numberArguments.put(relationId, numbers);
				}
				numbers.add(item);
			} else if (item.get("?beginDateStatement") != null
					&& item.get("?endDateStatement") != null) {
				List<QuerySolution> temporalSpans = temporalSpanArguments.get(relationId);
				if (temporalSpans == null) {
					temporalSpans = new ArrayList<QuerySolution>();
					temporalSpanArguments.put(relationId, temporalSpans);
				}
				temporalSpans.add(item);

				if (item.get("?beginDateStatement") != null) {
					idsToQueryForProvenances.add(item.get("?beginDateStatement").asResource()
							.getURI().split("#")[1]);
					idsToQueryForProvenances.add(item.get("?beginDate").asResource().getURI()
							.split("#")[1]);
				}
				if (item.get("?endDateStatement") != null) {
					idsToQueryForProvenances.add(item.get("?endDateStatement").asResource()
							.getURI().split("#")[1]);
					idsToQueryForProvenances.add(item.get("?endDate").asResource().getURI()
							.split("#")[1]);
				}
			} else if (item.get("?date") != null) {
				List<QuerySolution> dates = dateArguments.get(relationId);
				if (dates == null) {
					dates = new ArrayList<QuerySolution>();
					dateArguments.put(relationId, dates);
				}
				dates.add(item);
			} else if (item.get("?argumentRelationType") != null) {
				OntType argumentRelationType = new OntType(item.get("?argumentRelationType")
						.asResource());
				if (!argumentRelationType.getType().equals("TemporalSpan")) {
					List<Pair<OntType, QuerySolution>> relations = relationArguments
							.get(relationId);
					if (relations == null) {
						relations = new ArrayList<Pair<OntType, QuerySolution>>();
						relationArguments.put(relationId, relations);
					}
					relations.add(new Pair<OntType, QuerySolution>(argumentRelationType, item));
				}
			} else if (item.get("?genericThingCanonicalString") != null) {
				List<QuerySolution> genericThings = genericThingArguments.get(relationId);
				if (genericThings == null) {
					genericThings = new ArrayList<QuerySolution>();
					genericThingArguments.put(relationId, genericThings);
				}
				genericThings.add(item);
			}
		}

		List<String> idsToQueryForProvenancesList = new ArrayList<String>();
		idsToQueryForProvenancesList.addAll(idsToQueryForProvenances);
		Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
				idsToQueryForProvenancesList, sqlConnection);

		for (T.AbstractInsertionBuilder<?, T> relationInsertionBuilder : relationBuilders) {
			String relationId = relationInsertionBuilder.getKBID().getObjectID();
			List<KBTextProvenance.InsertionBuilder> relationProvenances = textProvenancesMap
					.get(relationId);
			if (relationProvenances != null) {
				relationInsertionBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(relationProvenances));
			}

			List<QuerySolution> entities = entityArguments.get(relationId);
			if (entities != null) {
				List<KBRelationArgument.InsertionBuilder> entityArgs = buildRelationArgumentsFromEntityQuerySolutions(
						null, entities, textProvenancesMap, entityTypeMap.get(relationId));
				for (KBRelationArgument.InsertionBuilder entityArgument : entityArgs) {
					relationInsertionBuilder.addArgument(entityArgument);
				}
			}

			List<QuerySolution> relationArgumentList = relationArgumentArguments.get(relationId);
			if (relationArgumentList != null) {
				for (QuerySolution item : relationArgumentList) {
					String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
					KBRelationArgument relationArgument = getArgumentRelationArgumentById(
							argumentId, sqlConnection);

					String relationArgumentId = item.get("?kbRelationArgumentID").asResource()
							.getURI().split("#")[1];
					relationInsertionBuilder.addArgument(buildKBRelationArgument(new OntType(item
							.get("?role").asResource()), relationArgument,
							item.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									relationArgumentId, KBOntologyModel.DATA_INSTANCES_PREFIX),
							textProvenancesMap.get(relationArgumentId)));
				}
			}

			List<Pair<OntType, QuerySolution>> relations = relationArguments.get(relationId);
			if (relations != null) {
				for (Pair<OntType, QuerySolution> relationItem : relations) {
					OntType relationArgumentType = relationItem.getL();
					QuerySolution item = relationItem.getR();

					String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
					KBRelation relation;
					if (relationArgumentType.getType().equals("Sentiment")) {
						relation = getSentimentById(new KBID(argumentId,
								KBOntologyModel.DATA_INSTANCES_PREFIX));
					} else if (relationArgumentType.getType().equals("Belief")) {
						relation = getBeliefById(new KBID(argumentId,
								KBOntologyModel.DATA_INSTANCES_PREFIX));
					} else if (kbOntologyModel.getLeafEventTypes().contains(
							relationArgumentType.getType())) {
						relation = getEventById(new KBID(argumentId,
								KBOntologyModel.DATA_INSTANCES_PREFIX));
					} else {
						relation = getRelationById(new KBID(argumentId,
								KBOntologyModel.DATA_INSTANCES_PREFIX));
					}

					String relationArgumentId = item.get("?kbRelationArgumentID").asResource()
							.getURI().split("#")[1];
					relationInsertionBuilder.addArgument(buildKBRelationArgument(new OntType(item
							.get("?role").asResource()), relation, item.get("?argumentConfidence")
							.asLiteral().getFloat(), new KBID(relationArgumentId,
							KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
							.get(relationArgumentId)));
				}
			}

			List<QuerySolution> numbers = numberArguments.get(relationId);
			if (numbers != null) {
				List<KBRelationArgument.InsertionBuilder> numberArgs = buildRelationArgumentsFromNumberQuerySolutions(
						numbers, textProvenancesMap);
				for (KBRelationArgument.InsertionBuilder numberArgument : numberArgs) {
					relationInsertionBuilder.addArgument(numberArgument);
				}
			}

			List<QuerySolution> temporalSpans = temporalSpanArguments.get(relationId);
			if (temporalSpans != null) {
				List<KBRelationArgument.InsertionBuilder> temporalSpanArgs = buildRelationArgumentsFromTemporalSpanQuerySolutions(
						temporalSpans, textProvenancesMap);
				for (KBRelationArgument.InsertionBuilder temporalSpanArgument : temporalSpanArgs) {
					relationInsertionBuilder.addArgument(temporalSpanArgument);
				}
			}

			List<QuerySolution> dates = dateArguments.get(relationId);
			if (dates != null) {
				List<KBRelationArgument.InsertionBuilder> dateArgs = buildRelationArgumentsFromDateQuerySolutions(
						dates, textProvenancesMap);
				for (KBRelationArgument.InsertionBuilder dateArgument : dateArgs) {
					relationInsertionBuilder.addArgument(dateArgument);
				}
			}

			List<QuerySolution> genericThings = genericThingArguments.get(relationId);
			if (genericThings != null) {
				List<KBRelationArgument.InsertionBuilder> genericThingArgs = buildRelationArgumentsFromGenericThingQuerySolutions(
						genericThings, textProvenancesMap);
				for (KBRelationArgument.InsertionBuilder genericThingArgument : genericThingArgs) {
					relationInsertionBuilder.addArgument(genericThingArgument);
				}
			}

			if (classType.equals(KBEvent.class)) {
				Map<OntType, Float> realisTypes = getEventRealisTypes(relationInsertionBuilder
						.getKBID());
				for (OntType realisType : realisTypes.keySet()) {
					((KBEvent.InsertionBuilder) relationInsertionBuilder).addRealisType(realisType,
							realisTypes.get(realisType));
				}
			}

			kbRelations.add((T) relationInsertionBuilder.build());
		}

		return kbRelations;
	}

	private KBRelationArgument getArgumentRelationArgumentById(String originalArgumentId,
			Connection sqlConnection) throws KBQueryException {
		
		QueryExecution qexec = null;
		try {
			KBRelationArgument.InsertionBuilder kbRelationArgumentBuilder = null;

			Query query = sparqlQueryBuilder
					.createGetArgumentRelationArgumentQuery(originalArgumentId);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<String> idsToQueryForProvenances = new ArrayList<String>();
			idsToQueryForProvenances.add(originalArgumentId);

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
				idsToQueryForProvenances.add(argumentId);

				if (item.get("?entityCanonicalMention") != null) {
					Map<OntType, Float> entityTypes = new HashMap<OntType, Float>();
					entityTypes.put(new OntType(item.get("?entityType").asResource()),
							item.get("?entityTypeConfidence").asLiteral().getFloat());
					while (resultSet.hasNext()) {
						item = resultSet.next();
						entityTypes.put(new OntType(item.get("?entityType").asResource()), item
								.get("?entityTypeConfidence").asLiteral().getFloat());
					}
					Map<String, Map<OntType, Float>> entityTypeMap = new HashMap<String, Map<OntType, Float>>();
					entityTypeMap.put(argumentId, entityTypes);

					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildRelationArgumentsFromEntityQuerySolutions(
							originalArgumentId, Arrays.asList(item), textProvenancesMap,
							entityTypeMap).get(0);
				} else if (item.get("?relationArgumentStatement") != null) {
					KBRelationArgument relationArgument = getArgumentRelationArgumentById(
							argumentId, sqlConnection);

					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildKBRelationArgument(
							new OntType(item.get("?role").asResource()), relationArgument, item
									.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX),
							textProvenancesMap.get(argumentId));
				} else if (item.get("?numberValue") != null) {
					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildRelationArgumentsFromNumberQuerySolutions(
							Arrays.asList(item), textProvenancesMap).get(0);
				} else if (item.get("?beginDateStatement") != null
						&& item.get("?endDateStatement") != null) {
					if (item.get("?beginDateStatement") != null) {
						idsToQueryForProvenances.add(item.get("?beginDateStatement").asResource()
								.getURI().split("#")[1]);
					}
					if (item.get("?endDateStatement") != null) {
						idsToQueryForProvenances.add(item.get("?endDateStatement").asResource()
								.getURI().split("#")[1]);
					}

					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildRelationArgumentsFromTemporalSpanQuerySolutions(
							Arrays.asList(item), textProvenancesMap).get(0);
				} else if (item.get("?date") != null) {
					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildRelationArgumentsFromDateQuerySolutions(
							Arrays.asList(item), textProvenancesMap).get(0);
				} else {
					KBRelation relation = getRelationById(new KBID(argumentId,
							KBOntologyModel.DATA_INSTANCES_PREFIX));

					Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
							idsToQueryForProvenances, sqlConnection);

					kbRelationArgumentBuilder = buildKBRelationArgument(
							new OntType(item.get("?role").asResource()), relation,
							item.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX),
							textProvenancesMap.get(argumentId));
				}
			}

			if (kbRelationArgumentBuilder != null) {
				return kbRelationArgumentBuilder.build(new KBID(originalArgumentId,
						KBOntologyModel.DATA_INSTANCES_PREFIX));
			} else {
				throw new KBQueryException("No argument of a relation argument exists for id "
						+ originalArgumentId);
			}
		} catch (Exception ex) {
			throw new KBQueryException(
					"Failed to query for argument of a relation argument for id "
							+ originalArgumentId, ex);
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromEntityQuerySolutions(
			String inputRelationArgumentId, List<QuerySolution> entities,
			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap,
			Map<String, Map<OntType, Float>> entityTypeMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : entities) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			float canonicalMentionConfidence = item.get("?canonicalMentionConfidence").asLiteral()
					.getFloat();
			float entityConfidence = item.get("?entityconfidence").asLiteral().getFloat();
			String canonicalMentionId = item.get("?entityCanonicalMention").asLiteral().getString();
			KBTextProvenance.InsertionBuilder canonicalMention = null;
			List<KBTextProvenance.InsertionBuilder> provenances = textProvenancesMap
					.get(argumentId);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				if (provenance.getKBID().getObjectID().equals(canonicalMentionId)) {
					canonicalMention = provenance;
					break;
				}
			}

			Map<OntType, Float> entityTypes = entityTypeMap.get(argumentId);
			KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(entityTypes,
					canonicalMention, entityConfidence, canonicalMentionConfidence);
			entityBuilder.addProvenances(Sets
					.<KBProvenance.InsertionBuilder> newHashSet(provenances));
			entityBuilder.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			String relationArgumentId = inputRelationArgumentId;
			if (relationArgumentId == null) {
				relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
						.split("#")[1];
			}
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), entityBuilder.build(), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
					.get(relationArgumentId)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromNumberQuerySolutions(
			List<QuerySolution> numbers,
			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : numbers) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			Number number = convertToNumber(item.getLiteral("?numberValue"));
			KBNumber.InsertionBuilder numberBuilder = KBNumber.numberInsertionBuilder(number);
			numberBuilder.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			List<KBTextProvenance.InsertionBuilder> numberProvenances = textProvenancesMap
					.get(argumentId);
			if (numberProvenances != null) {
				numberBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(numberProvenances));
			}

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), numberBuilder.build(), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
					.get(relationArgumentId)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromTemporalSpanQuerySolutions(
			List<QuerySolution> temporalSpans,
			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : temporalSpans) {
			// TODO: confidences of temporal spans are not in the kb at the
			// moment, once that's fixed, add them to this query
			// TODO: provenances of the begin & end date arguments of a temporal
			// span are not inserted into the kb, once that's fixed, add them to
			// this query
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			KBTemporalSpan.InsertionBuilder temporalSpanBuilder = new KBTemporalSpan.InsertionBuilder(
					1);
			temporalSpanBuilder.kbid = new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX);

			if (item.get("?beginDateStatement") != null) {
				String beginDateStatementId = item.get("?beginDateStatement").asResource().getURI()
						.split("#")[1];
				String beginDateId = item.get("?beginDate").asResource().getURI().split("#")[1];
				String timexString = item.get("?beginDateValue").asLiteral().getLexicalForm();
				KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(timexString);
				dateBuilder.setKBID(new KBID(beginDateId, KBOntologyModel.DATA_INSTANCES_PREFIX));

				List<KBTextProvenance.InsertionBuilder> dateProvenances = textProvenancesMap
						.get(beginDateId);
				if (dateProvenances != null) {
					dateBuilder.addProvenances(Sets
							.<KBProvenance.InsertionBuilder> newHashSet(dateProvenances));
				}

				// TODO: confidences of temporal span date arguments are not in
				// the kb at the moment, once that's fixed, add them to this
				// query
				KBRelationArgument.InsertionBuilder beginDateBuidler = temporalSpanBuilder
						.createBeginDateArgument(dateBuilder.build(), 1,
								new HashSet<KBProvenance.InsertionBuilder>());
				beginDateBuidler.kbid = new KBID(beginDateStatementId,
						KBOntologyModel.DATA_INSTANCES_PREFIX);
			}
			if (item.get("?endDateStatement") != null) {
				String endDateStatementId = item.get("?endDateStatement").asResource().getURI()
						.split("#")[1];
				String endDateId = item.get("?endDate").asResource().getURI().split("#")[1];
				String timexString = item.get("?endDateValue").asLiteral().getLexicalForm();
				KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(timexString);
				dateBuilder.setKBID(new KBID(endDateId, KBOntologyModel.DATA_INSTANCES_PREFIX));

				List<KBTextProvenance.InsertionBuilder> dateProvenances = textProvenancesMap
						.get(endDateId);
				if (dateProvenances != null) {
					dateBuilder.addProvenances(Sets
							.<KBProvenance.InsertionBuilder> newHashSet(dateProvenances));
				}

				// TODO: confidences of temporal span date arguments are not in
				// the kb at the moment, once that's fixed, add them to this
				// query
				KBRelationArgument.InsertionBuilder endDateBuilder = temporalSpanBuilder
						.createEndDateArgument(dateBuilder.build(), 1,
								new HashSet<KBProvenance.InsertionBuilder>());
				endDateBuilder.kbid = new KBID(endDateStatementId,
						KBOntologyModel.DATA_INSTANCES_PREFIX);
			}

			List<KBTextProvenance.InsertionBuilder> temporalSpanProvenances = textProvenancesMap
					.get(argumentId);
			if (temporalSpanProvenances != null) {
				temporalSpanBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(temporalSpanProvenances));
			}

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), temporalSpanBuilder.build(), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
					.get(relationArgumentId)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromDateQuerySolutions(
			List<QuerySolution> dates,
			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : dates) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			String timexString = item.get("?date").asLiteral().getLexicalForm();
			KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(timexString);
			dateBuilder.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			List<KBTextProvenance.InsertionBuilder> dateProvenances = textProvenancesMap
					.get(argumentId);
			if (dateProvenances != null) {
				dateBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(dateProvenances));
			}

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), dateBuilder.build(), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
					.get(relationArgumentId)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromGenericThingQuerySolutions(
			List<QuerySolution> genericThings,
			Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : genericThings) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			OntType genericThingType = new OntType(item.get("?genericThingType").asResource());
			String canonicalString = item.get("?genericThingCanonicalString").asLiteral()
					.getString();
			KBGenericThing.InsertionBuilder genericThingBuilder = KBGenericThing
					.genericThingInsertionBuilder(genericThingType, canonicalString);
			genericThingBuilder
					.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			List<KBTextProvenance.InsertionBuilder> genericThingProvenances = textProvenancesMap
					.get(argumentId);
			if (genericThingProvenances != null) {
				genericThingBuilder.addProvenances(Sets
						.<KBProvenance.InsertionBuilder> newHashSet(genericThingProvenances));
			}

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), genericThingBuilder.build(), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX), textProvenancesMap
					.get(relationArgumentId)));
		}

		return relationArguments;
	}

	private KBRelationArgument.InsertionBuilder buildKBRelationArgument(OntType type,
			KBPredicateArgument kbPredicateArgument, float confidence, KBID kbid,
			List<KBTextProvenance.InsertionBuilder> provenances) {
		KBRelationArgument.InsertionBuilder kbRelationArgument = KBRelationArgument
				.insertionBuilder(type, kbPredicateArgument, confidence);
		kbRelationArgument.setKBID(kbid);
		if (provenances != null) {
			kbRelationArgument.addProvenances(Sets
					.<KBProvenance.InsertionBuilder> newHashSet(provenances));
		}
		return kbRelationArgument;
	}

	/**
	 * get Adept KB relations by argument and type.
	 * 
	 * @return List<KBRelation>
	 */
	public List<KBRelation> getRelationsByArgAndType(KBID kbId, OntType type)
			throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationsByArgumentAndTypeQuery(
					type.getType(), kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with type " + type.getType()
					+ " and arg " + kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get KB event IDs given event argument
	 * 
	 * @return List<KBEvent> containing input URI as argument
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByArg(KBID kbId) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetEventsByArgumentQuery(kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with arg " + kbId.getObjectID(),
					ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get KB events by Type
	 * 
	 * @return List<KBEvent> corresponding to input type
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByType(OntType type) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			String typeString = type.getType();

			Query query = sparqlQueryBuilder.createGetRelationsByTypeQuery(typeString);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with type " + type.getType(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get KB events by argument and type
	 * 
	 * @return List<KBEvent>
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByArgAndType(KBID kbId, OntType type) throws KBQueryException {
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelationsByArgumentAndTypeQuery(
					type.getType(), kbId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet, sqlConnection,
					KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with type " + type.getType()
					+ " and arg " + kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get related entities upto given depth
	 * 
	 * 
	 * @return List of related entities
	 */

	public List<KBEntity> getRelatedEntities(KBID kbId, int depth) throws KBQueryException {
        Connection sqlConnection = null;                
        QueryExecution qexec = null;
		try {                        
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelatedEntitiesQuery(kbId.getObjectID(), depth);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
                        
            return getRelatedEntitiesFromResultSet(resultSet, sqlConnection);                        
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities related to ID = "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * get related entities by relation type upto given depth
	 * 
	 * 
	 * @return list of related entities
	 */

	public List<KBEntity> getRelatedEntitiesByRelationType(KBID kbId, int depth, OntType type) throws KBQueryException {
        Connection sqlConnection = null;                
        QueryExecution qexec = null;
		try {                        
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetRelatedEntitiesByRelationTypeQuery(kbId.getObjectID(), depth, type.getType());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
                        
            return getRelatedEntitiesFromResultSet(resultSet, sqlConnection);                        
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities related to ID = "
					+ kbId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}
        
    private List<KBEntity> getRelatedEntitiesFromResultSet(com.hp.hpl.jena.query.ResultSet resultSet, Connection sqlConnection) throws SQLException {                
        List<KBEntity> relatedEntities = new ArrayList<KBEntity>();

        Map<String, QuerySolution> entities = new HashMap<String, QuerySolution>();
        Map<String, Map<OntType, Float>> entityTypeMap = new HashMap<String, Map<OntType, Float>>();
        Set<String> idsToQueryForProvenances = new HashSet<String>();

        while (resultSet.hasNext()) {
            QuerySolution item = resultSet.next();

            String relatedEntityId = item.get("?relatedEntityId").asResource().getURI().split("#")[1];
            idsToQueryForProvenances.add(relatedEntityId);

            if (!entities.containsKey(relatedEntityId)){
                entities.put(relatedEntityId, item);
            }

            Map<OntType, Float> typeMap = entityTypeMap.get(relatedEntityId);
            if (typeMap == null) {
                typeMap = new HashMap<OntType, Float>();
                entityTypeMap.put(relatedEntityId, typeMap);
            }
            typeMap.put(new OntType(item.get("?entityType").asResource()),
                                item.get("?entityTypeConfidence").asLiteral().getFloat());
        }

        List<String> idsToQueryForProvenancesList = new ArrayList<String>();
        idsToQueryForProvenancesList.addAll(idsToQueryForProvenances);
        Map<String, List<KBTextProvenance.InsertionBuilder>> textProvenancesMap = getTextProvenancesByOwnerIds(
                        idsToQueryForProvenancesList, sqlConnection);

        for (String entityId : entities.keySet()) {
            QuerySolution item = entities.get(entityId);
            float canonicalMentionConfidence = item.get("?canonicalMentionConfidence").asLiteral().getFloat();
            float entityConfidence = item.get("?entityconfidence").asLiteral().getFloat();
            String canonicalMentionId = item.get("?entityCanonicalMention").asLiteral().getString();
            KBTextProvenance.InsertionBuilder canonicalMention = null;
            List<KBTextProvenance.InsertionBuilder> provenances = textProvenancesMap.get(entityId);
            for (KBTextProvenance.InsertionBuilder provenance : provenances) {
                    if (provenance.getKBID().getObjectID().equals(canonicalMentionId)) {
                            canonicalMention = provenance;
                            break;
                    }
            }

            Map<OntType, Float> entityTypes = entityTypeMap.get(entityId);

            KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(entityTypes,
                            canonicalMention, entityConfidence, canonicalMentionConfidence);
            entityBuilder.addProvenances(Sets.<KBProvenance.InsertionBuilder> newHashSet(provenances));
            entityBuilder.setKBID(new KBID(entityId, KBOntologyModel.DATA_INSTANCES_PREFIX));

            relatedEntities.add(entityBuilder.build());
        }

        return relatedEntities;
    }

	/**
	 * Get KB objects contained within input chunk
	 * 
	 * @param chunk
	 * @return
	 * @throws adept.kbapi.KBQueryException
	 */

	public List<KBPredicateArgument> getKBObjectsWithinChunk(Chunk chunk) throws KBQueryException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();

			// get source document ID and begin and end offsets from chunk
			String sourceDocId = chunk.getTokenStream().getDocument().getDocId();
			int beginOffset = chunk.getCharOffset().getBegin();
			int endOffset = chunk.getCharOffset().getEnd();
			return getKBObjectsWithinChunk(sourceDocId, beginOffset, endOffset, sqlConnection);
		} catch (SQLException e) {
			throw new KBQueryException("ERROR: Could not get KB objects within chunk.", e);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Get KB Objects contained within input source doc and character offsets
	 * 
	 * @param sourceDocumentID
	 * @param beginOffset
	 * @param endOffset
	 * @return
	 * @throws KBQueryException
	 */
	public List<KBPredicateArgument> getKBObjectsWithinSourceDocumentOffsets(
			String sourceDocumentID, int beginOffset, int endOffset) throws KBQueryException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();

			// get source document ID and begin and end offsets from chunk
			return getKBObjectsWithinChunk(sourceDocumentID, beginOffset, endOffset, sqlConnection);
		} catch (SQLException e) {
			throw new KBQueryException("ERROR: Could not get KB objects within chunk.", e);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private List<KBPredicateArgument> getKBObjectsWithinChunk(String sourceDocId, int beginOffset,
			int endOffset, Connection sqlConnection) throws KBQueryException, SQLException {
		PreparedStatement preparedStmt = null;
		java.sql.ResultSet result = null;

		try {
			List<KBPredicateArgument> kbObjects = new ArrayList<KBPredicateArgument>();
			List<String> kbIds = new ArrayList<String>();

			preparedStmt = SqlQueryBuilder.createGetKBIDsByChunkQuery(sourceDocId, beginOffset,
					endOffset, sqlConnection);
			result = preparedStmt.executeQuery();
			while (result.next()) {
				kbIds.add(result.getString("KBId"));
			}

			for (String kbid : kbIds) {
				kbObjects.add(getPredicateArgumentByID(kbid, sqlConnection));
			}

			return kbObjects;
		} catch (Exception ex) {
			throw new KBQueryException("ERROR: Could not get KB objects within chunk.", ex);
		} finally {
			try {
				if (result != null)
					result.close();
			} catch (Exception e) {
			}
			;
			try {
				if (preparedStmt != null)
					preparedStmt.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Execute a Sparql select query.
	 * 
	 * Please be sure to close the ResultSet object when done with it.
	 * 
	 * @return ResultSet
	 */
	public com.hp.hpl.jena.query.ResultSet executeSelectQuery(String query) {
		Query selectQuery = QueryFactory.create(query);
		QueryExecution qexec = sparqlService.getQueryExecution(selectQuery);
		com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
		return resultSet;
	}

	/**
	 * get all properties from triple store associated with given KB URI.
	 */
	private String getLeafTypeFromId(String id) {
		Query query = sparqlQueryBuilder.createGetLeafTypeBySubjectURIQuery(id);
		QueryExecution qexec = null;
		
		try{
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
	
			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
	
				RDFNode type = item.get("type");
				String typeAsString = type.isLiteral() ? type.asLiteral().getString() : type.toString()
						.split("#")[1];
	
				return typeAsString;
	
			}
	
			return null;
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
			

	}

	/**
	 * <p>
	 * get text provenance information from metadata store given its primary key
	 * ID.
	 * </p>
	 * 
	 * @throws SQLException
	 * 
	 * 
	 */
	protected List<KBTextProvenance.InsertionBuilder> getTextProvenancesByOwnerId(String id,
			Connection sqlConnection) throws SQLException {
		List<KBTextProvenance.InsertionBuilder> kbTextProvenances = new ArrayList<KBTextProvenance.InsertionBuilder>();

		PreparedStatement preparedStmt = null;
		java.sql.ResultSet resultSet = null;

		try {
			preparedStmt = SqlQueryBuilder.createTextProvenanceByIdQuery(id, sqlConnection);
			resultSet = preparedStmt.executeQuery();
			while (resultSet.next()) {
				KBTextProvenance.InsertionBuilder builder = KBTextProvenance.builder();
				builder.setConfidence(resultSet.getFloat("TP_confidence"));
				builder.setBeginOffset(resultSet.getInt("Chunk_beginOffset"));
				builder.setEndOffset(resultSet.getInt("Chunk_endOffset"));
				builder.setValue(resultSet.getString("Chunk_value"));
				builder.setSourceAlgorithmName(resultSet.getString("SA_algorithmName"));
				builder.setContributingSiteName(resultSet.getString("SA_contributingSiteName"));
				builder.setCorpusName(resultSet.getString("CORPUS_name"));
				builder.setCorpusID(resultSet.getString("CORPUS_ID"));
				builder.setCorpusURI(resultSet.getString("CORPUS_URI"));
				builder.setCorpusType(resultSet.getString("CORPUS_type"));
				builder.setDocumentPublicationDate(resultSet.getString("SD_publicationDate"));
				builder.setDocumentID(resultSet.getString("SD_ID"));
				builder.setDocumentURI(resultSet.getString("SD_URI"));
				builder.setSourceLanguage(resultSet.getString("SD_sourceLanguage"));
				builder.setKBID(new KBID(resultSet.getString("TP_ID"),
						KBOntologyModel.DATA_INSTANCES_PREFIX));

				kbTextProvenances.add(builder);

			}
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
			}
			;
			try {
				if (preparedStmt != null)
					preparedStmt.close();
			} catch (Exception e) {
			}
			;
		}

		return kbTextProvenances;
	}

	private Map<String, List<KBTextProvenance.InsertionBuilder>> getTextProvenancesByOwnerIds(
			List<String> ids, Connection sqlConnection) throws SQLException {
		if (ids == null || ids.isEmpty())
			return null;

		Map<String, List<KBTextProvenance.InsertionBuilder>> kbTextProvenances = new HashMap<String, List<KBTextProvenance.InsertionBuilder>>();

		PreparedStatement preparedStmt = null;
		java.sql.ResultSet resultSet = null;

		try {
			preparedStmt = SqlQueryBuilder.createTextProvenancesByIdsQuery(ids, sqlConnection);
			resultSet = preparedStmt.executeQuery();
			while (resultSet.next()) {
				KBTextProvenance.InsertionBuilder builder = KBTextProvenance.builder();
				builder.setConfidence(resultSet.getFloat("TP_confidence"));
				builder.setBeginOffset(resultSet.getInt("Chunk_beginOffset"));
				builder.setEndOffset(resultSet.getInt("Chunk_endOffset"));
				builder.setValue(resultSet.getString("Chunk_value"));
				builder.setSourceAlgorithmName(resultSet.getString("SA_algorithmName"));
				builder.setContributingSiteName(resultSet.getString("SA_contributingSiteName"));
				builder.setCorpusID(resultSet.getString("CORPUS_ID"));
				builder.setCorpusName(resultSet.getString("CORPUS_name"));
				builder.setCorpusURI(resultSet.getString("CORPUS_URI"));
				builder.setCorpusType(resultSet.getString("CORPUS_type"));
				builder.setDocumentPublicationDate(resultSet.getString("SD_publicationDate"));
				builder.setDocumentID(resultSet.getString("SD_ID"));
				builder.setDocumentURI(resultSet.getString("SD_URI"));
				builder.setSourceLanguage(resultSet.getString("SD_sourceLanguage"));
				builder.setKBID(new KBID(resultSet.getString("TP_ID"),
						KBOntologyModel.DATA_INSTANCES_PREFIX));

				String kbid = resultSet.getString("TP_KBId");
				List<KBTextProvenance.InsertionBuilder> provenances = kbTextProvenances.get(kbid);
				if (provenances == null) {
					provenances = new ArrayList<KBTextProvenance.InsertionBuilder>();
					kbTextProvenances.put(kbid, provenances);
				}
				provenances.add(builder);
			}
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
			}
			;
			try {
				if (preparedStmt != null)
					preparedStmt.close();
			} catch (Exception e) {
			}
			;
		}

		return kbTextProvenances;
	}

	/**
	 * gets all external KB IDs that this Adept KB ID maps to.
	 * 
	 * @param adeptKbId
	 * @return
	 */
	public List<KBID> getExternalKBIDs(KBID adeptKbId) {
		List<KBID> externalKBIDs = new ArrayList<KBID>();

		Query query = sparqlQueryBuilder.createGetExternalIdsByAdeptId(adeptKbId.getObjectID());
		QueryExecution qexec = null;
		
		try{
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
	
			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				RDFNode externalKBIdNode = item.get("?externalKbElementId");
				String externalKbId = externalKBIdNode.asLiteral().getString();
	
				RDFNode externalKbNameNode = item.get("?externalKbName");
				String externalKbName = externalKbNameNode.asLiteral().getString();
	
				externalKBIDs.add(new KBID(externalKbId, externalKbName));
			}
	
			return externalKBIDs;
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Get Adept KB Number Id by number value. Returns Optional.absent if not
	 * present.
	 * 
	 * @param number
	 * 
	 * @return
	 * @throws KBQueryException
	 */
	public Optional<KBNumber> getNumberByValue(Number number) throws KBQueryException {
		Query query = sparqlQueryBuilder.createGetNumberByNumberValueQuery(number);
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		
		try {
			// / get SQL connection instance
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
			sqlConnection = quickJDBC.getConnection();
			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String numberURI = subjectAsString.split("#")[1];
				KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(number);
				List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
						numberURI, sqlConnection);
				for (KBTextProvenance.InsertionBuilder provenance : provenances) {
					builder.addProvenance(provenance);
				}
				builder.setKBID(new KBID(numberURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(builder.build());
			}
			return Optional.absent();
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for number with value = " + number, ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			try {
				if (qexec != null){
					qexec.close();
				}
			}catch (Exception e){
			};
		}
	}

	/**
	 * Get a KBNumber by its id
	 * 
	 * @param numberId
	 * @return
	 */
	public KBNumber getNumberValueByID(KBID numberId) throws KBQueryException {
		Preconditions.checkNotNull(numberId);
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			String kbUri = numberId.getObjectID();
			Query query = sparqlQueryBuilder.createGetNumberByIDQuery(numberId.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			Number number = null;
			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				Literal value = item.getLiteral(SparqlQueryBuilder.VALUE);
				number = convertToNumber(value);
			}
			if (number == null) {
				throw new KBQueryException("No number found in kb for ID = "
						+ numberId.getObjectID());
			}

			KBNumber.InsertionBuilder numberBuilder = KBNumber.numberInsertionBuilder(number);
			List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
					kbUri, sqlConnection);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				numberBuilder.addProvenance(provenance);
			}

			numberBuilder.setKBID(numberId);
			return numberBuilder.build();
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for number with ID = "
					+ numberId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Get a generic Thing by its KBID.
	 * 
	 * @param genericThingId
	 * @return
	 * @throws KBQueryException
	 */
	public KBGenericThing getGenericThingByID(KBID genericThingId) throws KBQueryException {
		Preconditions.checkNotNull(genericThingId);
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			String kbUri = genericThingId.getObjectID();
			Query query = sparqlQueryBuilder.createGetGenericThingByIDQuery(genericThingId
					.getObjectID());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			String canonicalString = null;
			OntType type = null;

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				canonicalString = item.getLiteral("?canonicalString").getLexicalForm();
				type = new OntType(item.getResource("?type"));
			} else {
				throw new KBQueryException("No generic thing found in kb for ID = "
						+ genericThingId.getObjectID());
			}

			KBGenericThing.InsertionBuilder genericThingBuilder = KBGenericThing
					.genericThingInsertionBuilder(type, canonicalString);
			List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
					kbUri, sqlConnection);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				genericThingBuilder.addProvenance(provenance);
			}

			genericThingBuilder.setKBID(genericThingId);
			return genericThingBuilder.build();
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for generic thing with ID = "
					+ genericThingId.getObjectID(), ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Get a generic Thing by string value and type.
	 * 
	 * Returns Optional.absent if generic thing does not exist in KB.
	 * 
	 * @param genericThingId
	 * @return
	 * @throws KBQueryException
	 */
	public Optional<KBGenericThing> getGenericThingByTypeAndValue(OntType type,
			String canonicalString) throws KBQueryException {
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(canonicalString);
		Connection sqlConnection = null;
		QueryExecution qexec = null;
		try {
			// get SQL connection instance
			sqlConnection = quickJDBC.getConnection();

			Query query = sparqlQueryBuilder.createGetGenericThingByValueAndTypeQuery(type,
					canonicalString);
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			Resource idNode = null;
			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				idNode = item.getResource("?id");
			} else {
				return Optional.absent();
			}

			KBID kbId = new KBID(idNode.getURI().split("#")[1],
					KBOntologyModel.DATA_INSTANCES_PREFIX);

			KBGenericThing.InsertionBuilder genericThingBuilder = KBGenericThing
					.genericThingInsertionBuilder(type, canonicalString);
			List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
					kbId.getObjectID(), sqlConnection);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				genericThingBuilder.addProvenance(provenance);
			}

			genericThingBuilder.setKBID(kbId);
			return Optional.of(genericThingBuilder.build());
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for generic thing with Type = "
					+ type.getType() + " and value \"" + canonicalString + "\"", ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
			try {
				if (qexec != null){
					qexec.close();
				}
			}catch (Exception e){
			};
		}
	}

	/**
	 * @param value
	 * @return
	 */
	private Number convertToNumber(Literal value) {
		return (Number) value.getValue();
	}

	/**
	 * Retrieve the full document text that correspond to documentID and
	 * corpusID.
	 * 
	 * Return Optional.absent if document text is not available.
	 * 
	 * @param documentID
	 * @param corpusID
	 * @return
	 * @throws KBQueryException
	 */
	public Optional<String> getDocumentText(String documentID, String corpusID)
			throws KBQueryException {
		Connection sqlConnection = null;
		PreparedStatement preparedStmt = null;
		java.sql.ResultSet resultSet = null;

		Optional<String> result = null;
		try {
			sqlConnection = quickJDBC.getConnection();
			preparedStmt = sqlConnection.prepareStatement(SqlQueryBuilder.queryDocumentText);
			preparedStmt.setString(1, documentID);
			preparedStmt.setString(2, corpusID);
			resultSet = preparedStmt.executeQuery();
			if (resultSet.next()) {
				result = Optional.of(resultSet.getString(1));
			} else {
				result = Optional.absent();
			}
		} catch (Exception e) {
			throw new KBQueryException("Unable to retrieve DocumentText", e);
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} catch (Exception e) {
			}
			;
			try {
				if (preparedStmt != null)
					preparedStmt.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
		return result;
	}

	private class IntermediateEntity {
		public String id;
		public float entityConfidence;
		public String canonicalMention;
		public float canonicalMentionConfidence;
		public Map<OntType, Float> types = new HashMap<OntType, Float>();
	}

	/**
	 * <p>
	 * API to insert Adept Entity into the KB. The method accepts a KBEntity
	 * InsertionBuilder.
	 * </p>
	 * 
	 * <p>
	 * There is a check to see if the entity that is to be inserted already has
	 * an ID in the Adept-KB, in which case the operation fails and the user
	 * will be asked to reconsider the insert. To enable insert upon
	 * consideration, user will need to remove the Adept KB ID from the list of
	 * external KB IDs.
	 * </p>
	 * 
	 * <p>
	 * The properties of the entity that get inserted into the triple store are
	 * its type, canonical mention identifier (in the SQL metadata DB), and the
	 * canonical mention string value. The mention metadata, including token
	 * offsets, mention type, source document and source algorithm are stored as
	 * part of the metadata database.
	 * </p>
	 * 
	 * @param entityInsertionBuilder
	 *            the Adept entity object to be inserted.
	 */
	protected void insertEntity(KBEntity.InsertionBuilder entityInsertionBuilder)
			throws KBUpdateException {
		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);

			String entityId = UUID.randomUUID().toString();
			UpdateRequest entityInsertRequest = UpdateFactory.create();

			// check entity insert preconditions
			// check that there is atleast one mention
			if (entityInsertionBuilder.getCanonicalMention() == null
					&& (entityInsertionBuilder.getProvenances() == null || entityInsertionBuilder
							.getProvenances().size() == 0)) {
				throw new KBUpdateException(
						"Entity should have at least one mention. Cannot complete insertion for entity");
			}

			// insert canonical mention
			if (entityInsertionBuilder.getCanonicalMention() != null) {
				insertTextProvenance(entityId, entityInsertionBuilder.getCanonicalMention(),
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			// insert other entity mentions
			for (KBProvenance.InsertionBuilder mention : entityInsertionBuilder.getProvenances()) {
				KBTextProvenance.InsertionBuilder textMention = (KBTextProvenance.InsertionBuilder) mention;
				// ensure we aren't reinserting the canonical mention
				if (!textMention.getChunkId().equals(
						entityInsertionBuilder.getCanonicalMention().getChunkId())
						|| textMention.getConfidence() != entityInsertionBuilder
								.getCanonicalMention().getConfidence()) {
					insertTextProvenance(entityId, textMention, chunkInsertBatchStatement,
							textProvenanceInsertBatchStatement, sqlConnection);
				} else {
					textMention.kbid = entityInsertionBuilder.getCanonicalMention().kbid;
				}
			}

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();

			// insert external KB ID mapping
			if (entityInsertionBuilder.getExternalKBIds() != null) {
				UpdateRequest externalKbIdsUpdateRequest = addMapToExternalKBIds(new KBID(entityId,
						KBOntologyModel.DATA_INSTANCES_PREFIX),
						entityInsertionBuilder.getExternalKBIds());
				for (Update update : externalKbIdsUpdateRequest.getOperations()) {
					entityInsertRequest.add(update);
				}
			}
			// insert entity triples
			UpdateRequest entityInsertQueries = sparqlQueryBuilder.createEntityInsertQueries(
					entityInsertionBuilder, entityId);
			for (Update update : entityInsertQueries.getOperations()) {
				entityInsertRequest.add(update);
			}
			UpdateProcessor upp = sparqlService.getUpdateProcessor(entityInsertRequest);
			upp.execute();

			sqlConnection.commit();

			entityInsertionBuilder
					.setKBID(new KBID(entityId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert entity.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * <p>
	 * API to insert Adept TimexValue and XSD date values into the KB.
	 * 
	 * <p>
	 * There is a check to see if the TimexValue that is to be inserted already
	 * has an ID in the Adept-KB. If so, that ID will be REUSED. Note that this
	 * differs from the behavior of insertEntity, since Date objects are
	 * ultimately canonical values. Thus this method can be used to add
	 * additional TimePhrase mentions to an existing Date.
	 * </p>
	 * 
	 * <p>
	 * The KBDate object stores its canonical value. The mention metadata,
	 * including token offsets, mention type, source document and source
	 * algorithm are stored as part of the metadata database.
	 * </p>
	 * 
	 * @param dateInsertionBuilder
	 *            the Adept entity object to be inserted.
	 */
	protected void insertDateValue(KBDate.InsertionBuilder dateInsertionBuilder)
			throws KBUpdateException {

		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);

			String dateId = UUID.randomUUID().toString();

			Optional<KBDate> existingDate = getDateByTimex2Value(dateInsertionBuilder
					.getTimexDate());
			List<KBTextProvenance.InsertionBuilder> existingProvenances = new ArrayList<KBTextProvenance.InsertionBuilder>();
			if (existingDate.isPresent()) {
				dateId = existingDate.get().getKBID().getObjectID();
				existingProvenances = getTextProvenancesByOwnerId(dateId, sqlConnection);
			}

			// insert date mentions
			for (KBProvenance.InsertionBuilder mention : dateInsertionBuilder.getProvenances()) {
				insertTextProvenance(dateId, (KBTextProvenance.InsertionBuilder) mention,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			dateInsertionBuilder.addProvenances(existingProvenances);

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();

			if (!existingDate.isPresent()) {
				// insert date triples
				UpdateRequest xsdDateInsertRequest = sparqlQueryBuilder
						.createDateValueInsertQueries(dateInsertionBuilder.getTimexDate(),
								dateInsertionBuilder.getXSDDate(), dateId);
				UpdateProcessor upp = sparqlService.getUpdateProcessor(xsdDateInsertRequest);
				upp.execute();
			}

			sqlConnection.commit();

			dateInsertionBuilder.setKBID(new KBID(dateId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Unable to insert timex value", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Insert Chunk into the SQL database. This method is responsible for
	 * serializing the tokenstream, saving it onto the metadata server, and
	 * storing a pointer to this serialized file as part of the metadata record.
	 * 
	 */
	private void insertChunk(KBTextProvenance.InsertionBuilder provenance,
			PreparedStatement chunkInsertBatchStatement, Connection sqlConnection)
			throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException {
		if (provenance.getDocumentID() != null)
			insertSourceDocument(provenance, sqlConnection);

		// insert chunk into SQL DB
		if (!insertedChunks.containsKey(provenance.getChunkId())) {
			if (!quickJDBC.recordExists(SqlQueryBuilder.doesChunkExistQuery(
					provenance.getChunkId(), sqlConnection))) {
				SqlQueryBuilder.addTextChunkInsertQueryToBatch(provenance.getChunkId(),
						provenance.getValue(), provenance.getBeginOffset(),
						provenance.getEndOffset(),
						(provenance.getDocumentID() != null ? provenance.getDocumentID() : null),
						chunkInsertBatchStatement);
			}
			insertedChunks.put(provenance.getChunkId(), true);
		}
	}

	/**
	 * Insert source document into the SQL DB
	 * 
	 */
	private void insertSourceDocument(KBTextProvenance.InsertionBuilder provenance,
			Connection sqlConnection) throws SQLException {
		if (provenance.getCorpusName() != null) {
			insertCorpus(provenance.getCorpusID(), provenance.getCorpusType(),
					provenance.getCorpusName(), provenance.getCorpusURI(), sqlConnection);
		}

		if (!insertedSourceDocuments.containsKey(provenance.getDocumentID())) {
			if (!quickJDBC.recordExists(SqlQueryBuilder.doesSourceDocExistQuery(
					provenance.getDocumentID(), sqlConnection))) {
				quickJDBC.executeSqlUpdate(SqlQueryBuilder.createSourceDocumentInsertQuery(
						provenance.getDocumentID(),
						provenance.getDocumentURI(),
						provenance.getSourceLanguage(),
						provenance.getCorpusID(),
						provenance.getDocumentPublicationDate() == null ? null : java.sql.Date
								.valueOf(provenance.getDocumentPublicationDate()), sqlConnection));
			}
			insertedSourceDocuments.put(provenance.getDocumentID(), true);
		}
	}

	/**
	 * 
	 * Insert corpus in the SQL DB.
	 */
	private void insertCorpus(String corpusId, String corpusType, String corpusName,
			String corpusUri, Connection sqlConnection) throws SQLException {
		if (!insertedCorpora.containsKey(corpusId)) {
			if (!quickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery(corpusId,
					sqlConnection))) {
				quickJDBC.executeSqlUpdate(SqlQueryBuilder.createCorpusInsertQuery(corpusId,
						corpusType, corpusName, corpusUri, sqlConnection));
			}
			insertedCorpora.put(corpusId, true);
		}
	}

	/**
	 * Insert source algorithm in the SQL DB
	 * 
	 */
	private void insertSourceAlgorithm(String sourceAlgorithmName, String contributingSiteName,
			Connection sqlConnection) throws SQLException {

		if (!insertedSourceAlgorithms.containsKey(sourceAlgorithmName)) {
			if (!quickJDBC.recordExists(SqlQueryBuilder.doesSourceAlgorithmExistQuery(
					sourceAlgorithmName, sqlConnection))) {
				quickJDBC.executeSqlUpdate(SqlQueryBuilder.createSourceAlgorithmInsertQuery(
						sourceAlgorithmName, contributingSiteName, sqlConnection));
			}
			insertedSourceAlgorithms.put(sourceAlgorithmName, true);
		}
	}

	protected void insertEvent(KBEvent.InsertionBuilder insertionBuilder) throws KBUpdateException {
		List<Update> realisUpdates = new ArrayList<Update>();

		String eventId = UUID.randomUUID().toString();
		for (OntType ontType : insertionBuilder.getRealisTypes().keySet()) {
			String realisType = ontType.getType();

			String typeStmtId = UUID.randomUUID().toString();
			realisUpdates.addAll(sparqlQueryBuilder.createInsertEventRealisType(eventId,
					typeStmtId, realisType, insertionBuilder.getRealisTypes().get(ontType))
					.getOperations());
		}
		insertRelation(insertionBuilder, eventId, realisUpdates);
	}

	protected <BuilderType extends AbstractInsertionBuilder<BuilderType, RelationType>, RelationType extends KBRelation> void insertRelation(
			AbstractInsertionBuilder<BuilderType, RelationType> insertionBuilder)
			throws KBUpdateException {
		String relationId = UUID.randomUUID().toString();
		insertRelation(insertionBuilder, relationId, Collections.<Update> emptyList());
	}

	/**
	 * <p>
	 * Insert ADEPT DocumentRelation into KB. The method accepts an Adept
	 * DocumentRelation object, which can contain references to multiple
	 * provenances (instances of the Adept RelationMention object). Each of
	 * these provenances has a pointer to a text chunk that serves as its
	 * justification, along with information about the argument chunks of the
	 * relation. An argument chunk must necessarily align with an entity
	 * mention. In the future versions of the KB API, once support for date and
	 * number arguments is added, the argument chunks will also be allowed to
	 * align with date and number mentions. The entity mention represented by
	 * any argument chunk must link to a KB entity that has already been
	 * inserted.
	 * </p>
	 * 
	 * <p>
	 * In addition to the DocumentRelation instance, the method also expects
	 * information about the mapping from document entities (that the relation
	 * argument mentions resolve to) to Adept KB entities, and a map with
	 * associated confidences with information on any external KB IDs that this
	 * document relation has already been resolved to.
	 * </p>
	 * 
	 * <p>
	 * There is a check to see if the relation that is to be inserted already
	 * has an ID in the Adept KB, in which case the operation fails and the user
	 * will be asked to reconsider the insert. To enable insert upon
	 * consideration, user will need to remove the Adept KB ID from the list of
	 * external KB IDs.
	 * </p>
	 * 
	 * 
	 * The following preconditions are also checked. The method fails if even
	 * one of them is not satisfied.<br>
	 * (1) The document relation has at least one relation mention provenance
	 * entry.<br>
	 * (2) All argument mentions in the provenances map to a valid argument
	 * entity in the document with the highest confidence. That is, the
	 * getBestEntityId() call on the entity mention returns the argument entity.<br>
	 * (3) The roles of the argument mentions agree with the role of the
	 * argument entities that they resolve to.<br>
	 * (4) All argument entities and mentions already exist in the ADEPT KB. An
	 * entity mention will be considered identical to an existing entity mention
	 * in the KBmetadata store, if and only if all of the following fields
	 * match: mention type, mention token offsets, string value, source
	 * document, and the KB entity that the mention resolves to. The KB entity
	 * on the argument mention is obtained through
	 * mention.getBestEntity().getBestKBEntity() -- i.e. 1-best resolution.<br>
	 * <br>
	 * (5) TODO: Check that the entity type of the argument's entity is as
	 * expected in ontology.<br>
	 * (6) TODO: Check that all slots of the relation are filled as is expected
	 * in ontology.<br>
	 * 
	 * <p>
	 * The properties of the relation that are inserted into the triple store
	 * are the relation type, the KB level arguments, and argument confidences.
	 * The argument confidence value is the one associated with the
	 * DocumentRelation.Filler objects. The relation justifications and argument
	 * justifications are stored in the metadata store.
	 * </p>
	 * 
	 * @param insertionBuilder
	 *            the relation to be inserted *
	 */
	protected <BuilderType extends AbstractInsertionBuilder<BuilderType, RelationType>, RelationType extends KBRelation> void insertRelation(
			AbstractInsertionBuilder<BuilderType, RelationType> insertionBuilder,
			String relationId, List<Update> additionalUpdates) throws KBUpdateException {
		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;

		String existingAdeptId = null;
		for (KBID kbRelation : insertionBuilder.getExternalKBIds()) {
			if (kbRelation.getKBNamespace().equals(KBOntologyModel.DATA_INSTANCES_PREFIX)) {
				throw new KBUpdateException(
						"This relation already seems to have an entry in the Adept KB with ID = "
								+ existingAdeptId
								+ ". Please use the updateRelation() method to update this existing relation.");
			}
		}

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);

			if (insertionBuilder instanceof KBMentalState.InsertionBuilder) {
				checkMentalStatePreconditions((KBMentalState.InsertionBuilder<?>) insertionBuilder);
			} else {
				checkRelationPreconditions(insertionBuilder);
			}

			UpdateRequest relationInsertRequest = UpdateFactory.create();
			for (KBRelationArgument.InsertionBuilder argument : insertionBuilder.getArguments()) {
				String argumentStmtId = UUID.randomUUID().toString();

				UpdateRequest argumentInsertRequest = sparqlQueryBuilder
						.createArgumentInsertQueries(argument.getRole(), argument.getConfidence(),
								argument.getTarget().getKBID().getObjectID(), relationId,
								insertionBuilder.getType().getType(), argumentStmtId);
				for (Update update : argumentInsertRequest.getOperations()) {
					relationInsertRequest.add(update);
				}

				for (KBProvenance.InsertionBuilder provenance : argument.getProvenances()) {
					insertTextProvenance(argumentStmtId,
							(KBTextProvenance.InsertionBuilder) provenance,
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
							sqlConnection);
				}

				argument.setKBID(new KBID(argumentStmtId, KBOntologyModel.DATA_INSTANCES_PREFIX));
			}

			for (KBProvenance.InsertionBuilder provenance : insertionBuilder.getProvenances()) {
				insertTextProvenance(relationId, (KBTextProvenance.InsertionBuilder) provenance,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();

			// insert external KB ID mapping
			if (insertionBuilder.getExternalKBIds() != null) {
				UpdateRequest externalKbIdsUpdateRequest = addMapToExternalKBIds(new KBID(
						relationId, KBOntologyModel.DATA_INSTANCES_PREFIX),
						insertionBuilder.getExternalKBIds());
				for (Update update : externalKbIdsUpdateRequest.getOperations()) {
					relationInsertRequest.add(update);
				}
			}

			// insert relation triple
			UpdateRequest relationInsertRequestQueries = sparqlQueryBuilder
					.createRelationInsertQueries(insertionBuilder.getType(),
							insertionBuilder.getConfidence(), relationId);
			for (Update update : relationInsertRequestQueries.getOperations()) {
				relationInsertRequest.add(update);
			}
			for (Update update : additionalUpdates) {
				relationInsertRequest.add(update);
			}
			UpdateProcessor upp = sparqlService.getUpdateProcessor(relationInsertRequest);
			upp.execute();

			sqlConnection.commit();

			insertionBuilder.setKBID(new KBID(relationId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert relation.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private void insertTextProvenance(String sourceUri,
			KBTextProvenance.InsertionBuilder provenance,
			PreparedStatement chunkInsertBatchStatement,
			PreparedStatement textProvenanceInsertBatchStatement, Connection sqlConnection)
			throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException {
		// insert chunk
		insertChunk(provenance, chunkInsertBatchStatement, sqlConnection);

		// insert source algorithm
		if (provenance.getSourceAlgorithmName() != null) {
			insertSourceAlgorithm(provenance.getSourceAlgorithmName(),
					provenance.getContributingSiteName(), sqlConnection);
		}

		String provenanceId = UUID.randomUUID().toString();
		SqlQueryBuilder.addTextProvenanceInsertQueryToBatch(provenanceId, provenance.getChunkId(),
				provenance.getSourceAlgorithmName() != null ? provenance.getSourceAlgorithmName()
						: null, sourceUri, provenance.getConfidence(),
				textProvenanceInsertBatchStatement);
		provenance.setKBID(new KBID(provenanceId, KBOntologyModel.DATA_INSTANCES_PREFIX));
	}

	private void deleteTextProvenance(KBID provenanceKbId,
			PreparedStatement textProvenanceDeleteBatchStatement) throws SQLException {
		SqlQueryBuilder.addTextProvenanceDeleteQueryToBatch(provenanceKbId.getObjectID(),
				textProvenanceDeleteBatchStatement);
	}

	/**
	 * <p>
	 * Modify existing KB entity. The updates propagate to the metadata SQL DB,
	 * or in other words the update may also be to a mention or other metadata
	 * associated with the Entity.
	 * </p>
	 * 
	 * <p>
	 * If the type associated with the updated entity is different from the
	 * original type(s), it gets appended into the triple store. The canonical
	 * mention ID and canonical string value get overwritten by the update.
	 * Entity mentions and external KB IDs get appended in the metadata DB.
	 * Confidences get overwritten.
	 * </p>
	 * 
	 * @param entityUpdateBuilder
	 */
	protected void updateEntity(KBEntity.UpdateBuilder entityUpdateBuilder)
			throws KBUpdateException {

		String kbUri = entityUpdateBuilder.getKBID().getObjectID();

		// get SQL connection
		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;
		PreparedStatement textProvenanceDeleteBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);

			// check if KB object to be updated exists in KB.
			if (!doesKBObjectExist(kbUri)) {
				throw new KBUpdateException("Entity to be updated does not exist in the KB.");
			}

			// insert new canonicalmention if it exists
			if (entityUpdateBuilder.getNewCanonicalMention() != null) {
				insertTextProvenance(kbUri, entityUpdateBuilder.getNewCanonicalMention(),
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			for (KBProvenance.InsertionBuilder mention : entityUpdateBuilder.getNewProvenances()) {
				KBTextProvenance.InsertionBuilder textMention = (KBTextProvenance.InsertionBuilder) mention;
				// ensure we aren't reinserting the canonical mention
				if (entityUpdateBuilder.getNewCanonicalMention() == null
						|| !textMention.getChunkId().equals(
								entityUpdateBuilder.getNewCanonicalMention().getChunkId())
						|| textMention.getConfidence() != entityUpdateBuilder
								.getNewCanonicalMention().getConfidence()) {
					insertTextProvenance(kbUri, (KBTextProvenance.InsertionBuilder) mention,
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
							sqlConnection);
				} else {
					mention.kbid = entityUpdateBuilder.getNewCanonicalMention().kbid;
				}
			}

			for (KBProvenance mentionToRemove : entityUpdateBuilder.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			// execute updates in triple store
            UpdateRequest entityUpdateRequest = UpdateFactory.create();
                                
            // insert external KB ID mapping
			if (entityUpdateBuilder.getNewExternalKBIDs() != null) {
				UpdateRequest externalKbIdsUpdateRequest = addMapToExternalKBIds(new KBID(kbUri,
						KBOntologyModel.DATA_INSTANCES_PREFIX),
						entityUpdateBuilder.getNewExternalKBIDs());
				for (Update update : externalKbIdsUpdateRequest.getOperations()) {
					entityUpdateRequest.add(update);
				}
			}
                        
            // delete external KB IDs
            if (entityUpdateBuilder.getExternalKBIDsToRemove() != null) {
                UpdateRequest externalKbIdsDeleteRequest = deleteMapToExternalKBIds(new KBID(kbUri,
                        KBOntologyModel.DATA_INSTANCES_PREFIX),
                        entityUpdateBuilder.getExternalKBIDsToRemove());
                for (Update update : externalKbIdsDeleteRequest.getOperations()) {
                    entityUpdateRequest.add(update);
                }
            }
                        
			UpdateRequest entityUpdateQueries = sparqlQueryBuilder.createEntityUpdateQueries(
					entityUpdateBuilder, kbUri);
			if (entityUpdateQueries != null) {
				for (Update update : entityUpdateQueries.getOperations()) {
                                        entityUpdateRequest.add(update);
                                }
			}
                        
            UpdateProcessor upp = sparqlService.getUpdateProcessor(entityUpdateRequest);
            upp.execute();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Entity update failed.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceDeleteBatchStatement != null)
					textProvenanceDeleteBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * <p>
	 * Modify existing KB event. The updates propagate to the metadata SQL DB,
	 * or in other words the update may also be to a mention or other metadata
	 * associated with the relation.
	 * </p>
	 * 
	 * 
	 * @param updateBuilder
	 *            the Adept object to be updated
	 */
	protected void updateEvent(KBEvent.UpdateBuilder updateBuilder) throws KBUpdateException {
		List<Update> realisUpdates = new ArrayList<Update>();
		realisUpdates.addAll(sparqlQueryBuilder.createRemoveEventRealisTypes(
				updateBuilder.getKBID()).getOperations());

		for (OntType ontType : updateBuilder.getUpdatedRealisTypes().keySet()) {
			String realisType = ontType.getType();

			String typeStmtId = UUID.randomUUID().toString();
			realisUpdates.addAll(sparqlQueryBuilder.createInsertEventRealisType(
					updateBuilder.getKBID().getObjectID(), typeStmtId, realisType,
					updateBuilder.getUpdatedRealisTypes().get(ontType)).getOperations());
		}
		updateRelation(updateBuilder, realisUpdates);
	}

	/**
	 * <p>
	 * Modify existing KB relation. The updates propagate to the metadata SQL
	 * DB, or in other words the update may also be to a mention or other
	 * metadata associated with the relation.
	 * </p>
	 * 
	 * <p>
	 * The method does not allow modification of relation type or argument
	 * entities. The only changes allowed are appending new relation mentions,
	 * argument mentions (for existing arguments) and external KB IDs, and
	 * overwriting the relation and argument confidences.
	 * </p>
	 * 
	 * <p>
	 * Similar preconditions are checked as in the case of relation insertion.
	 * </p>
	 * 
	 * @param relationUpdateBuilder
	 *            the Adept object to be updated
	 */
	protected <BuilderType extends KBRelation.AbstractUpdateBuilder<BuilderType, RelationType>, RelationType extends KBRelation> void updateRelation(
			KBRelation.AbstractUpdateBuilder<BuilderType, RelationType> relationUpdateBuilder)
			throws KBUpdateException {

		updateRelation(relationUpdateBuilder, Collections.<Update> emptyList());
	}

	/**
	 * <p>
	 * Modify existing KB relation. The updates propagate to the metadata SQL
	 * DB, or in other words the update may also be to a mention or other
	 * metadata associated with the relation.
	 * </p>
	 * 
	 * <p>
	 * The method does not allow modification of relation type or argument
	 * entities. The only changes allowed are appending new relation mentions,
	 * argument mentions (for existing arguments) and external KB IDs, and
	 * overwriting the relation and argument confidences.
	 * </p>
	 * 
	 * <p>
	 * Similar preconditions are checked as in the case of relation insertion.
	 * </p>
	 * 
	 * @param relationUpdateBuilder
	 *            the Adept object to be updated
	 */
	protected <BuilderType extends KBRelation.AbstractUpdateBuilder<BuilderType, RelationType>, RelationType extends KBRelation> void updateRelation(
			KBRelation.AbstractUpdateBuilder<BuilderType, RelationType> relationUpdateBuilder,
			List<Update> additionalUpdates) throws KBUpdateException {
		String kbUri = relationUpdateBuilder.getKBID().getObjectID();

		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;
		PreparedStatement textProvenanceDeleteBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);

			UpdateRequest relationUpdateRequest = UpdateFactory.create();

			// There should be at least one provenance entry
			if (relationUpdateBuilder.getUpdatedProvenanceCount() < 1) {
				throw new KBUpdateException("Relation should have at least one provenance entry.");
			}

			// check if KB object to be updated exists in KB.
			if (!doesKBObjectExist(kbUri)) {
				throw new KBUpdateException("Relation to be updated does not exist in the KB.");
			}

			for (KBRelationArgument.InsertionBuilder argument : relationUpdateBuilder
					.getNewArguments()) {
				String argumentStmtId = UUID.randomUUID().toString();

				UpdateRequest argumentInsertRequest = sparqlQueryBuilder
						.createArgumentInsertQueries(argument.getRole(), argument.getConfidence(),
								argument.getTarget().getKBID().getObjectID(), relationUpdateBuilder
										.getKBID().getObjectID(), relationUpdateBuilder.getType()
										.getType(), argumentStmtId);
				for (Update update : argumentInsertRequest.getOperations()) {
					relationUpdateRequest.add(update);
				}

				for (KBProvenance.InsertionBuilder provenance : argument.getProvenances()) {
					insertTextProvenance(argumentStmtId,
							(KBTextProvenance.InsertionBuilder) provenance,
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
							sqlConnection);
				}

				argument.setKBID(new KBID(argumentStmtId, KBOntologyModel.DATA_INSTANCES_PREFIX));
			}

			for (KBProvenance.InsertionBuilder mention : relationUpdateBuilder.getNewProvenances()) {
				insertTextProvenance(kbUri, (KBTextProvenance.InsertionBuilder) mention,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			for (KBProvenance mentionToRemove : relationUpdateBuilder.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

			if (relationUpdateBuilder.getNewConfidence() != null) {
				UpdateRequest confidenceUpdateRequest = sparqlQueryBuilder
						.createRelationConfidenceUpdateQuery(kbUri,
								relationUpdateBuilder.getNewConfidence());
				for (Update update : confidenceUpdateRequest.getOperations()) {
					relationUpdateRequest.add(update);
				}
			}
			for (Update update : additionalUpdates) {
				relationUpdateRequest.add(update);
			}
                        
            // insert external KB ID mapping
			if (relationUpdateBuilder.getNewExternalKBIDs() != null) {
				UpdateRequest externalKbIdsUpdateRequest = addMapToExternalKBIds(new KBID(kbUri,
						KBOntologyModel.DATA_INSTANCES_PREFIX),
						relationUpdateBuilder.getNewExternalKBIDs());
				for (Update update : externalKbIdsUpdateRequest.getOperations()) {
					relationUpdateRequest.add(update);
				}
			}
                        
            // delete external KB IDs
            if (relationUpdateBuilder.getExternalKBIDsToRemove() != null) {
                UpdateRequest externalKbIdsDeleteRequest = deleteMapToExternalKBIds(new KBID(kbUri,
                        KBOntologyModel.DATA_INSTANCES_PREFIX),
                        relationUpdateBuilder.getExternalKBIDsToRemove());
                for (Update update : externalKbIdsDeleteRequest.getOperations()) {
                    relationUpdateRequest.add(update);
                }
            }

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			UpdateProcessor upp = sparqlService.getUpdateProcessor(relationUpdateRequest);
			upp.execute();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Relation update failed.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceDeleteBatchStatement != null)
					textProvenanceDeleteBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	protected void updateRelationArgument(
			KBRelationArgument.UpdateBuilder relationArgumentUpdateBuilder)
			throws KBUpdateException {
		String kbUri = relationArgumentUpdateBuilder.getKBID().getObjectID();

		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;
		PreparedStatement textProvenanceDeleteBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);

			UpdateRequest relationArgumentUpdateRequest = UpdateFactory.create();

			// check if KB object to be updated exists in KB.
			if (!doesKBObjectExist(kbUri)) {
				throw new KBUpdateException("Relation to be updated does not exist in the KB.");
			}

			for (KBProvenance.InsertionBuilder mention : relationArgumentUpdateBuilder
					.getNewProvenances()) {
				insertTextProvenance(kbUri, (KBTextProvenance.InsertionBuilder) mention,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			for (KBProvenance mentionToRemove : relationArgumentUpdateBuilder
					.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

			if (relationArgumentUpdateBuilder.getNewConfidence() != null) {
				UpdateRequest confidenceUpdateRequest = sparqlQueryBuilder
						.createRelationConfidenceUpdateQuery(kbUri,
								relationArgumentUpdateBuilder.getNewConfidence());
				for (Update update : confidenceUpdateRequest.getOperations()) {
					relationArgumentUpdateRequest.add(update);
				}
			}

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			UpdateProcessor upp = sparqlService.getUpdateProcessor(relationArgumentUpdateRequest);
			upp.execute();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Relation argument update failed.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceDeleteBatchStatement != null)
					textProvenanceDeleteBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	protected void updateKBPredicateArgumentProvenances(
			KBPredicateArgument.UpdateBuilder<?, ?> updateBuilder) throws KBUpdateException {
		String kbUri = updateBuilder.getKBID().getObjectID();

		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;
		PreparedStatement textProvenanceDeleteBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);

			for (KBProvenance.InsertionBuilder mention : updateBuilder.getNewProvenances()) {
				insertTextProvenance(kbUri, (KBTextProvenance.InsertionBuilder) mention,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			for (KBProvenance mentionToRemove : updateBuilder.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("KBPredicateArgument update failed.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceDeleteBatchStatement != null)
					textProvenanceDeleteBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Delete a first class KB object and all associated metadata.
	 * 
	 * @return boolean value indicating success or failure
	 */
	public boolean deleteKBObject(KBID kbId) throws KBUpdateException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			return deleteKBObject(kbId, sqlConnection);
		} catch (SQLException ex) {
			throw new KBUpdateException(
					"KB object deletion failed. Unable to get connection to SQL DB", ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private boolean deleteKBObject(KBID kbId, Connection sqlConnection) throws KBUpdateException {

		String kbUri = kbId.getObjectID();

		try {
			// Else, delete all mentions belonging to entity in the metadata DB
			deleteTextProvenancesForKBObject(kbUri, sqlConnection);

			// delete external KB ID maps
			deleteAllExternalKBIdMapsForElement(kbUri);

			deleteTriplesInTripleStore(kbUri);

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("KB object deletion failed.", e);
		}

		return true;
	}

	/**
	 * When KB objects are removed, only TextProvenance and Chunk data
	 * associated with those objects are removed. Calling this method cleans up
	 * any orphaned Source Documents, Source Algorims, and Corpora in the
	 * metadata db.
	 * 
	 * @throws KBUpdateException
	 */
	public void removeOrphanMetaData() throws KBUpdateException {
		Connection sqlConnection = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			removeOrphanMetaData(sqlConnection);
			sqlConnection.commit();
		} catch (SQLException ex) {
			throw new KBUpdateException("Removing orphaned metadata failed.", ex);
		} finally {
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	private void removeOrphanMetaData(Connection sqlConnection) throws SQLException {
		// Delete any SourceAlgorithm orphans
		PreparedStatement sourceAlgPreparedStmt = SqlQueryBuilder
				.createDeleteOrphanSourceAlgorithmsQuery(sqlConnection);
		quickJDBC.executeSqlUpdate(sourceAlgPreparedStmt);

		// Delete any SourceDocument orphans
		PreparedStatement sourceDocPreparedStmt = SqlQueryBuilder
				.createDeleteOrphanSourceDocumentsQuery(sqlConnection);
		quickJDBC.executeSqlUpdate(sourceDocPreparedStmt);

		// Delete any Corpus orphans
		PreparedStatement corpusPreparedStmt = SqlQueryBuilder
				.createDeleteOrphanCorpusQuery(sqlConnection);
		quickJDBC.executeSqlUpdate(corpusPreparedStmt);
	}

	/**
	 * deletes only the triples. Does not touch metadata.
	 */
	private void deleteTriplesInTripleStore(String kbUri) {
		// get UpdateRequest that deletes triples that have input URI in subject
		// or object, also deletes coresponding reified statements.
		UpdateRequest deleteRequest = sparqlQueryBuilder.createDeleteQueries(kbUri);
		UpdateProcessor upp = sparqlService.getUpdateProcessor(deleteRequest);
		upp.execute();
	}

	/**
	 * Delete entity mentions corresponding to a specific KB entity from the SQL
	 * DB
	 */
	private void deleteTextProvenancesForKBObject(String objectUri, Connection sqlConnection)
			throws SQLException {
		PreparedStatement preparedStmt = SqlQueryBuilder.createTextProvenanceDeleteQuery(objectUri,
				sqlConnection);
		quickJDBC.executeSqlUpdate(preparedStmt);
	}

	/** delete external KB IDs belonging to a given Adept KB ID. */
	private void deleteAllExternalKBIdMapsForElement(String kbUri) {
		UpdateRequest deleteRequest = sparqlQueryBuilder
				.createDeleteAllExternalKbIdsForAdeptId(kbUri);
		UpdateProcessor upp = sparqlService.getUpdateProcessor(deleteRequest);
		upp.execute();
	}


	/**
	 * Check if a KB object exists in the triple store
	 * 
	 */
	private boolean doesKBObjectExist(String kbUri) {
		Query query = QueryFactory.create(sparqlQueryBuilder.createDoesKBObjectExistQuery(kbUri));
		QueryExecution qexec = null;
		
		try{
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet results = qexec.execSelect();
			if (results.hasNext())
				return true;
			else
				return false;
		}finally{
			if (qexec != null){
				try{
					qexec.close();
				}catch(Exception e){
					//Do nothing
				}
			}
		}
	}

	/**
	 * Check relation preconditions on insert or update. The following are
	 * checked:<br>
	 * (1) The document relation has at least one relation mention provenance
	 * entry.<br>
	 * (2) Check that the entity type of the argument's entity is as expected in
	 * ontology.<br>
	 * (3) Check that all slots of the relation are filled as is expected in
	 * ontology.<br>
	 */
	private <BuilderType extends KBRelation.AbstractInsertionBuilder<BuilderType, RelationType>, RelationType extends KBRelation> void checkRelationPreconditions(
			KBRelation.AbstractInsertionBuilder<BuilderType, RelationType> relationInsertionBuilder)
			throws SQLException, KBUpdateException {
		// check preconditions:
		// 1. There should be at least one provenance entry
		if (relationInsertionBuilder.getProvenances() == null
				|| relationInsertionBuilder.getProvenances().isEmpty()) {
			if (!relationInsertionBuilder.getType().equals(KBTemporalSpan.temporalSpanType)) {
				throw new KBUpdateException("Relation should have at least one provenance entry.");
			}
		}

		// 2. Check that the entity type of the argument's entity is as expected
		// in ontology
		// 3. Check that all slots of a relation are filled, as expected by the
		// ontology
		String relationType = relationInsertionBuilder.getType().getType();
		if (!kbOntologyModel.getLeafRelationTypes().contains(relationType)
				&& !kbOntologyModel.getLeafEventTypes().contains(relationType)
				&& !relationType.equals("Subsidiary")
                && !relationType.equals("FamilyRelationship")
                && !relationType.equals("MemberOriginReligionEthnicity")) {
			throw new KBUpdateException("Relation type " + relationType + " not found in ontology.");
		}

		Map<String, String> expectedArguments = new HashMap<String, String>(kbOntologyModel
				.getRelationArgumentTypes().get(relationType));
		Map<String, Integer> expectedArgumentOccurences = new HashMap<String, Integer>();
		for (String expectedArgument : expectedArguments.keySet()) {
			if (expectedArgumentOccurences.containsKey(expectedArgument)) {
				expectedArgumentOccurences.put(expectedArgument,
						expectedArgumentOccurences.get(expectedArgument) + 1);
			}
			expectedArgumentOccurences.put(expectedArgument, 1);
		}

		for (KBRelationArgument.InsertionBuilder argument : relationInsertionBuilder.getArguments()) {
			String argumentType = argument.getRole().getType();

			if (!expectedArguments.containsKey(argumentType)) {
				List<String> actualRelationArgumentTypes = new ArrayList<String>();
				for (KBRelationArgument.InsertionBuilder actualArgument : relationInsertionBuilder
						.getArguments()) {
					actualRelationArgumentTypes.add(actualArgument.getRole().getType());
				}
				throw new KBUpdateException("Invalid arguments for relation "
						+ relationType
						+ ". Expected: "
						+ kbOntologyModel.getRelationArgumentTypes().get(relationType).keySet()
								.toString() + " Actual: " + actualRelationArgumentTypes.toString()
						+ ".");
			}

			if (argument.getTarget() instanceof KBEntity) {
				List<String> possibleEntityTypes = kbOntologyModel.getEntityTypeSubclasses().get(
						expectedArguments.get(argumentType));
				KBEntity entity = (KBEntity) argument.getTarget();
				boolean doesCorrectEntityTypeExist = false;
				String entityTypeList = "";

				if (possibleEntityTypes != null) {
					for (OntType type : entity.getTypes().keySet()) {
						String entityType = type.getType();
						entityTypeList += entityType + ", ";
						if (possibleEntityTypes.contains(entityType)) {
							doesCorrectEntityTypeExist = true;
							break;
						}
					}
					if (!doesCorrectEntityTypeExist) {
						throw new KBUpdateException("Argument type " + argumentType
								+ "'s entity is incorrect type (Expected one of "
								+ possibleEntityTypes.toString() + " Actual: "
								+ entityTypeList.substring(0, entityTypeList.lastIndexOf(",")));
					}
				}
			}

			expectedArgumentOccurences.put(argumentType,
					expectedArgumentOccurences.get(argumentType) - 1);
		}

		// Only check for presence of all arguments if relation is not an event.
		if (!kbOntologyModel.getLeafEventTypes().contains(relationType)
				&& !relationType.equals("TemporalSpan")) {
			for (String expectedArgument : expectedArgumentOccurences.keySet()) {
				if (expectedArgumentOccurences.get(expectedArgument) > 0) {
					throw new KBUpdateException("Did not find enough occurences of argument "
							+ expectedArgument + " in relation.");
				}
			}
		}
	}

	private void checkMentalStatePreconditions(KBMentalState.InsertionBuilder<?> insertionBuilder)
			throws KBUpdateException {
		MentalStateType mentalStateType = insertionBuilder.getType().getType().equals("Sentiment") ? MentalStateType.Sentiment
				: MentalStateType.Belief;

		// check preconditions:
		// 1. There should be at least one provenance entry
		if (insertionBuilder.getProvenances() == null
				|| insertionBuilder.getProvenances().isEmpty()) {
			throw new KBUpdateException(mentalStateType
					+ " should have at least one provenance entry.");
		}

		// 2. There must be at least one argument each of source, strength, and
		// target types
		boolean containsSourceArg = false;
		boolean containsStrengthArg = false;
		boolean containsTargetArg = false;
		for (KBRelationArgument.InsertionBuilder argument : insertionBuilder.getArguments()) {
			String argType = argument.getRole().getType();
			if (argType.equals("source")) {
				containsSourceArg = true;
			} else if (argType.equals("strength")) {
				containsStrengthArg = true;
			} else if (argType.equals("target")) {
				containsTargetArg = true;
			} else if (!argType.equals("validTemporalSpan")) {
				throw new KBUpdateException(
						"Only argument types of source, strength, target, and validTemporalSpan are allowed in a "
								+ mentalStateType.toString() + ".");
			}
		}

		if (!containsSourceArg || !containsStrengthArg || !containsTargetArg) {
			throw new KBUpdateException(
					mentalStateType.toString()
							+ " must contain at least one argument each of source, strength, and target types.");
		}
	}

	/**
	 * <p>
	 * add map from Adept KB ID to external KB Ids.
	 * </p>
	 * 
	 * <p>
	 * Note that an external KB ID map to one and only one Adept KB ID. As a
	 * result, if you attempt to map an already seen external KB entity to a new
	 * Adept Entity, this method will fail.
	 * </p>
	 * 
	 * <p>
	 * On the other hand, an Adept KB entity is allowed to map to more than one
	 * external KB entity.
	 * </p>
	 */
	private UpdateRequest addMapToExternalKBIds(KBID adeptKbId, Set<KBID> externalKBIds)
			throws KBUpdateException {
		UpdateRequest externalIdInsertRequest = UpdateFactory.create();
		if (externalKBIds != null) {
			for (KBID kbId : externalKBIds) {
				UpdateRequest updateRequest = sparqlQueryBuilder.createExternalIDInsertQueries(
						adeptKbId.getObjectID(), kbId.getObjectID(), kbId.getKBNamespace());
				for (Update update : updateRequest.getOperations()) {
					externalIdInsertRequest.add(update);
				}
			}
		}
		return externalIdInsertRequest;
	}
        
    /**
	 * <p>
	 * delete map from Adept KB ID to external KB Ids.
	 * </p>
	 */
	private UpdateRequest deleteMapToExternalKBIds(KBID adeptKbId, Set<KBID> externalKBIds)
			throws KBUpdateException {
		UpdateRequest externalIdInsertRequest = UpdateFactory.create();
		if (externalKBIds != null) {
			for (KBID kbId : externalKBIds) {
				UpdateRequest updateRequest = sparqlQueryBuilder.createExternalIDDeleteQueries(
						adeptKbId.getObjectID(), kbId.getObjectID(), kbId.getKBNamespace());
				for (Update update : updateRequest.getOperations()) {
					externalIdInsertRequest.add(update);
				}
			}
		}
		return externalIdInsertRequest;
	}

	/**
	 * <p>
	 * API to insert Adept Number into the KB. The method accepts an Adept
	 * NumericValue object, and a list of mentions that resolve to this date.
	 * 
	 * <p>
	 * There is a check to see if the number that is to be inserted already has
	 * an ID in the Adept-KB. If so, that ID will be REUSED. Note that this
	 * differs from the behavior of insertEntity, since number objects are
	 * ultimately canonical values. Thus this method can be used to add
	 * additional NumberPHrase mentions to an existing Number.
	 * </p>
	 * 
	 * <p>
	 * The KBNumber object stores its canonical value. The mention metadata,
	 * including token offsets, mention type, source document and source
	 * algorithm are stored as part of the metadata database.
	 * </p>
	 * 
	 * @param numberInsertionBuilder
	 *            the Adept entity object to be inserted.
	 */
	protected void insertNumber(KBNumber.InsertionBuilder numberInsertionBuilder)
			throws KBUpdateException {

		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);

			String numberId = UUID.randomUUID().toString();

			Optional<KBNumber> existingNumber = getNumberByValue(numberInsertionBuilder.getNumber());
			List<KBTextProvenance.InsertionBuilder> existingProvenances = new ArrayList<KBTextProvenance.InsertionBuilder>();
			if (existingNumber.isPresent()) {
				numberId = existingNumber.get().getKBID().getObjectID();
				existingProvenances = getTextProvenancesByOwnerId(numberId, sqlConnection);
			}

			for (KBProvenance.InsertionBuilder numberPhrase : numberInsertionBuilder
					.getProvenances()) {
				insertTextProvenance(numberId, (KBTextProvenance.InsertionBuilder) numberPhrase,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			numberInsertionBuilder.addProvenances(existingProvenances);

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();

			if (!existingNumber.isPresent()) {
				// insert date triples
				UpdateRequest xsdDateInsertRequest = sparqlQueryBuilder.createNumberInsertQueries(
						numberInsertionBuilder.getNumber(), numberId);
				UpdateProcessor upp = sparqlService.getUpdateProcessor(xsdDateInsertRequest);
				upp.execute();
			}

			sqlConnection.commit();

			numberInsertionBuilder
					.setKBID(new KBID(numberId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert number.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Save a document text into the KB. The documentID and corpusID are used as
	 * keys to store the document. A pre-existing text of the same documentID
	 * and corpusID will cause a KBUpdateException.
	 * 
	 * @param documentID
	 * @param corpusID
	 * @param text
	 * @throws KBUpdateException
	 */
	public void saveDocumentText(String documentID, String corpusID, String text)
			throws KBUpdateException {
		Connection sqlConnection = null;
		PreparedStatement statement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			statement = sqlConnection.prepareStatement(SqlQueryBuilder.insertDocumentText);
			statement.setString(1, documentID);
			statement.setString(2, corpusID);
			statement.setString(3, text);
			statement.execute();
			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert document text.", e);
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Delete a document text in the KB.
	 * 
	 * @param documentID
	 * @param corpusID
	 * @throws KBUpdateException
	 */
	public void deleteDocumentText(String documentID, String corpusID) throws KBUpdateException {
		Connection sqlConnection = null;
		PreparedStatement statement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			statement = sqlConnection.prepareStatement(SqlQueryBuilder.deleteDocumentText);
			statement.setString(1, documentID);
			statement.setString(2, corpusID);
			statement.execute();
			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Failed to delete document text.", e);
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Close the connection to the KB, freeing database connection resources.
	 * 
	 * @throws KBUpdateException
	 */
	public void close() throws KBUpdateException {
		try {
			this.quickJDBC.close();
		} catch (SQLException e) {
			throw new KBUpdateException("Could not close SQL Connections", e);
		}
	}

	/**
	 * @param insertionBuilder
	 * @throws KBUpdateException
	 */
	protected void insertGenericThing(KBGenericThing.InsertionBuilder insertionBuilder)
			throws KBUpdateException {
		Connection sqlConnection = null;
		PreparedStatement chunkInsertBatchStatement = null;
		PreparedStatement textProvenanceInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);

			String genericThingId = UUID.randomUUID().toString();

			Optional<KBGenericThing> existingGenericThing = getGenericThingByTypeAndValue(
					insertionBuilder.getType(), insertionBuilder.getCanonicalString());
			List<KBTextProvenance.InsertionBuilder> existingProvenances = new ArrayList<KBTextProvenance.InsertionBuilder>();
			if (existingGenericThing.isPresent()) {
				genericThingId = existingGenericThing.get().getKBID().getObjectID();
				existingProvenances = getTextProvenancesByOwnerId(genericThingId, sqlConnection);
			}

			for (KBProvenance.InsertionBuilder provenanceBuilder : insertionBuilder
					.getProvenances()) {
				insertTextProvenance(genericThingId,
						(KBTextProvenance.InsertionBuilder) provenanceBuilder,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						sqlConnection);
			}

			insertionBuilder.addProvenances(existingProvenances);

			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();

			if (!existingGenericThing.isPresent()) {
				// insert date triples
				UpdateRequest insertGenericThingRequest = sparqlQueryBuilder
						.createGenericThingInsertQueries(insertionBuilder.getType(),
								insertionBuilder.getCanonicalString(), genericThingId);
				UpdateProcessor upp = sparqlService.getUpdateProcessor(insertGenericThingRequest);
				upp.execute();
			}

			sqlConnection.commit();

			insertionBuilder
					.setKBID(new KBID(genericThingId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert Generic Thing.", e);
		} finally {
			try {
				if (chunkInsertBatchStatement != null)
					chunkInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (textProvenanceInsertBatchStatement != null)
					textProvenanceInsertBatchStatement.close();
			} catch (Exception e) {
			}
			;
			try {
				if (sqlConnection != null)
					sqlConnection.close();
			} catch (Exception e) {
			}
			;
		}
	}
}

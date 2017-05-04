package adept.kbapi;

/*-
 * #%L
 * adept-kb
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

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
import adept.kbapi.sparql.SparqlUtils;
import adept.kbapi.sql.ConnectionStatistics;
import adept.kbapi.sql.QuickJDBC;
import adept.kbapi.sql.SqlQueryBuilder;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
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

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

  private static Logger log = LoggerFactory.getLogger(KB.class);

	private final QuickJDBC quickJDBC;
	private final SparqlQueryBuilder sparqlQueryBuilder;
	private final KBOntologyModel kbOntologyModel;
	private final SPARQLService sparqlService;

	private LRUCache<String, Boolean> insertedCorpora;
	private LRUCache<String, Boolean> insertedSourceAlgorithms;

	public static final int CACHE_SIZE = 500;

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
		} else {
			Query query = sparqlQueryBuilder.createGetInformationForMultipleEntitiesQuery(kbUris);
			return getEntities(query);
		}
	}

	private List<KBEntity> getEntities(Query query) throws KBQueryException {
    QueryExecution qexec = null;

    try {

      qexec = sparqlService.getQueryExecution(query);
      com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<KBEntity> entities = new ArrayList<KBEntity>();
			List<String> kbUris = new ArrayList<String>();
			List<IntermediateEntity> intermediateEntities = new ArrayList<IntermediateEntity>();

			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode idNode = item.get("?id");
				String id = idNode.asResource().getURI().split("#")[1];
				kbUris.add(id);

				RDFNode confidenceNode = item.get("?confidence");
				float entityConfidence = confidenceNode.asLiteral().getFloat();

				RDFNode canonicalMentionNode = item.get("?canonicalMention");
				String canonicalMention = null;
				if (canonicalMentionNode.isLiteral()) {
					canonicalMention = canonicalMentionNode.asLiteral().getString();
				} else {
					canonicalMention = SparqlUtils.getLocalName(canonicalMentionNode.asResource());
				}
				String canonicalString = item.get("?entityCanonicalString").asLiteral().getString();

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
					intermediateEntity.canonicalMentionId = canonicalMention;
					intermediateEntity.canonicalMentionString = canonicalString;
					intermediateEntity.canonicalMentionConfidence = canonicalMentionConfidence;
					intermediateEntities.add(intermediateEntity);
				}
				intermediateEntity.types.put(new OntType(typeNode.asResource()), typeConfidence);
			}

			for (IntermediateEntity intermediateEntity : intermediateEntities) {
				KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(
						intermediateEntity.types, new KBID(intermediateEntity.canonicalMentionId, KBOntologyModel.DATA_INSTANCES_PREFIX),
						intermediateEntity.canonicalMentionString,
						intermediateEntity.entityConfidence,
						intermediateEntity.canonicalMentionConfidence);
				entityBuilder.setKBID(new KBID(intermediateEntity.id,
						KBOntologyModel.DATA_INSTANCES_PREFIX));
				entities.add(entityBuilder.build(this, true));
			}

			return entities;

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for entities", ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
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
		return getRelationByIdInternal(kbId, KBRelation.class,true);
	}

  /**
   * <p>
   * Query KB relation without arguments given corresponding KBID.
   * </p>
   *
   * <p>
   * This method returns a "light-weight" KBRelation--one that only has reltionType and
   * confidence values set, and no arguments. This method was added to be useful in certain
   * situations (like KBRelation de-duplication) where fetching arguments of KBRelations may not
   * be required (as arguments are already de-duplicated), and would otherwise slow down the
   * de-duplication process. A user should use this method with caution. For all practical purposes, #getRelationById method should be used.
   * </p>
   *
   * @param kbId
   *            KBID
   *
   * @return KBRelation object without arguments
   */

  public KBRelation getRelationByIdWithoutArgs(KBID kbId) throws KBQueryException {
    return getRelationByIdInternal(kbId, KBRelation.class,false);
  }

	private <T extends KBRelation> KBRelation getRelationByIdInternal(KBID kbId, Class<T>
	    classType, boolean fetchArgs) throws KBQueryException{
    QueryExecution qexec = null;

    try {
      Query query = null;
      if(fetchArgs) {
        query = sparqlQueryBuilder.createGetRelationByIdQuery(kbId.getObjectID());
      }else{
        query = sparqlQueryBuilder.createGetRelationByIdWithoutArgsQuery(
            kbId.getObjectID());
      }
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			List<T> relations = this.<T> getRelationsFromResultSet(
					kbId.getObjectID(), resultSet, classType);
			if (relations.isEmpty()) {
				throw new KBQueryException("Could not find Relation for id: " + kbId.getObjectID());
			}
			return relations.get(0);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relation with ID = "
					+ kbId.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
  		  qexec.close();
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
	private KBPredicateArgument getPredicateArgumentByID(String argumentKbUri) throws KBQueryException {
		String argumentType = getLeafTypeFromId(argumentKbUri);

		KBID predicateArgumentKBID = new KBID(argumentKbUri, KBOntologyModel.DATA_INSTANCES_PREFIX);
		if (argumentType.equals("TemporalSpan")) {
			return getTemporalSpanByID(predicateArgumentKBID);
		}else if (kbOntologyModel.getLeafRelationTypes().contains(argumentType)) {
			return getRelationById(predicateArgumentKBID);
		} else if (kbOntologyModel.getLeafEventTypes().contains(argumentType)){
			return getEventById(predicateArgumentKBID);
		}else if (argumentType.equals("Sentiment")) {
			return getSentimentById(predicateArgumentKBID);
		} else if (argumentType.equals("Belief")) {
			return getBeliefById(predicateArgumentKBID);
		} else if (argumentType.equals("Number")) {
			return getNumberValueByID(predicateArgumentKBID);
		} else if (argumentType.equals("Date")) {
			return getKBDateByDateId(predicateArgumentKBID);
		} else if (argumentType.equals("Statement")) {
			return getArgumentRelationArgumentById(predicateArgumentKBID.getObjectID());
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
		return (KBSentiment)getRelationByIdInternal(kbId, KBSentiment.class,true);
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
		return (KBBelief) getRelationByIdInternal(kbId, KBBelief.class,true);
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
		return (KBTemporalSpan) getRelationByIdInternal(kbId, KBTemporalSpan.class,true);
	}

	/**
	 * Query KB event by KBID
	 *
	 * @return Adept Event object
	 */

	public KBEvent getEventById(KBID kbId) throws KBQueryException {
		return (KBEvent) getRelationByIdInternal(kbId, KBEvent.class,true);
	}

  /**
   * <p>
   * Query KB event without arguments given corresponding KBID.
   * </p>
   *
   * <p>
   * This method returns a "light-weight" KBEvent--one that only has eventType,
   * confidence and realisType map set, and no arguments. This method was added to be useful
   * in certain situations (like KBEvent de-duplication) where fetching arguments of KBEvents may
   * not be required (as arguments are already de-duplicated), and would otherwise slow down the de-duplication process. A user should use
   * this method with caution. For all practical purposes, #getEventById method should be used.
   * </p>
   *
   * @param kbId
   *            KBID
   *
   * @return KBEvent object without arguments
   */

  public KBEvent getEventByIdWithoutArgs(KBID kbId) throws KBQueryException {
    return (KBEvent) getRelationByIdInternal(kbId, KBEvent.class,false);
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
		} finally {
		  if (null != qexec) {
		    qexec.close();
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

		try (Connection sqlConnection = quickJDBC.getConnection()){
			return getEntitiesByStringReference(value, sqlConnection);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with reference " + value, ex);
		}
	}

	private List<KBEntity> getEntitiesByStringReference(String value, Connection sqlConnection)
			throws SQLException, KBQueryException {

		try (PreparedStatement entitiesByStringReferenceQueryPreparedStmt
		    = SqlQueryBuilder.createKBIdsByChunkValueQuery(value, sqlConnection)) {
		  java.sql.ResultSet entitiesByStringReferenceQueryResult = entitiesByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredEntities = new ArrayList<String>();
			while (entitiesByStringReferenceQueryResult.next()) {
				String entityId = entitiesByStringReferenceQueryResult.getString("KBId");
				if (entityId != null && !discoveredEntities.contains(entityId)) {
					discoveredEntities.add(entityId);
				}
			}

			return getEntitiesByIds(discoveredEntities);

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

		try (Connection sqlConnection = quickJDBC.getConnection()){
			return getEntitiesByIds(getObjectIdsByRegexMatch(regex, sqlConnection, caseSensitive));
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with reference " + regex, ex);
		}
	}

	private List<String> getObjectIdsByRegexMatch(String regex, Connection sqlConnection, boolean caseSensitive) throws SQLException {
		PreparedStatement idsByRegexQueryPreparedStmt = null;
		java.sql.ResultSet idsByRegexQueryResult = null;

		try {
			idsByRegexQueryPreparedStmt = SqlQueryBuilder.createKBIdsByChunkRegexQuery(
					regex, sqlConnection, caseSensitive);
			idsByRegexQueryResult = idsByRegexQueryPreparedStmt.executeQuery();
			List<String> discoveredEntities = new ArrayList<String>();
			while (idsByRegexQueryResult.next()) {
				String entityId = idsByRegexQueryResult.getString("KBId");
				if (entityId != null && !discoveredEntities.contains(entityId)) {
					discoveredEntities.add(entityId);
				}
			}

			return discoveredEntities;

		} finally {
			try {
				if (idsByRegexQueryResult != null)
					idsByRegexQueryResult.close();
			} catch (Exception e) {
			}
			;
			try {
				if (idsByRegexQueryPreparedStmt != null)
					idsByRegexQueryPreparedStmt.close();
			} catch (Exception e) {
			}
			;
		}
	}

	/**
	 * Gets a list of {@link KBEntity} objects with the given {@link OntType}
	 * @param type the {@link OntType} associated with the KBEntities to search
	 * @return List of {@link KBEntity} objects with the given {@link OntType}
	 * @throws KBQueryException if the list of KBEntities could not be fetched for any reason
	 */

	public List<KBEntity> getEntitiesByType(OntType type) throws KBQueryException {
		Query query = sparqlQueryBuilder.createGetEntitiesByTypeQuery(type.getType());
		return getEntities(query);
	}

	/**
	 * get Adept KB object corresponding to an external KB entity. The method
	 * will return an Adept KB object, given the external KB ID has already been
	 * stored in the database.
	 *
	 * @deprecated Use {@link #getKBObjectsByExternalID(KBID)} instead
	 *
	 * @return KBPredicateArgument
	 */
	@Deprecated
	public Optional<KBPredicateArgument> getKBObjectByExternalID(KBID externalKbId)
			throws KBQueryException {
		Preconditions.checkNotNull(externalKbId);
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetAdeptIdByExternalIdAndName(
                      externalKbId.getObjectID(), externalKbId.getKBNamespace());
      qexec = sparqlService.getQueryExecution(query);
		  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();
				String kbObjectURI = subjectAsString.split("#")[1];

				return Optional.fromNullable(getPredicateArgumentByID(kbObjectURI));
			} else {
				return Optional.<KBPredicateArgument> absent();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for objects for external id: "
					+ externalKbId.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get a set of Adept KB objects corresponding to an external KB entity. The method
	 * will return a set of Adept KB objects, given the external KB ID has already been
	 * stored in the database.
	 *
	 * @return Optional&lt;Set&lt;KBPredicateArgument&gt;&gt;
	 */
	public Optional<Set<KBPredicateArgument>> getKBObjectsByExternalID(KBID externalKbId)
			throws KBQueryException {
		Preconditions.checkNotNull(externalKbId);
		QueryExecution qexec = null;
		Set<KBPredicateArgument> kbObjects = new HashSet<>();
		try {
			Query query = sparqlQueryBuilder.createGetAdeptIdByExternalIdAndName(
					externalKbId.getObjectID(), externalKbId.getKBNamespace());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			while (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();
				String kbObjectURI = subjectAsString.split("#")[1];

				kbObjects.add(getPredicateArgumentByID(kbObjectURI));
			}
			if(kbObjects.isEmpty()){
				kbObjects = null;
			}
			return Optional.fromNullable(kbObjects);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for objects for external id: "
					+ externalKbId.getObjectID(), ex);
		} finally {
			if (null != qexec) {
				qexec.close();
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
	public Optional<KBPredicateArgument> getKBObjectByExternalIDAndType(KBID externalKbId,
			OntType type)
			throws KBQueryException {
		Preconditions.checkNotNull(externalKbId);
		Preconditions.checkNotNull(type);
		QueryExecution qexec = null;
		try {
			Query query = sparqlQueryBuilder.createGetAdeptIdByExternalIdNameAndType(
					externalKbId.getObjectID(), externalKbId.getKBNamespace()
					,type.getType());
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();
				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();
				String kbObjectURI = subjectAsString.split("#")[1];

				return Optional.fromNullable(getPredicateArgumentByID(kbObjectURI));
			} else {
				return Optional.<KBPredicateArgument> absent();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for objects for external id: "
					+ externalKbId.getObjectID(), ex);
		} finally {
			if (null != qexec) {
				qexec.close();
			}
		}
	}

	/**
	 * get Adept KB entities by value and type.
	 *
	 *
	 * @param value
	 * @param type
	 * @return List&lt;KBID&gt;
	 * @throws adept.kbapi.KBQueryException
	 */

	public List<KBEntity> getEntitiesByValueAndType(String value, OntType type)
			throws KBQueryException {

		try (Connection sqlConnection = quickJDBC.getConnection()) {
			return getEntitiesByValueAndType(value, type, sqlConnection);
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for entities with type " + type.getType()
					+ " and reference " + value, ex);
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
    QueryExecution qexec = null;
    try {

      Query query = sparqlQueryBuilder.createGetDateByXSDValueQuery(value);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String dateURI = subjectAsString.split("#")[1];

				KBDate.InsertionBuilder dateBuilder = KBDate.xsdDateInsertionBuilder(
				    value);

				dateBuilder.setKBID(new KBID(dateURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(dateBuilder.build(this, true));
			} else {
				return Optional.absent();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for date with xsd value = " + value, ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
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

    QueryExecution qexec = null;
    try {

      Query query = sparqlQueryBuilder.createGetDateByTimexValueQuery(value);
      qexec = sparqlService.getQueryExecution(query);
      com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String dateURI = subjectAsString.split("#")[1];

				KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(value);
				dateBuilder.setKBID(new KBID(dateURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(dateBuilder.build(this, true));
			} else {
				return Optional.absent();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for date with Timex value = " + value, ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}


	protected Set<KBProvenance> getProvenancesForObject(KBID kbid) throws KBQueryException {

		try (Connection sqlConnection = quickJDBC.getConnection()) {
			// get SQL connection instance
			Set<KBProvenance> result = new HashSet<KBProvenance>();
			List<KBTextProvenance.InsertionBuilder> provenances = getTextProvenancesByOwnerId(
			    kbid.getObjectID(), sqlConnection);
			for (KBTextProvenance.InsertionBuilder provenance : provenances) {
				result.add(provenance.build());
			}
			return result;
		} catch (SQLException ex) {
			throw new KBQueryException("Failed to query for provenances for kbid: " + kbid, ex);
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
    QueryExecution qexec = null;
    try {

      String kbUri = kbId.getObjectID();

      Query query = sparqlQueryBuilder.createGetTimexValueForDateIDQuery(kbUri);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
			String timexString = null;
			if (resultSet.hasNext()) {
				timexString = resultSet.next().get(SparqlQueryBuilder.VALUE).asLiteral()
						.getLexicalForm();
				KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(
					    timexString);

					dateBuilder.setKBID(kbId);
				return dateBuilder.build(this, true);
			} else { // no matching result
				return null;
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for date with ID = " + kbId.getObjectID(),
					ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get Adept KB relations by KB argument URI.
	 *
	 * @return List&lt;KBRelation&gt;
	 */
	public List<KBRelation> getRelationsByArg(KBID kbId) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetRelationsByArgumentQuery(
          kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with arg "
					+ kbId.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get Adept KB relations by KB argument URI.
	 *
	 * @return {@code List<KBRelation>}
	 */
	public List<KBRelation> getRelationsByArgs(KBID arg1, KBID arg2) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetRelationsByArgumentsQuery(arg1,
          arg2);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with args "
					+ arg1.getObjectID()+ " "+arg2.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get Adept KB Sentiments by two KB argument URIs.
	 *
	 * @return List&lt;KBRelation&gt;
	 */
	public List<KBSentiment> getSentimentsByArgs(KBID arg1, KBID arg2) throws KBQueryException {
    QueryExecution qexec = null;
    try {

      Query query = sparqlQueryBuilder.createGetSentimentsByArgumentsQuery(arg1,
          arg2);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBSentiment> getRelationsFromResultSet(null, resultSet,
					KBSentiment.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with args "
					+ arg1.getObjectID()+ " "+arg2.getObjectID(), ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	/**
	 * get Adept KB Beliefs by two KB argument URIs.
	 *
	 * @return List&lt;KBRelation&gt;
	 */
	public List<KBBelief> getBeliefsByArgs(KBID arg1, KBID arg2) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetBeliefsByArgumentsQuery(arg1,
          arg2);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBBelief> getRelationsFromResultSet(null, resultSet,
					KBBelief.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with args "
					+ arg1.getObjectID()+ " "+arg2.getObjectID(), ex);
		} finally {
      if (null != qexec) {
        qexec.close();
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
				String type = item.getResource("type").getLocalName();
				Class<? extends KBRelation> classType = KBRelation.class;
				if (kbOntologyModel.getLeafEventTypes().contains(type)){
					classType = KBEvent.class;
				} else if (type.equals("Sentiment")) {
					classType = KBSentiment.class;
				} else if (type.equals("Belief")) {
					classType = KBBelief.class;
				} else if (type.equals("TemporalSpan")) {
					classType = KBTemporalSpan.class;
				}
				return getRelationByIdInternal(relationKBID, classType,true);
			} else {
				throw new KBQueryException(
						"Could not find an owning Relation for KBRelationArgument id: "
								+ kbId.getObjectID());
			}

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with arg "
					+ kbId.getObjectID(), ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	/**
	 * get Adept KB sentiments by KB argument URI.
	 *
	 * @return {@code List<KBSentiment>}
	 */
	public List<KBSentiment> getSentimentsByArg(KBID kbId) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetSentimentsByArgumentQuery(
          kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBSentiment> getRelationsFromResultSet(null, resultSet,
					KBSentiment.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with arg "
					+ kbId.getObjectID(), ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	/**
	 * get Adept KB belief IDs by KB argument URI.
	 *
	 * @return {@code List<KBBelief>}
	 */
	public List<KBBelief> getBeliefsByArg(KBID kbId) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetBeliefsByArgumentQuery(kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBBelief> getRelationsFromResultSet(null, resultSet,
					KBBelief.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with arg "
					+ kbId.getObjectID(), ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	/**
	 * get Adept KB relations by mention value.
	 *
	 * @return {@code List<KBRelation>}
	 */

	public List<KBRelation> getRelationsByStringReference(String value) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()) {
			List<String> validRelations = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByStringReference(value, sqlConnection);
			for (String objectId : objectIds) {
				validRelations.add(objectId);
			}
			if (!validRelations.isEmpty()) {
			  QueryExecution qexec = null;
			  try {
          Query query = sparqlQueryBuilder.createGetRelationsByIdsQuery(
              validRelations);
          qexec = sparqlService.getQueryExecution(query);
 				  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				  return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
						KBRelation.class);
			  } finally {
		      if (null != qexec) {
		        qexec.close();
		      }
		    }
			} else {
				return Collections.emptyList();
			}

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with reference " + value, ex);
		}
	}

	/**
	 * get Adept KB relations by mention value.
	 * @param regex
	 * @param caseSensitive
	 * @return {@code List<KBRelation>}
	 * @throws KBQueryException
	 */

	public List<KBRelation> getRelationsByRegexMatch(String regex, boolean caseSensitive) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()) {

			List<String> validRelations = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByRegexMatch(regex, sqlConnection,
			    caseSensitive);
			for (String objectId : objectIds) {
				validRelations.add(objectId);
			}
			if (!validRelations.isEmpty()) {
        QueryExecution qexec = null;
        try {
          Query query = sparqlQueryBuilder.createGetRelationsByIdsQuery(
              validRelations);
          qexec = sparqlService.getQueryExecution(query);
				  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				  return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
						KBRelation.class);
				} finally {
		      if (null != qexec) {
		        qexec.close();
		      }
		    }
			} else {
				return Collections.emptyList();
			}

		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with reference " + regex, ex);
		}
	}

	/**
	 * get Adept KB events by mention value.
	 * @param value
	 * @return {@code List<KBEvent>}
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByStringReference(String value) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()) {

			List<String> validEvents = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByStringReference(value, sqlConnection);
			for (String objectId : objectIds) {
				validEvents.add(objectId);
			}

			if (!validEvents.isEmpty()) {
        Query query = sparqlQueryBuilder.createGetEventsByIdsQuery(validEvents);
        QueryExecution qexec = null;
        try {
          qexec = sparqlService.getQueryExecution(query);
				  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				  return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
						KBEvent.class);
				} finally {
          if (null != qexec) {
            qexec.close();
          }
        }
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with reference " + value, ex);
		}
	}

	/**
	 * get Adept KB events by mention value.
	 *
	 * @param regex
	 * @param caseSensitive
	 * @return {@code List<KBEvent>}
	 */

	public List<KBEvent> getEventsByRegexMatch(String regex, boolean caseSensitive) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()){

			List<String> validEvents = new ArrayList<String>();
			List<String> objectIds = getObjectIdsByRegexMatch(regex, sqlConnection,
			    caseSensitive);
			for (String objectId : objectIds) {
				validEvents.add(objectId);
			}

			if (!validEvents.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetEventsByIdsQuery(
				    validEvents);
        QueryExecution qexec = null;
				try {
				  qexec = sparqlService.getQueryExecution(query);
  				com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				  return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
						KBEvent.class);
        } finally {
          if (null != qexec) {
            qexec.close();
          }
        }
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with reference " + regex, ex);
		}
	}

	private List<String> getObjectIdsByStringReference(String value, Connection sqlConnection) throws SQLException {

		try (PreparedStatement relationsByStringReferenceQueryPreparedStmt
		    = SqlQueryBuilder.createKBIdsByChunkValueQuery(value, sqlConnection)) {
		  java.sql.ResultSet relationsByStringReferenceQueryResult = relationsByStringReferenceQueryPreparedStmt
					.executeQuery();
			List<String> discoveredObjects = new ArrayList<String>();
			while (relationsByStringReferenceQueryResult.next()) {
				String objectId = relationsByStringReferenceQueryResult.getString("KBId");
				discoveredObjects.add(objectId);
			}

			return discoveredObjects;
		}
	}

	/**
	 * get Adept KB sentiments by mention value.
	 *
	 * @return {@code List<KBSentiment>}
	 */
	public List<KBSentiment> getSentimentsByStringReference(String value) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()) {
			List<String> discoveredRelations = getObjectIdsByStringReference(value,
			    sqlConnection);
			if (!discoveredRelations.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetSentimentsByIdsQuery(
				    discoveredRelations);
        QueryExecution qexec = null;
				try {
				  qexec = sparqlService.getQueryExecution(query);
 				  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
				  return this.<KBSentiment> getRelationsFromResultSet(null, resultSet,
						KBSentiment.class);
				} finally {
          if (null != qexec) {
            qexec.close();
          }
        }
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for sentiments with reference " + value, ex);
		}
	}

	/**
	 * get Adept KB beliefs by mention value.
	 * @param value
	 * @return {@code List<KBBelief>}
	 * @throws KBQueryException
	 */
	public List<KBBelief> getBeliefsByStringReference(String value) throws KBQueryException {
		try (Connection sqlConnection = quickJDBC.getConnection()) {
			List<String> discoveredRelations = getObjectIdsByStringReference(value,
			    sqlConnection);

			if (!discoveredRelations.isEmpty()) {
				Query query = sparqlQueryBuilder.createGetBeliefsByIdsQuery(discoveredRelations);
				QueryExecution qexec = null;
				try {
				  qexec = sparqlService.getQueryExecution(query);
				  com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

				  return this.<KBBelief> getRelationsFromResultSet(null, resultSet,
						KBBelief.class);
				} finally {
          if (null != qexec) {
            qexec.close();
          }
        }
			} else {
				return Collections.emptyList();
			}
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for beliefs with reference " + value, ex);
		}
	}

	/**
	 * get Adept KB relations by Type.
	 *
	 * @param type
	 * @return {@code List<KBRelation>} corresponding to input type
	 * @throws KBQueryException
	 */
	public List<KBRelation> getRelationsByType(OntType type) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      String typeString = type.getType();

      Query query = sparqlQueryBuilder.createGetRelationsByTypeQuery(typeString);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with type " + type.getType(),
					ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	@SuppressWarnings("unchecked")
	private <T extends KBRelation> List<T> getRelationsFromResultSet(String inputRelationId,
			com.hp.hpl.jena.query.ResultSet resultSet, Class<T> classType)
			throws KBQueryException {
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
			}

			if (item.get("?argument") != null){
				String argumentId = item.get("?argument").asResource().getURI().split("#")[1];

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
		}

		for (T.AbstractInsertionBuilder<?, T> relationInsertionBuilder : relationBuilders) {
			String relationId = relationInsertionBuilder.getKBID().getObjectID();

			List<QuerySolution> entities = entityArguments.get(relationId);
			if (entities != null) {
				List<KBRelationArgument.InsertionBuilder> entityArgs = buildRelationArgumentsFromEntityQuerySolutions(
						null, entities, entityTypeMap.get(relationId));
				for (KBRelationArgument.InsertionBuilder entityArgument : entityArgs) {
					relationInsertionBuilder.addArgument(entityArgument);
				}
			}

			List<QuerySolution> relationArgumentList = relationArgumentArguments.get(relationId);
			if (relationArgumentList != null) {
				for (QuerySolution item : relationArgumentList) {
					String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
					KBRelationArgument relationArgument = getArgumentRelationArgumentById(
							argumentId);

					String relationArgumentId = item.get("?kbRelationArgumentID").asResource()
							.getURI().split("#")[1];
					relationInsertionBuilder.addArgument(buildKBRelationArgument(new OntType(item
							.get("?role").asResource()), relationArgument,
							item.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									relationArgumentId, KBOntologyModel.DATA_INSTANCES_PREFIX)
							));
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
							KBOntologyModel.DATA_INSTANCES_PREFIX)));
				}
			}

			List<QuerySolution> numbers = numberArguments.get(relationId);
			if (numbers != null) {
				List<KBRelationArgument.InsertionBuilder> numberArgs = buildRelationArgumentsFromNumberQuerySolutions(
						numbers);
				for (KBRelationArgument.InsertionBuilder numberArgument : numberArgs) {
					relationInsertionBuilder.addArgument(numberArgument);
				}
			}

			List<QuerySolution> temporalSpans = temporalSpanArguments.get(relationId);
			if (temporalSpans != null) {
				List<KBRelationArgument.InsertionBuilder> temporalSpanArgs = buildRelationArgumentsFromTemporalSpanQuerySolutions(
						temporalSpans);
				for (KBRelationArgument.InsertionBuilder temporalSpanArgument : temporalSpanArgs) {
					relationInsertionBuilder.addArgument(temporalSpanArgument);
				}
			}

			List<QuerySolution> dates = dateArguments.get(relationId);
			if (dates != null) {
				List<KBRelationArgument.InsertionBuilder> dateArgs = buildRelationArgumentsFromDateQuerySolutions(
						dates);
				for (KBRelationArgument.InsertionBuilder dateArgument : dateArgs) {
					relationInsertionBuilder.addArgument(dateArgument);
				}
			}

			List<QuerySolution> genericThings = genericThingArguments.get(relationId);
			if (genericThings != null) {
				List<KBRelationArgument.InsertionBuilder> genericThingArgs = buildRelationArgumentsFromGenericThingQuerySolutions(
						genericThings);
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

			kbRelations.add(relationInsertionBuilder.build(this, true));
		}

		return kbRelations;
	}

	private KBRelationArgument getArgumentRelationArgumentById(String originalArgumentId) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      KBRelationArgument.InsertionBuilder kbRelationArgumentBuilder = null;

      Query query = sparqlQueryBuilder
                      .createGetArgumentRelationArgumentQuery(originalArgumentId);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				String argumentId = item.get("?argument").asResource().getURI().split("#")[1];

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



					kbRelationArgumentBuilder = buildRelationArgumentsFromEntityQuerySolutions(
							originalArgumentId, Arrays.asList(item),
							entityTypeMap).get(0);
				} else if (item.get("?relationArgumentStatement") != null) {
					KBRelationArgument relationArgument = getArgumentRelationArgumentById(
							argumentId);



					kbRelationArgumentBuilder = buildKBRelationArgument(
							new OntType(item.get("?role").asResource()), relationArgument, item
									.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX)
							);
				} else if (item.get("?numberValue") != null) {

					kbRelationArgumentBuilder = buildRelationArgumentsFromNumberQuerySolutions(
							Arrays.asList(item)).get(0);
				} else if (item.get("?beginDateStatement") != null
						&& item.get("?endDateStatement") != null) {
					kbRelationArgumentBuilder = buildRelationArgumentsFromTemporalSpanQuerySolutions(
							Arrays.asList(item)).get(0);
				} else if (item.get("?date") != null) {

					kbRelationArgumentBuilder = buildRelationArgumentsFromDateQuerySolutions(
							Arrays.asList(item)).get(0);
				} else {
					KBRelation relation = getRelationById(new KBID(argumentId,
							KBOntologyModel.DATA_INSTANCES_PREFIX));

					kbRelationArgumentBuilder = buildKBRelationArgument(
							new OntType(item.get("?role").asResource()), relation,
							item.get("?argumentConfidence").asLiteral().getFloat(), new KBID(
									argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX)
							);
				}
			}

			if (kbRelationArgumentBuilder != null) {
				return kbRelationArgumentBuilder.build(this, new KBID(originalArgumentId,
						KBOntologyModel.DATA_INSTANCES_PREFIX), true);
			} else {
				throw new KBQueryException("No argument of a relation argument exists for id "
						+ originalArgumentId);
			}
		} catch (Exception ex) {
			throw new KBQueryException(
					"Failed to query for argument of a relation argument for id "
							+ originalArgumentId, ex);
		} finally {
      if (null != qexec) {
        qexec.close();
      }
    }
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromEntityQuerySolutions(
			String inputRelationArgumentId, List<QuerySolution> entities,
			Map<String, Map<OntType, Float>> entityTypeMap) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : entities) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			float canonicalMentionConfidence = item.get("?canonicalMentionConfidence").asLiteral()
					.getFloat();
			float entityConfidence = item.get("?entityconfidence").asLiteral().getFloat();
			String canonicalMentionId = null;
			RDFNode canonicalMentionNode = item.get("?entityCanonicalMention");
			if (canonicalMentionNode.isLiteral()) {
				canonicalMentionId = canonicalMentionNode.asLiteral().toString();
			} else {
				canonicalMentionId = SparqlUtils.getLocalName(canonicalMentionNode.asResource());
			}
			String canonicalMentionString = item.get("?entityCanonicalString").asLiteral().getString();

			Map<OntType, Float> entityTypes = entityTypeMap.get(argumentId);
			KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(
			    entityTypes,
			    new KBID(canonicalMentionId, KBOntologyModel.DATA_INSTANCES_PREFIX),
			    canonicalMentionString, entityConfidence, canonicalMentionConfidence);

			entityBuilder.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			String relationArgumentId = inputRelationArgumentId;
			if (relationArgumentId == null) {
				relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
						.split("#")[1];
			}
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), entityBuilder.build(this, true), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromNumberQuerySolutions(
			List<QuerySolution> numbers) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : numbers) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			Number number = convertToNumber(item.getLiteral("?numberValue"));
			KBNumber.InsertionBuilder numberBuilder = KBNumber.numberInsertionBuilder(
			    number);
			numberBuilder.setKBID(new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));


			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), numberBuilder.build(this, true), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromTemporalSpanQuerySolutions(
			List<QuerySolution> temporalSpans) {
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


				// TODO: confidences of temporal span date arguments are not in
				// the kb at the moment, once that's fixed, add them to this
				// query
				KBRelationArgument.InsertionBuilder beginDateBuidler = temporalSpanBuilder
						.createBeginDateArgument(dateBuilder.build(this, true), 1,
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


				// TODO: confidences of temporal span date arguments are not in
				// the kb at the moment, once that's fixed, add them to this
				// query
				KBRelationArgument.InsertionBuilder endDateBuilder = temporalSpanBuilder
						.createEndDateArgument(dateBuilder.build(this, true), 1,
								new HashSet<KBProvenance.InsertionBuilder>());
				endDateBuilder.kbid = new KBID(endDateStatementId,
						KBOntologyModel.DATA_INSTANCES_PREFIX);
			}


			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), temporalSpanBuilder.build(this, true), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromDateQuerySolutions(
			List<QuerySolution> dates) {
		List<KBRelationArgument.InsertionBuilder> relationArguments = new ArrayList<KBRelationArgument.InsertionBuilder>();
		for (QuerySolution item : dates) {
			String argumentId = item.get("?argument").asResource().getURI().split("#")[1];
			String timexString = item.get("?date").asLiteral().getLexicalForm();
			KBDate.InsertionBuilder dateBuilder = KBDate.timexInsertionBuilder(
			    timexString);
			dateBuilder.setKBID(
			    new KBID(argumentId, KBOntologyModel.DATA_INSTANCES_PREFIX));

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), dateBuilder.build(this, true), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX)));
		}

		return relationArguments;
	}

	private List<KBRelationArgument.InsertionBuilder> buildRelationArgumentsFromGenericThingQuerySolutions(
			List<QuerySolution> genericThings) {
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

			String relationArgumentId = item.get("?kbRelationArgumentID").asResource().getURI()
					.split("#")[1];
			relationArguments.add(buildKBRelationArgument(new OntType(item.get("?role")
					.asResource()), genericThingBuilder.build(this, true), item.get("?argumentConfidence")
					.asLiteral().getFloat(), new KBID(relationArgumentId,
					KBOntologyModel.DATA_INSTANCES_PREFIX)));
		}

		return relationArguments;
	}

	private KBRelationArgument.InsertionBuilder buildKBRelationArgument(OntType type,
			KBPredicateArgument kbPredicateArgument, float confidence, KBID kbid) {
		KBRelationArgument.InsertionBuilder kbRelationArgument = KBRelationArgument
				.insertionBuilder(type, kbPredicateArgument, confidence);
		kbRelationArgument.setKBID(kbid);
		return kbRelationArgument;
	}

	/**
	 * get Adept KB relations by argument and type.
	 *
	 * @param kbId
	 * @param type
	 * @return {@code List<KBRelation>}
     * @throws KBQueryException
	 */
	public List<KBRelation> getRelationsByArgAndType(KBID kbId, OntType type)
			throws KBQueryException {
    QueryExecution qexec = null;
    try {

      Query query = sparqlQueryBuilder.createGetRelationsByArgumentAndTypeQuery(
          type.getType(), kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBRelation> getRelationsFromResultSet(null, resultSet,
					KBRelation.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for relations with type " + type.getType()
					+ " and arg " + kbId.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get KB event IDs given event argument
	 * 
	 * @param kbId
	 * @return {@code List<KBEvent>} containing input URI as argument
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByArg(KBID kbId) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetEventsByArgumentQuery(
          kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
					KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with arg " + kbId.getObjectID(),
					ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get KB event IDs given event arguments
	 * @param arg1
	 * @param arg2
	 * @return {@code List<KBEvent>} containing input URI as argument
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByArgs(KBID arg1, KBID arg2) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetEventsByArgumentsQuery(arg1, arg2);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
					KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with arg " + arg1.getObjectID()+" "+arg2.getObjectID(),
					ex);
		} finally {
			if (qexec != null) {
  			qexec.close();
  		}
		}
	}

	/**
	 * get KB events by Type
	 *
	 * @param type
	 * @return {@code List<KBEvent>} corresponding to input type
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByType(OntType type) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      String typeString = type.getType();

      Query query = sparqlQueryBuilder.createGetRelationsByTypeQuery(typeString);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
			    KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with type " + type.getType(), ex);
		} finally {
      if (qexec != null) {
        qexec.close();
      }
    }
	}

	/**
	 * get KB events by argument and type
	 *
	 * @return {@code List<KBEvent>}
	 * @throws KBQueryException
	 */

	public List<KBEvent> getEventsByArgAndType(KBID kbId, OntType type) throws KBQueryException {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetRelationsByArgumentAndTypeQuery(
          type.getType(), kbId.getObjectID());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();

			return this.<KBEvent> getRelationsFromResultSet(null, resultSet,
			    KBEvent.class);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for events with type " + type.getType()
					+ " and arg " + kbId.getObjectID(), ex);
		} finally {
      if (qexec != null) {
        qexec.close();
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
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetRelatedEntitiesQuery(
          kbId.getObjectID(), depth);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
      return getRelatedEntitiesFromResultSet(resultSet);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for entities related to ID = "
					+ kbId.getObjectID(), ex);
		} finally {
      if (qexec != null) {
        qexec.close();
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
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetRelatedEntitiesByRelationTypeQuery(
          kbId.getObjectID(), depth, type.getType());
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
      return getRelatedEntitiesFromResultSet(resultSet);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for entities related to ID = "
					+ kbId.getObjectID(), ex);
		} finally {
      if (qexec != null) {
        qexec.close();
      }
    }
	}

	private List<KBEntity> getRelatedEntitiesFromResultSet(
			com.hp.hpl.jena.query.ResultSet resultSet) {
		List<KBEntity> relatedEntities = new ArrayList<KBEntity>();

		Map<String, QuerySolution> entities = new HashMap<String, QuerySolution>();
		Map<String, Map<OntType, Float>> entityTypeMap = new HashMap<String, Map<OntType, Float>>();

		while (resultSet.hasNext()) {
			QuerySolution item = resultSet.next();

			String relatedEntityId = item.get("?relatedEntityId").asResource().getURI().split("#")[1];
			if (!entities.containsKey(relatedEntityId)) {
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

		for (Map.Entry<String, QuerySolution> entry : entities.entrySet()) {
		  String entityId = entry.getKey();
			QuerySolution item = entry.getValue();
			float canonicalMentionConfidence = item.get("?canonicalMentionConfidence").asLiteral()
					.getFloat();
			float entityConfidence = item.get("?entityconfidence").asLiteral().getFloat();
			RDFNode canonicalMentionNode = item.get("?entityCanonicalMention");
			String canonicalMentionString = item.get("?entityCanonicalString").asLiteral().getString();
			String canonicalMentionId;
			if (canonicalMentionNode.isLiteral()) {
				canonicalMentionId = canonicalMentionNode.asLiteral().toString();
			} else {
				canonicalMentionId = SparqlUtils.getLocalName(canonicalMentionNode.asResource());
			}

			Map<OntType, Float> entityTypes = entityTypeMap.get(entityId);

			KBEntity.InsertionBuilder entityBuilder = KBEntity.entityInsertionBuilder(entityTypes,
					new KBID(canonicalMentionId, KBOntologyModel.DATA_INSTANCES_PREFIX),
					canonicalMentionString, entityConfidence,
					canonicalMentionConfidence);
			entityBuilder.setKBID(new KBID(entityId, KBOntologyModel.DATA_INSTANCES_PREFIX));
			relatedEntities.add(entityBuilder.build(this, true));
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
		java.sql.ResultSet result = null;

		try (PreparedStatement preparedStmt = SqlQueryBuilder.createGetKBIDsByChunkQuery(sourceDocId, beginOffset,
        endOffset, sqlConnection)) {
			List<KBPredicateArgument> kbObjects = new ArrayList<KBPredicateArgument>();
			List<String> kbIds = new ArrayList<String>();

			result = preparedStmt.executeQuery();
			while (result.next()) {
				kbIds.add(result.getString("KBId"));
			}

			for (String kbid : kbIds) {
				kbObjects.add(getPredicateArgumentByID(kbid));
			}

			return kbObjects;
		} catch (Exception ex) {
			throw new KBQueryException("ERROR: Could not get KB objects within chunk.", ex);
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
	 * Get a list of KBIDs for objects which have the given type, but not any of the
	 * ignoredTypes. The type and ignoredType strings should be the same as type-values in
	 * the KB. For example, you can get all objects which are of type "adept-base:Relation" but
	 * not of type "adept-base:Event" (note that adept-base:Event is a subtype of
	 * adept-base:Relation) using this API.
	 * @param type ontology type string for the KBObjects to retrieve
	 * @param ignoredTypes ontology type strings that should not be associated with the
	 *                            retrieved KBObjects
	 * @return List of KBIDs of KBObjects which have the given type, but none of the ignore
	 * types.
	 */

	public List<KBID> getKBIDsByType(String type, String[] ignoredTypes) {
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetIdsByTypeQuery(type, ignoredTypes);
      qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
			List<KBID> ids = new ArrayList<KBID>();
		//TODO This is the real code
			while (resultSet.hasNext()) {
				// Get the entity by ID
				QuerySolution item = resultSet.next();
				ids.add(new KBID(item.getResource("id")));
			}
		//TODO End real code
		//TODO Delete below loop to test over all items in DB
//			for (int i = 0; i < 100; i++) {
//				// Get the entity by ID
//				if (resultSet.hasNext()) {
//					QuerySolution item = resultSet.next();
//					ids.add(new KBID(item.getResource("id")));
//				} else {
//					break;
//				}
//			}
		//TODO End small chunk loop
			return ids;
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * get all properties from triple store associated with given KB URI.
	 */
	private String getLeafTypeFromId(String id) {
    Query query = sparqlQueryBuilder.createGetLeafTypeBySubjectURIQuery(id);
    QueryExecution qexec = null;

    try {
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
		} finally {
      if (null != qexec) {
        qexec.close();
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
			preparedStmt = SqlQueryBuilder.createTextProvenanceByIdQuery(id,
			    sqlConnection);
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



	/**
	 * gets all external KB IDs that the given Adept KB ID maps to.
	 *
	 * @param adeptKbId the KBID of an object in the KB
	 * @return List of external KBIDs associated with the object
	 */
	public List<KBID> getExternalKBIDs(KBID adeptKbId) {
		List<KBID> externalKBIDs = new ArrayList<KBID>();

		Query query = sparqlQueryBuilder.createGetExternalIdsByAdeptId(
		    adeptKbId.getObjectID());
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
		} finally {
			if (qexec != null){
				qexec.close();
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
		QueryExecution qexec = null;

		try {
			// / get SQL connection instance
			qexec = sparqlService.getQueryExecution(query);
			com.hp.hpl.jena.query.ResultSet resultSet = qexec.execSelect();
			if (resultSet.hasNext()) {
				QuerySolution item = resultSet.next();

				RDFNode subject = item.get(SparqlQueryBuilder.SUBJECT);
				String subjectAsString = subject.asResource().getURI();

				String numberURI = subjectAsString.split("#")[1];
				KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(number);
				builder.setKBID(new KBID(numberURI, KBOntologyModel.DATA_INSTANCES_PREFIX));
				return Optional.of(builder.build(this, true));
			}
			return Optional.absent();
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for number with value = " + number, ex);
		} finally {
			if (qexec != null){
				qexec.close();
			}
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
		QueryExecution qexec = null;
		try {
			Query query = sparqlQueryBuilder.createGetNumberByIDQuery(
			    numberId.getObjectID());
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

			KBNumber.InsertionBuilder numberBuilder = KBNumber.numberInsertionBuilder(
			    number);

			numberBuilder.setKBID(numberId);
			return numberBuilder.build(this, true);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for number with ID = "
					+ numberId.getObjectID(), ex);
		} finally {
			if (qexec != null){
			  qexec.close();
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
    QueryExecution qexec = null;
    try {
      Query query = sparqlQueryBuilder.createGetGenericThingByIDQuery(
          genericThingId
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
			genericThingBuilder.setKBID(genericThingId);
			return genericThingBuilder.build(this, true);
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for generic thing with ID = "
					+ genericThingId.getObjectID(), ex);
		} finally {
		  if (null != qexec) {
		    qexec.close();
		  }
		}
	}

	/**
	 * Get a generic Thing by string value and type.
	 *
	 * Returns Optional.absent if generic thing does not exist in KB.
	 *
	 * @return
	 * @throws KBQueryException
	 */
	public Optional<KBGenericThing> getGenericThingByTypeAndValue(OntType type,
			String canonicalString) throws KBQueryException {
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(canonicalString);
		QueryExecution qexec = null;
		try {
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

			genericThingBuilder.setKBID(kbId);
			return Optional.of(genericThingBuilder.build(this, true));
		} catch (Exception ex) {
			throw new KBQueryException("Failed to query for generic thing with Type = "
					+ type.getType() + " and value \"" + canonicalString + "\"", ex);
		} finally {
			if (qexec != null){
				qexec.close();
			}
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
		PreparedStatement preparedStmt = null;
		java.sql.ResultSet resultSet = null;

		Optional<String> result = null;
		try (Connection sqlConnection = quickJDBC.getConnection()) {
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
		}
		return result;
	}

	private class IntermediateEntity {
		public String id;
		public float entityConfidence;
		public String canonicalMentionId;
		public String canonicalMentionString;
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
		PreparedStatement documentInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

			String entityId = UUID.randomUUID().toString();
			UpdateRequest entityInsertRequest = UpdateFactory.create();

			// check entity insert preconditions
			// check that there is atleast one mention
			if (entityInsertionBuilder.getProvenances() == null || entityInsertionBuilder
							.getProvenances().size() == 0) {
				throw new KBUpdateException(
						"Entity should have at least one mention. Cannot complete insertion for entity");
			}

			// insert canonical mention
			if (entityInsertionBuilder.getCanonicalMentionBuilder().isPresent()) {
				KBTextProvenance.InsertionBuilder textMention = (KBTextProvenance.InsertionBuilder) entityInsertionBuilder.getCanonicalMentionBuilder().get();

				insertTextProvenance(entityId, textMention, chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
						documentInsertBatchStatement, sqlConnection);
			}
			//

			// insert other entity mentions
			for (KBProvenance.InsertionBuilder mention : entityInsertionBuilder.getProvenances()) {
				KBTextProvenance.InsertionBuilder textMention = (KBTextProvenance.InsertionBuilder) mention;

				KBTextProvenance.InsertionBuilder canonicalMention = (KBTextProvenance.InsertionBuilder) entityInsertionBuilder.getCanonicalMentionBuilder().get();

				if (!textMention.getChunkId().equals(canonicalMention.getChunkId())
						|| textMention.getConfidence() != entityInsertionBuilder.getCanonicalMentionConfidence()){
					insertTextProvenance(entityId, textMention,
							chunkInsertBatchStatement,
							textProvenanceInsertBatchStatement,
							documentInsertBatchStatement,
							sqlConnection);
				} else {
					textMention.kbid = entityInsertionBuilder.getCanonicalMentionID();
				}
			}


			documentInsertBatchStatement.executeBatch();
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
					.setKBID(new KBID(entityId,
					    KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert entity: "+e.getMessage(), e);
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
				if (documentInsertBatchStatement != null)
					documentInsertBatchStatement.close();
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
   * API to insert OpenIE relation-argument into the KB. The method accepts a KBOpenIEArgument.
   * InsertionBuilder.
   * </p>
   *
   *
   * @param openIEArgumentInsertionBuilder
   *            the OpenIE argument to be inserted.
   */
  protected void insertOpenIEArgument(KBOpenIEArgument.InsertionBuilder
      openIEArgumentInsertionBuilder)
      throws KBUpdateException {
    Connection sqlConnection = null;
    PreparedStatement chunkInsertBatchStatement = null;
    PreparedStatement textProvenanceInsertBatchStatement = null;
    PreparedStatement documentInsertBatchStatement = null;

    try {
      sqlConnection = quickJDBC.getConnection();
      chunkInsertBatchStatement = sqlConnection
	  .prepareStatement(SqlQueryBuilder.insertTextChunk);
      textProvenanceInsertBatchStatement = sqlConnection
	  .prepareStatement(SqlQueryBuilder.insertOpenIEArgumentProvenance);
      documentInsertBatchStatement = sqlConnection.prepareStatement(
	  SqlQueryBuilder.insertSourceDocument);

      String openIEArgumentId = UUID.randomUUID().toString();

      //there should be at least one provenance for the OpenIEArgument
      if (openIEArgumentInsertionBuilder.getProvenances() == null || openIEArgumentInsertionBuilder
	  .getProvenances().size() == 0) {
	throw new KBUpdateException(
	    "OpenIEArgument should have at least one provenance. Cannot complete insertion for "
		+ "OpenIEArgument");
      }

      PreparedStatement argumentInsertionStmt = SqlQueryBuilder
	  .createOpenIEArgumentInsertionStatement(openIEArgumentInsertionBuilder,
	      openIEArgumentId, sqlConnection);
      argumentInsertionStmt.executeUpdate();

      sqlConnection.commit();

      // insert the provenances
      for (KBProvenance.InsertionBuilder argChunk : openIEArgumentInsertionBuilder
	  .getProvenances()) {
	KBTextProvenance.InsertionBuilder textProvenance = (KBTextProvenance.InsertionBuilder)
	    argChunk;

	insertTextProvenance(openIEArgumentId, textProvenance,
	    chunkInsertBatchStatement,
	    textProvenanceInsertBatchStatement,
	    documentInsertBatchStatement,
	    sqlConnection);
      }

      documentInsertBatchStatement.executeBatch();
      chunkInsertBatchStatement.executeBatch();
      textProvenanceInsertBatchStatement.executeBatch();

      sqlConnection.commit();

      openIEArgumentInsertionBuilder
	  .setKBID(new KBID(openIEArgumentId,
	      KBOntologyModel.DATA_INSTANCES_PREFIX));
    } catch (Exception e) {
      throw new KBUpdateException("Failed to insert openIE relation: "+e.getMessage(), e);
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
	if (documentInsertBatchStatement != null)
	  documentInsertBatchStatement.close();
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
   * API to insert OpenIE relation into the KB. The method accepts a KBOpenIERelation.
   * InsertionBuilder.
   * </p>
   *
   *
   * @param openIERelationInsertionBuilder
   *            the Open relation to be inserted.
   */
  protected void insertOpenIERelation(KBOpenIERelation.InsertionBuilder openIERelationInsertionBuilder)
      throws KBUpdateException {
    Connection sqlConnection = null;
    PreparedStatement chunkInsertBatchStatement = null;
    PreparedStatement textProvenanceInsertBatchStatement = null;
    PreparedStatement documentInsertBatchStatement = null;

    try {
      sqlConnection = quickJDBC.getConnection();
      chunkInsertBatchStatement = sqlConnection
	  .prepareStatement(SqlQueryBuilder.insertTextChunk);
      textProvenanceInsertBatchStatement = sqlConnection
	  .prepareStatement(SqlQueryBuilder.insertOpenIERelationProvenance);
      documentInsertBatchStatement = sqlConnection.prepareStatement(
	  SqlQueryBuilder.insertSourceDocument);

      String openIERelationId = UUID.randomUUID().toString();

      //there should be at least one provenance for the OpenIERelation
      if (openIERelationInsertionBuilder.getProvenances() == null || openIERelationInsertionBuilder
	  .getProvenances().size() == 0) {
	throw new KBUpdateException(
	    "OpenIERelation should have at least one provenance. Cannot complete insertion for "
		+ "OpenIERelation");
      }

      PreparedStatement relationInsertionStmt = SqlQueryBuilder
	  .createOpenIERelationInsertionStatement(openIERelationInsertionBuilder,
	      openIERelationId,sqlConnection);
      relationInsertionStmt.executeUpdate();
      sqlConnection.commit();

      // insert the provenances
      for (KBProvenance.InsertionBuilder justificationOrContext : openIERelationInsertionBuilder
	  .getProvenances()) {
	KBTextProvenance.InsertionBuilder textProvenance = (KBTextProvenance.InsertionBuilder)
	    justificationOrContext;

	insertTextProvenance(openIERelationId, textProvenance,
	      chunkInsertBatchStatement,
	      textProvenanceInsertBatchStatement,
	      documentInsertBatchStatement,
	      sqlConnection);
      }

      documentInsertBatchStatement.executeBatch();
      chunkInsertBatchStatement.executeBatch();
      textProvenanceInsertBatchStatement.executeBatch();

      sqlConnection.commit();

      openIERelationInsertionBuilder
	  .setKBID(new KBID(openIERelationId,
	      KBOntologyModel.DATA_INSTANCES_PREFIX));
    } catch (Exception e) {
      throw new KBUpdateException("Failed to insert openIE relation: "+e.getMessage(), e);
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
	if (documentInsertBatchStatement != null)
	  documentInsertBatchStatement.close();
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
		PreparedStatement documentInsertBatchStatement = null;
		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
			documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

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
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
						sqlConnection);
			}

			dateInsertionBuilder.addProvenances(existingProvenances);

			documentInsertBatchStatement.executeBatch();
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

			dateInsertionBuilder.setKBID(
			    new KBID(dateId, KBOntologyModel.DATA_INSTANCES_PREFIX));
		} catch (Exception e) {
			throw new KBUpdateException("Unable to insert timex value: "+e.getMessage(), e);
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
				if (documentInsertBatchStatement != null)
					documentInsertBatchStatement.close();
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
	 * @param documentInsertBatchStatement
	 *
	 */
	private void insertChunk(KBTextProvenance.InsertionBuilder provenance,
			PreparedStatement chunkInsertBatchStatement, PreparedStatement documentInsertBatchStatement, Connection sqlConnection)
			throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException {
		if (provenance.getDocumentID() != null)
			insertSourceDocument(provenance, documentInsertBatchStatement, sqlConnection);

		// insert chunk into SQL DB
		SqlQueryBuilder.addTextChunkInsertQueryToBatch(provenance.getChunkId(),
		    provenance.getValue(), provenance.getBeginOffset(),
		    provenance.getEndOffset(),
		    (provenance.getDocumentID() != null ? provenance.getDocumentID() : null),
		    chunkInsertBatchStatement);
	}

	/**
	 * Insert source document into the SQL DB
	 * @param documentInsertBatchStatement
	 *
	 */
	private void insertSourceDocument(KBTextProvenance.InsertionBuilder provenance,
			PreparedStatement documentInsertBatchStatement, Connection sqlConnection) throws SQLException {
		if (provenance.getCorpusID() != null) {
			insertCorpus(provenance.getCorpusID(), provenance.getCorpusType(),
			    provenance.getCorpusName(), provenance.getCorpusURI(), sqlConnection);
		}

		SqlQueryBuilder.addDocumentInsertQueryToBatch(documentInsertBatchStatement,
		    provenance.getDocumentID(),
		    provenance.getDocumentURI(),
		    provenance.getSourceLanguage(),
		    provenance.getCorpusID(),
		    provenance.getDocumentPublicationDate() == null ? null : java.sql.Date
			.valueOf(provenance.getDocumentPublicationDate()), sqlConnection);
	}

	/**
	 *
	 * Insert corpus in the SQL DB.
	 */
	private void insertCorpus(String corpusId, String corpusType, String corpusName,
			String corpusUri, Connection sqlConnection) throws SQLException {
		// Set auto commit to true for corpus insertion
		//  because if a transaction has a failed query then
		//  the entire transaction is treated as a failed statement.
		sqlConnection.setAutoCommit(true);
		int tries = 0;
		boolean isSuccessful = false;
		while (!isSuccessful) {
			try {
				if (!insertedCorpora.containsKey(corpusId)) {
					if (!quickJDBC.recordExists(SqlQueryBuilder.doesCorpusExistQuery
							(corpusId, sqlConnection))) {
						quickJDBC.executeSqlUpdate(
								SqlQueryBuilder.createCorpusInsertQuery(
										corpusId,
										corpusType, corpusName,
										corpusUri, sqlConnection));
					}
					insertedCorpora.put(corpusId, true);
				}
				isSuccessful = true;
			} catch (PSQLException ex) {
				if (tries <= 3) {
					System.err.println("Failed to insert corpus " +
							corpusName + " attempting try #"+(tries+1));
					try { Thread.sleep(5000); } catch (InterruptedException
							e) {}
					tries++;
				} else {
					System.err.println("Failed to insert corpus after "+
							(tries+1)
							+" tries. Rethrowing exception.");
					throw ex;
				}
			}
		}
		sqlConnection.setAutoCommit(false);
	}

	/**
	 * Insert source algorithm in the SQL DB
	 *
	 */
	private void insertSourceAlgorithm(String sourceAlgorithmName, String contributingSiteName,
			Connection sqlConnection) throws SQLException, PSQLException {
		// Set auto commit to true for source alg insertion
		//  because if a transaction has a failed query then
		//  the entire transaction is treated as a failed statement.
		sqlConnection.setAutoCommit(true);
		int tries = 0;
		boolean isSuccessful = false;
		while (!isSuccessful) {
			try {
				if (!insertedSourceAlgorithms.containsKey(sourceAlgorithmName)) {
					if (!quickJDBC.recordExists(SqlQueryBuilder.doesSourceAlgorithmExistQuery(
							sourceAlgorithmName, sqlConnection))) {
						quickJDBC.executeSqlUpdate(SqlQueryBuilder.createSourceAlgorithmInsertQuery(
							sourceAlgorithmName, contributingSiteName, sqlConnection));
					}
					insertedSourceAlgorithms.put(sourceAlgorithmName, true);
				}
				isSuccessful = true;
			} catch (PSQLException ex) {
				if (tries <= 3) {
					System.err.println("Failed to insert source algorithm " + sourceAlgorithmName + " attempting try #"+(tries+1));
					try { Thread.sleep(5000); } catch (InterruptedException
							e) {}
					tries++;
				} else {
					System.err.println("Failed to insert source algorithm after "+(tries+1)+" tries. Rethrowing exception.");
					throw ex;
				}
			}
		}
		sqlConnection.setAutoCommit(false);
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
		PreparedStatement documentInsertBatchStatement = null;
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
			documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

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
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
							sqlConnection);
				}

				argument.setKBID(new KBID(argumentStmtId, KBOntologyModel.DATA_INSTANCES_PREFIX));
			}

			for (KBProvenance.InsertionBuilder provenance : insertionBuilder.getProvenances()) {
				insertTextProvenance(relationId, (KBTextProvenance.InsertionBuilder) provenance,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
						sqlConnection);
			}

			documentInsertBatchStatement.executeBatch();
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
			throw new KBUpdateException("Failed to insert relation: "+e.getMessage(), e);
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
				if (documentInsertBatchStatement != null)
					documentInsertBatchStatement.close();
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
			PreparedStatement textProvenanceInsertBatchStatement, PreparedStatement documentInsertBatchStatement, Connection sqlConnection)
			throws SQLException, UnsupportedEncodingException, MalformedURLException, IOException, PSQLException {
		// insert chunk
		insertChunk(provenance, chunkInsertBatchStatement, documentInsertBatchStatement, sqlConnection);

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

  private void updateTextProvenance(KBID provenanceKbId, KBID newSourceUri,
      		PreparedStatement textProvenanceUpdateBatchStatement) throws SQLException {
    		SqlQueryBuilder.addTextProvenanceUpdateQueryToBatch(provenanceKbId.getObjectID(),
		    newSourceUri.getObjectID(), textProvenanceUpdateBatchStatement);
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
	  	PreparedStatement textProvenanceUpdateBatchStatement = null;
		PreparedStatement textProvenanceDeleteBatchStatement = null;
		PreparedStatement documentInsertBatchStatement = null;

		try {
			sqlConnection = quickJDBC.getConnection();
			chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
			textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  	textProvenanceUpdateBatchStatement = sqlConnection.prepareStatement
			    (SqlQueryBuilder.updateTextProvenance);
			textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);
			documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

			// check if KB object to be updated exists in KB.
			if (!doesKBObjectExist(kbUri)) {
				throw new KBUpdateException("Entity to be updated does not exist in the KB.");
			}

			// insert new canonicalmention if it exists
			if (entityUpdateBuilder.getNewCanonicalMention() != null) {
				insertTextProvenance(kbUri, entityUpdateBuilder.getNewCanonicalMention(),
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
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
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
							sqlConnection);
				} else {
					mention.kbid = entityUpdateBuilder.getNewCanonicalMention().kbid;
				}
			}

			for (KBProvenance mentionToRemove : entityUpdateBuilder.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

		  	for (KBProvenance.UpdateBuilder mentionToUpdate : entityUpdateBuilder
			    .getProvenancesToUpdate()) {
			  	log.info("Updating mention {}...",mentionToUpdate.getKBID().getObjectID());
			  	log.info("...with KBId: {}",((KBTextProvenance.UpdateBuilder) mentionToUpdate)
				    .getSourceEntityKBID().getObjectID());
		    		updateTextProvenance(mentionToUpdate.getKBID(),
				    ((KBTextProvenance.UpdateBuilder) mentionToUpdate)
					.getSourceEntityKBID(),
				    textProvenanceUpdateBatchStatement);
		  	}

			documentInsertBatchStatement.executeBatch();
			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
		  	textProvenanceUpdateBatchStatement.executeBatch();
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
			throw new KBUpdateException("Entity update failed: "+e.getMessage(), e);
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
		    		if (textProvenanceUpdateBatchStatement != null)
		      			textProvenanceUpdateBatchStatement.close();
			} catch (Exception e) {
		  	}
		  	;
			try {
				if (documentInsertBatchStatement != null)
					documentInsertBatchStatement.close();
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

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
		  PreparedStatement textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  PreparedStatement textProvenanceUpdateBatchStatement = sqlConnection.prepareStatement
			    (SqlQueryBuilder.updateTextProvenance);
		  PreparedStatement textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);
		  PreparedStatement documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

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
							chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
							sqlConnection);
				}

				argument.setKBID(new KBID(argumentStmtId, KBOntologyModel.DATA_INSTANCES_PREFIX));
			}

			for (KBProvenance.InsertionBuilder mention : relationUpdateBuilder.getNewProvenances()) {
				insertTextProvenance(kbUri,
				    (KBTextProvenance.InsertionBuilder) mention,
				    chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
				    documentInsertBatchStatement,
				    sqlConnection);
			}

		  	for (KBProvenance.UpdateBuilder mention : relationUpdateBuilder.getProvenancesToUpdate()) {
		    		updateTextProvenance(mention.getKBID(),
				    ((KBTextProvenance.UpdateBuilder)mention).getSourceEntityKBID(),
				    textProvenanceUpdateBatchStatement);
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

            documentInsertBatchStatement.executeBatch();
			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
		  	textProvenanceUpdateBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();


			UpdateProcessor upp = sparqlService.getUpdateProcessor(relationUpdateRequest);
			upp.execute();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Relation update failed: "+e.getMessage(), e);
		}
	}

	protected void updateRelationArgument(
			KBRelationArgument.UpdateBuilder relationArgumentUpdateBuilder)
			throws KBUpdateException {
		String kbUri = relationArgumentUpdateBuilder.getKBID().getObjectID();

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
		  PreparedStatement textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  PreparedStatement textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);
		  PreparedStatement documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

			UpdateRequest relationArgumentUpdateRequest = UpdateFactory.create();

			// check if KB object to be updated exists in KB.
			if (!doesKBObjectExist(kbUri)) {
				throw new KBUpdateException("Relation to be updated does not exist in the KB.");
			}

			for (KBProvenance.InsertionBuilder mention : relationArgumentUpdateBuilder
					.getNewProvenances()) {
				insertTextProvenance(kbUri, (KBTextProvenance.InsertionBuilder) mention,
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
						sqlConnection);
			}

			for (KBProvenance mentionToRemove : relationArgumentUpdateBuilder
					.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(),
				    textProvenanceDeleteBatchStatement);
			}

			if (relationArgumentUpdateBuilder.getNewConfidence() != null) {
				UpdateRequest confidenceUpdateRequest = sparqlQueryBuilder
						.createRelationConfidenceUpdateQuery(kbUri,
						    relationArgumentUpdateBuilder
							.getNewConfidence());
				for (Update update : confidenceUpdateRequest.getOperations()) {
					relationArgumentUpdateRequest.add(update);
				}
			}

			documentInsertBatchStatement.executeBatch();
			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			UpdateProcessor upp = sparqlService.getUpdateProcessor(relationArgumentUpdateRequest);
			upp.execute();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Relation argument update failed: "+e.getMessage(), e);
		}
	}

	protected void updateKBPredicateArgumentProvenances(
			KBPredicateArgument.UpdateBuilder<?, ?> updateBuilder) throws KBUpdateException {
		String kbUri = updateBuilder.getKBID().getObjectID();

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
		  PreparedStatement textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  PreparedStatement textProvenanceDeleteBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.deleteTextProvenance);
		  PreparedStatement documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

			for (KBProvenance.InsertionBuilder mention : updateBuilder.getNewProvenances()) {
				insertTextProvenance(kbUri,
				    (KBTextProvenance.InsertionBuilder) mention,
				    chunkInsertBatchStatement, textProvenanceInsertBatchStatement,
				    documentInsertBatchStatement,
				    sqlConnection);
			}

			for (KBProvenance mentionToRemove : updateBuilder.getProvenancesToRemove()) {
				deleteTextProvenance(mentionToRemove.getKBID(), textProvenanceDeleteBatchStatement);
			}

			documentInsertBatchStatement.executeBatch();
			chunkInsertBatchStatement.executeBatch();
			textProvenanceInsertBatchStatement.executeBatch();
			textProvenanceDeleteBatchStatement.executeBatch();

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("KBPredicateArgument update failed: "+e.getMessage(), e);
		}
	}

	/**
	 * Delete a first class KB object and all associated metadata.
	 *
	 * @return boolean value indicating success or failure
	 */
	public boolean deleteKBObject(KBID kbId) throws
									   KBUpdateException {

		try (Connection sqlConnection =  quickJDBC.getConnection()) {
			return deleteKBObject(kbId, sqlConnection, true);
		} catch (SQLException ex) {
			throw new KBUpdateException(
					"KB object deletion failed. Unable to get connection to SQL DB", ex);
		}
	}

  /**
   * Delete a first class KB object without deleting its metadata. This method should be used to
   * delete a duplicate KB object which has been "merged" with another object.
   *
   * @return boolean value indicating success or failure
   */
  public boolean deleteDuplicateKBObject(KBID kbId) throws
									   KBUpdateException {

    try (Connection sqlConnection = quickJDBC.getConnection() ) {
      return deleteKBObject(kbId, sqlConnection, false);
    } catch (SQLException ex) {
      throw new KBUpdateException(
	  "KB object deletion failed. Unable to get connection to SQL DB", ex);
    }
  }

	private boolean deleteKBObject(KBID kbId, Connection sqlConnection, boolean
	    deleteTextProvenances) throws
									     KBUpdateException {

		String kbUri = kbId.getObjectID();

		try {
		  	if(deleteTextProvenances) {
			  // Else, delete all mentions belonging to entity in the metadata DB
			  deleteTextProvenancesForKBObject(kbUri, sqlConnection);
			}

			// delete external KB ID maps
			deleteAllExternalKBIdMapsForElement(kbUri);

			deleteTriplesInTripleStore(kbUri);

			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("KB object deletion failed: "+e.getMessage(), e);
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

		try (Connection sqlConnection = quickJDBC.getConnection()) {
			removeOrphanMetaData(sqlConnection);
			sqlConnection.commit();
		} catch (SQLException ex) {
			throw new KBUpdateException("Removing orphaned metadata failed: "+ex.getMessage(),
					ex);
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

		// Delete any TextChunk orphans
		//   TextChunks may have been used by multiple TextProvenances
		PreparedStatement chunkPreparedStmt = SqlQueryBuilder
				.createDeleteOrphanTextChunksQuery(sqlConnection);
		quickJDBC.executeSqlUpdate(chunkPreparedStmt);
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
		try (PreparedStatement preparedStmt = SqlQueryBuilder.createTextProvenanceDeleteQuery(objectUri,
				sqlConnection)) {
		  quickJDBC.executeSqlUpdate(preparedStmt);
		}
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
		} finally {
		  if (null != qexec) {
		    qexec.close();
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
			throws KBUpdateException {
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
        StringBuilder entityTypeList = new StringBuilder();

				if (possibleEntityTypes != null) {
					for (OntType type : entity.getTypes().keySet()) {
						String entityType = type.getType();
						entityTypeList.append(entityType).append(", ");
						if (possibleEntityTypes.contains(entityType)) {
							doesCorrectEntityTypeExist = true;
							break;
						}
					}
					if (entityTypeList.length() > 2) {
					  entityTypeList.setLength(entityTypeList.length() - 2);
					}
					if (!doesCorrectEntityTypeExist) {
						throw new KBUpdateException("Argument type " + argumentType
								+ "'s entity is incorrect type for relation "
								+ relationType
								+ " (Expected one of "
								+ possibleEntityTypes.toString() + " Actual: "
								+ entityTypeList.toString() + ")");
					}
				}
			}

			expectedArgumentOccurences.put(argumentType,
					expectedArgumentOccurences.get(argumentType) - 1);
		}

		// Only check for presence of all arguments if relation is not an event.
		if (!kbOntologyModel.getLeafEventTypes().contains(relationType)
				&& !relationType.equals("TemporalSpan")) {
		  for (Map.Entry<String, Integer> entry : expectedArgumentOccurences.entrySet()) {
				if (entry.getValue() > 0) {
					throw new KBUpdateException("Did not find enough occurences of argument "
							+ entry.getKey() + " in relation.");
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

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
		  PreparedStatement textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  PreparedStatement documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

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
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
						sqlConnection);
			}

			numberInsertionBuilder.addProvenances(existingProvenances);

			documentInsertBatchStatement.executeBatch();
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
			throw new KBUpdateException("Failed to insert number: "+e.getMessage(), e);
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

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement statement = sqlConnection.prepareStatement(SqlQueryBuilder.insertDocumentText);
			statement.setString(1, documentID);
			statement.setString(2, corpusID);
			statement.setString(3, text);
			statement.execute();
			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Failed to insert document text: "+e.getMessage(), e);
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

		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement statement = sqlConnection.prepareStatement(SqlQueryBuilder.deleteDocumentText);
			statement.setString(1, documentID);
			statement.setString(2, corpusID);
			statement.execute();
			sqlConnection.commit();
		} catch (Exception e) {
			throw new KBUpdateException("Failed to delete document text: "+e.getMessage(), e);
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
		try (Connection sqlConnection = quickJDBC.getConnection()) {
		  PreparedStatement chunkInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextChunk);
		  PreparedStatement textProvenanceInsertBatchStatement = sqlConnection
					.prepareStatement(SqlQueryBuilder.insertTextProvenance);
		  PreparedStatement documentInsertBatchStatement = sqlConnection.prepareStatement(SqlQueryBuilder.insertSourceDocument);

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
						chunkInsertBatchStatement, textProvenanceInsertBatchStatement, documentInsertBatchStatement,
						sqlConnection);
			}

			insertionBuilder.addProvenances(existingProvenances);

			documentInsertBatchStatement.executeBatch();
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
			throw new KBUpdateException("Failed to insert Generic Thing: "+e.getMessage(), e);
		}
	}

	public ConnectionStatistics getSQLConnectionStatistics(){
		return quickJDBC.getConnectionStatistics();
	}
}

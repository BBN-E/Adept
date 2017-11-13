/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

package adept.kbapi.unittests;

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

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.update.GraphStoreFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Random;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentEvent;
import adept.common.DocumentEventArgument;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.EventMention;
import adept.common.EventMentionArgument;
import adept.common.EventText;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.RelationMention;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.io.Reader;
import adept.kbapi.KB;
import adept.kbapi.KBBelief;
import adept.kbapi.KBDate;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEntityMentionProvenance;
import adept.kbapi.KBEvent;
import adept.kbapi.KBGenericThing;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBParameters;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBSentiment;
import adept.kbapi.KBTemporalSpan;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
import adept.kbapi.sql.QuickJDBC;
import adept.kbapi.sql.SqlQueryBuilder;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class KBUnitTest {
	private final Random rand = new Random();
	private String schemaName;
	private QuickJDBC quickJDBC;
	private Connection sqlConnection;
	private LocalSPARQLService sparqlService;

	protected KBParameters unitTestKBParameters;
	protected TokenStream testTokenStream;
	protected TokenOffset testTokenOffset;
	protected KB kb;

	private static final String packagePath = new Object() {
	}.getClass().getPackage().getName().replace(".", "/");
	private static final String kbUnitTestingParamsFile = packagePath + "/KBUnitTestParameters.xml";
	private static final String sqlSchemaFile = "/adept/utilities/DEFT KB create schema.txt";

	protected final String defaultEntityType = "org";
	protected final String defaultEntityMentionType = "NAME";
	protected final float defaultEntityConfidence = 0.8f;
	protected final float defaultEntityMentionConfidence = 0.9f;
	protected final float defaultEntityCanonicalMentionConfidence = 0.63f;
	protected final List<Pair<String, Double>> defaultEntityTypesWithConfidences;

	protected final String defaultRelationType = "per:cities_of_residence";
	protected final float defaultRelationConfidence = 0.9f;
	protected final float defaultRelationMentionConfidence = 0.9f;

	private int provenanceNumber = 1;

	public KBUnitTest() {
		defaultEntityTypesWithConfidences = new ArrayList<Pair<String, Double>>();
		defaultEntityTypesWithConfidences.add(new Pair<String, Double>("per", 0.22));
		defaultEntityTypesWithConfidences.add(new Pair<String, Double>("loc", 0.33));
	}

	@Before
	public void setUp() {
		initializeDataStores();
		initializeTest();
	}

	@After
	public void tearDown() {
		try {
			kb.close();
		} catch (KBUpdateException e) {
			e.printStackTrace();
		}
		deconstructDataStores();
	}

	protected void initializeDataStores() {
		try {
			schemaName = "unittest" + rand.nextInt(Integer.MAX_VALUE);

			Model model = ModelFactory.createRDFSModel(ModelFactory.createDefaultModel());
			model.read(getClass().getResourceAsStream("/adept/ontology/adept-base.ttl"), "", "TTL");
			model.read(getClass().getResourceAsStream("/adept/ontology/adept-core.ttl"), "", "TTL");
			sparqlService = new LocalSPARQLService(GraphStoreFactory.create(model));

			unitTestKBParameters = new KBParameters(kbUnitTestingParamsFile);
			quickJDBC = new QuickJDBC(unitTestKBParameters);
			sqlConnection = quickJDBC.getConnection();
			sqlConnection.setAutoCommit(true);
			createUnitTestSqlSchema(schemaName);
			sqlConnection.setAutoCommit(false);

			// Build schema
			String schema = new String(Files.readAllBytes(Paths.get(getClass().getResource(
					sqlSchemaFile).toURI())));
			PreparedStatement createSchema = sqlConnection.prepareStatement(schema);
			createSchema.executeUpdate();
			createSchema.close();
			sqlConnection.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to initialize data stores for unit tests. " + ex.getMessage());
		}
	}

	protected void deconstructDataStores() {
		try {
			dropUnitTestSqlSchema(schemaName);
			sqlConnection.close();
			quickJDBC.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to deconstruct data stores for unit tests. " + ex.getMessage());
		}
	}

	protected void initializeTest() {
		try {
			kb = new KB(unitTestKBParameters, schemaName, sparqlService);

			HltContentContainer hltContentContainer = new HltContentContainer();
			Document document = DocumentMaker.getInstance().createDocument("sample.txt",
					null, "Text", "sample_entity_1.txt", "English",
					Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample.txt"),
					hltContentContainer);
			CharOffset testCharOff = new CharOffset(0, 16);
			String testText = "BBN Technologies";
			Token testToken = new Token(0, testCharOff, testText);
			testTokenStream = new TokenStream(TokenizerType.WHITESPACE, TranscriptType.SOURCE,
					"English", ChannelName.NONE, ContentType.TEXT, document);
			testTokenOffset = new TokenOffset(0, 0);
			testTokenStream.add(testToken);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to initialize test. " + ex.getMessage());
		}
	}

	protected KBEntity insertDefaultTestEntity() throws KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		if (kb != null) {
			Pair<Entity, List<EntityMention>> entityWithMentions = createTestEntityWithMentions(
					defaultEntityType, defaultEntityConfidence, defaultEntityMentionType,
					defaultEntityMentionConfidence, defaultEntityCanonicalMentionConfidence,
					defaultEntityTypesWithConfidences);
			KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
					entityWithMentions.getL(), entityWithMentions.getR(),
					KBOntologyMap.getTACOntologyMap());
			return insertionBuilder.insert(kb);
		}
		return null;
	}

	protected Chunk createTestChunk() {
		try {
			HltContentContainer hltContentContainer = new HltContentContainer();
			Document document = DocumentMaker.getInstance().createDocument("sample.txt",
					null, "Text", "sample_entity_1.txt", "English",
					Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample.txt"),
					hltContentContainer);
			CharOffset testCharOff = new CharOffset(0, 16);
			String testText = "BBN Technologies";
			Token testToken = new Token(0, testCharOff, testText);
			TokenStream tokenStream = new TokenStream(TokenizerType.WHITESPACE,
					TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
			TokenOffset tokenOffset = new TokenOffset(0, 0);
			tokenStream.add(testToken);
			return new Chunk(tokenOffset, tokenStream);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to create test chunk. " + ex.getMessage());
		}
		return null;
	}

	protected String getChunkIDByProvenance(KBProvenance provenance) {
		try {
			PreparedStatement preparedStatement = SqlQueryBuilder.createGetChunkIDByProvenance(provenance, sqlConnection);
			ResultSet result = preparedStatement.executeQuery();
			if (result.next()) {
				return result.getString("chunk");
			} else {
				throw new Exception("Provenance KBID not found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to get Chunk for Provenance " + provenance.getKBID().getObjectID() + ": " + ex.getMessage());
		}
		return null;
	}

	protected List<String> getOrphanChunkIDs() {
		List<String> orphanChunks = new ArrayList<String>();
		try {
			PreparedStatement preparedStatement = SqlQueryBuilder.createGetOrphanTextChunksQuery(sqlConnection);
			ResultSet result = preparedStatement.executeQuery();
			while (result.next()) {
				orphanChunks.add(result.getString("ID"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to get orphan Chunks: " + ex.getMessage());
		}
		return orphanChunks;
	}

	protected Pair<Entity, List<EntityMention>> createTestEntityWithMentions(String entityType,
			float entityConfidence, String entityMentionType, float entityMentionConfidence,
			float entityCanonicalMentionConfidence,
			List<Pair<String, Double>> entityTypesWithConfidences) {
		try {
			// create entity
			Entity entity = new Entity(1, new Type(entityType));

			// create mention
			List<EntityMention> mentions = new ArrayList<EntityMention>();
			EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream);
			mention.addEntityConfidencePair(entity.getEntityId(), entityMentionConfidence);
			mention.setMentionType(new Type(entityMentionType));
			mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
			mention.setEntityType(new Type(entityType));
			mentions.add(mention);

			// create a second mention
			EntityMention otherMention = new EntityMention(0, testTokenOffset, testTokenStream);
			otherMention.addEntityConfidencePair(entity.getEntityId(), 0.3f);
			otherMention.setMentionType(new Type(entityMentionType));
			otherMention.setSourceAlgorithm(new SourceAlgorithm("Example2", "BBN"));
			otherMention.setEntityType(new Type(entityType));
			mentions.add(otherMention);

			// set canonical mention
			entity.setCanonicalMentions(mention);
			entity.setEntityConfidence(entityConfidence);
			entity.setCanonicalMentionConfidence(entityCanonicalMentionConfidence);

			for (Pair<String, Double> entityTypeWithConfidence : entityTypesWithConfidences) {
				entity.addType(new Type(entityTypeWithConfidence.getL()),
						entityTypeWithConfidence.getR());
			}

			return new Pair<Entity, List<EntityMention>>(entity, mentions);
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Unable to create entity object. " + ex.getMessage());
		}

		return null;
	}

	protected KBRelation insertDefaultTestRelation(KBEntity kbEntity1, KBEntity kbEntity2)
			throws KBUpdateException, InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		if (kb != null) {
			Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
					kbEntity1, kbEntity2, defaultRelationType, defaultRelationConfidence,
					defaultRelationMentionConfidence);

			KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
					relationWithEntityMap.getL(), relationWithEntityMap.getR(),
					KBOntologyMap.getTACOntologyMap());
			return insertionBuilder.insert(kb);
		}
		return null;
	}

	protected Pair<DocumentEvent, HashMap<Item, KBPredicateArgument>> createTestEventWithEntityMap(
			KBEntity kbEntity1, KBEntity kbEntity2, String eventTypeString, float eventConfidence,
			float eventMentionConfidence) {
		// entity and entity mention creation
		Entity locationentity = new Entity(1, new Type("loc"));
		Entity personentity = new Entity(2, new Type("per"));

		Type eventType = new Type(eventTypeString);
		Type personRole = new Type("person");
		Type locationRole = new Type("place");

		// create mention
		EntityMention locationmention = new EntityMention(0, testTokenOffset, testTokenStream);
		locationmention.addEntityConfidencePair(locationentity.getEntityId(), 0.9f);
		locationmention.setMentionType(new Type("NAME"));
		locationmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		EntityMention personmention = new EntityMention(1, testTokenOffset, testTokenStream);
		personmention.addEntityConfidencePair(personentity.getEntityId(), 0.9f);
		personmention.setMentionType(new Type("NAME"));
		personmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		// TimePhrase timePhrase = new TimePhrase(testTokenOffset,
		// testTokenStream, null);

		// set canonical mention and confidences on entities
		locationentity.setCanonicalMentions(locationmention);
		locationentity.setEntityConfidence(0.8f);
		locationentity.setCanonicalMentionConfidence(0.63);

		personentity.setCanonicalMentions(personmention);
		personentity.setEntityConfidence(0.87);
		personentity.setCanonicalMentionConfidence(0.1);

		// create relation mention
		EventMention.Builder eventMentionBuilder = EventMention.builder(eventType);
		Chunk mentionTextChunk = new Chunk(testTokenOffset, testTokenStream);
		eventMentionBuilder.setScore(eventMentionConfidence);
		eventMentionBuilder.setProvenance(EventText.builder(eventType, mentionTextChunk)
				.setScore(.23f).build());
		EventMentionArgument mentionArg1 = EventMentionArgument
				.builder(eventType, personRole, new Chunk(testTokenOffset, testTokenStream))
				.setScore(.32f).build();
		eventMentionBuilder.addArgument(mentionArg1);
		EventMentionArgument mentionArg2 = EventMentionArgument
				.builder(eventType, locationRole, new Chunk(testTokenOffset, testTokenStream))
				.setScore(.30f).build();
		eventMentionBuilder.addArgument(mentionArg2);

		EventMention eventMention = eventMentionBuilder.build();

		// create document relation
		DocumentEvent.Builder documentEventBuilder = DocumentEvent.builder(eventType);
		documentEventBuilder.addProvenanceFromEventMention(eventMention);
		documentEventBuilder.setScore(eventConfidence);
		DocumentEventArgument.Builder arg1Builder = DocumentEventArgument.builder(eventType,
				personRole, DocumentEventArgument.Filler.fromEntity(personentity)).setScore(0.51f);
		arg1Builder.addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(
				eventMention, mentionArg1));
		documentEventBuilder.addArgument(arg1Builder.build());

		DocumentEventArgument.Builder arg2Builder = DocumentEventArgument.builder(eventType,
				locationRole, DocumentEventArgument.Filler.fromEntity(locationentity)).setScore(
				0.52f);
		arg2Builder.addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(
				eventMention, mentionArg2));
		documentEventBuilder.addArgument(arg2Builder.build());

		documentEventBuilder.addAttribute(new Type("GENERIC"), .2f);

		// map from document entities to Adept KB entities
		HashMap<Item, KBPredicateArgument> entityMap = new HashMap<Item, KBPredicateArgument>();
		entityMap.put(personentity, kbEntity1);
		entityMap.put(locationentity, kbEntity2);

		DocumentEvent documentEvent = documentEventBuilder.build();

		return new Pair<DocumentEvent, HashMap<Item, KBPredicateArgument>>(documentEvent, entityMap);
	}

	protected Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> createTestRelationWithEntityMap(
			KBEntity kbEntity1, KBEntity kbEntity2, String relationType, float relationConfidence,
			float relationMentionConfidence) {

		// entity and entity mention creation
		Entity locationentity = new Entity(1, new Type("Location"));
		Entity personentity = new Entity(2, new Type("Person"));

		// create mention
		EntityMention locationmention = new EntityMention(0, testTokenOffset, testTokenStream);
		locationmention.addEntityConfidencePair(locationentity.getEntityId(), 0.9f);
		locationmention.setMentionType(new Type("NAME"));
		locationmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		EntityMention personmention = new EntityMention(1, testTokenOffset, testTokenStream);
		personmention.addEntityConfidencePair(personentity.getEntityId(), 0.9f);
		personmention.setMentionType(new Type("NAME"));
		personmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		// set canonical mention and confidences on entities
		locationentity.setCanonicalMentions(locationmention);
		locationentity.setEntityConfidence(0.8f);
		locationentity.setCanonicalMentionConfidence(0.63);

		personentity.setCanonicalMentions(personmention);
		personentity.setEntityConfidence(0.87);
		personentity.setCanonicalMentionConfidence(0.1);

		// create relation mention
		RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type(
				relationType));
		relationMentionBuilder.setConfidence(relationMentionConfidence);
		relationMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));
		RelationMention.Filler argFiller1 = RelationMention.Filler.fromEntityMention(personmention,
				new Type("arg-1"), 0.51f);
		relationMentionBuilder.addArgument(argFiller1);
		RelationMention.Filler argFiller2 = RelationMention.Filler.fromEntityMention(
				locationmention, new Type("arg-2"), 0.52f);
		relationMentionBuilder.addArgument(argFiller2);

		List<RelationMention> provenances = new ArrayList<RelationMention>();
		provenances.add(relationMentionBuilder.build());

		// create document relation
		DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(new Type(
				relationType));
		documentRelationBuilder.addProvenances(provenances);
		documentRelationBuilder.setConfidence(relationConfidence);
		DocumentRelationArgument.Builder arg1Builder = DocumentRelationArgument.builder(new Type(
				"arg-1"), DocumentRelationArgument.Filler.fromEntity(personentity), 0.51f);
		arg1Builder.addProvenance(argFiller1);
		documentRelationBuilder.addArgument(arg1Builder.build());
		DocumentRelationArgument.Builder arg2Builder = DocumentRelationArgument.builder(new Type(
				"arg-2"), DocumentRelationArgument.Filler.fromEntity(locationentity), 0.52f);
		arg2Builder.addProvenance(argFiller2);
		documentRelationBuilder.addArgument(arg2Builder.build());

		// map from document entities to Adept KB entities
		HashMap<Item, KBPredicateArgument> entityMap = new HashMap<Item, KBPredicateArgument>();
		entityMap.put(personentity, kbEntity1);
		entityMap.put(locationentity, kbEntity2);

		DocumentRelation documentRelation = documentRelationBuilder.build();

		return new Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>>(documentRelation,
				entityMap);
	}

	private void createUnitTestSqlSchema(String schemaName) {
		try {
			PreparedStatement createSchemaPreparedStmt = sqlConnection
					.prepareStatement("CREATE SCHEMA " + schemaName);
			createSchemaPreparedStmt.executeUpdate();
			createSchemaPreparedStmt.close();

			PreparedStatement setSearchPathPreparedStmt = sqlConnection
					.prepareStatement("SET search_path TO " + schemaName);
			setSearchPathPreparedStmt.executeUpdate();
			setSearchPathPreparedStmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			Assert.fail("Unable to create unit test sql schema. " + ex.getMessage());
		}
	}

	protected void checkObjectConfidence(KBPredicateArgument object, float confidence) {
		Model model = sparqlService.getDefaultGraph();
		String objectURI = object.getKBID().getKBNamespace() + object.getKBID().getObjectID();
		NodeIterator confidences = model.listObjectsOfProperty(model.createResource(objectURI),
				model.createProperty(KBOntologyModel.ONTOLOGY_BASE_PREFIX + "confidence"));

		Assert.assertTrue("No confidence present for KBID: " + objectURI, confidences.hasNext());
		RDFNode node = confidences.next();
		Assert.assertTrue("Confidence in graph is not a literal", node.isLiteral());
		Assert.assertEquals("Confidence did not match expected confidence", confidence, node
				.asLiteral().getFloat(), 0.0001f);
		Assert.assertFalse("More than one confidence was present for KBID: " + objectURI,
				confidences.hasNext());
	}

	/**
	 * Query the given {@code KBPredicateArgument} from the KB by its KBID.
	 * Then, verify equality and equal hashCode()s for the given and queried
	 * objects using {@code Assert.assertEquals()}.
	 *
	 * @param kbpa
	 */
	public void assertEqualsAndHashCodeByQueryByKBID(KBPredicateArgument kbpa)
			throws KBQueryException {
		assertNotNull("Given KB object is null.", kbpa);
		Class<?> kbpaClass = kbpa.getClass();
		KBPredicateArgument queriedKbpa;
		if (kbpaClass == KBDate.class) {
			queriedKbpa = kb.getKBDateByDateId(kbpa.getKBID());
		} else if (kbpaClass == KBEntity.class) {
			queriedKbpa = kb.getEntityById(kbpa.getKBID());
		} else if (kbpaClass == KBGenericThing.class) {
			queriedKbpa = kb.getGenericThingByID(kbpa.getKBID());
		} else if (kbpaClass == KBNumber.class) {
			queriedKbpa = kb.getNumberValueByID(kbpa.getKBID());
		} else if (kbpaClass == KBRelation.class) {
			queriedKbpa = kb.getRelationById(kbpa.getKBID());
		} else if (kbpaClass == KBTemporalSpan.class) {
			queriedKbpa = kb.getTemporalSpanByID(kbpa.getKBID());
		} else if (kbpaClass == KBBelief.class) {
			queriedKbpa = kb.getBeliefById(kbpa.getKBID());
		} else if (kbpaClass == KBEvent.class) {
			queriedKbpa = kb.getEventById(kbpa.getKBID());
		} else if (kbpaClass == KBSentiment.class) {
			queriedKbpa = kb.getSentimentById(kbpa.getKBID());
		} else {
			throw new IllegalArgumentException("Unrecognized KBPredicateArgument subclass "
					+ kbpaClass.getSimpleName() + ".");
		}
		assertEquals(String.format("Queried %s is not equal to inserted %s",
				kbpaClass.getSimpleName(), kbpaClass.getSimpleName()), kbpa, queriedKbpa);
		assertEquals(String.format(
				"Queried %s's hashCode() is not equal to inserted %s's hashCode()",
				kbpaClass.getSimpleName(), kbpaClass.getSimpleName()), kbpa.hashCode(),
				queriedKbpa.hashCode());
	}

	private void dropUnitTestSqlSchema(String schemaName) {
		try {
			sqlConnection.setAutoCommit(true);
			PreparedStatement dropSchemaPreparedStmt = sqlConnection
					.prepareStatement("DROP SCHEMA " + schemaName + " CASCADE");
			dropSchemaPreparedStmt.setQueryTimeout(30);
			dropSchemaPreparedStmt.executeUpdate();
			dropSchemaPreparedStmt.close();
			sqlConnection.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
			Assert.fail("Unable to drop unit test sql schema \"" + schemaName
					+ "\", this will have to be manually dropped. " + ex.getMessage());
		}
	}

	/**
	 * @param relation
	 * @param ontType
	 * @return
	 */
	protected KBRelationArgument getArgumentByRole(KBRelation relation, OntType ontType) {
		for (KBRelationArgument argument : relation.getArguments()) {
			if (argument.getRole().equals(ontType)) {
				return argument;
			}
		}
		return null;
	}

	protected KBTextProvenance.InsertionBuilder generateProvenance(String value) {
		return generateProvenance(value, provenanceNumber++);
	}

	protected KBEntityMentionProvenance.InsertionBuilder generateProvenance(String value, String
			entityMentionType) {
		return generateProvenance(value, provenanceNumber++,entityMentionType);
	}

	protected static KBTextProvenance.InsertionBuilder generateProvenance(String value, int
			index) {
		KBTextProvenance.InsertionBuilder builder = KBTextProvenance.builder();
		builder.setBeginOffset(0);
		builder.setEndOffset(1);
		builder.setConfidence(.5f);
		builder.setContributingSiteName("Contributing Site " + index);
		builder.setCorpusID("CorpusID" + index);
		builder.setCorpusName("Corpus" + index);
		builder.setCorpusType("CorpusType" + index);
		builder.setCorpusURI("CorpusURI" + index);
		builder.setDocumentID("DocumentID" + index);
		builder.setDocumentPublicationDate("2015-11-18");
		builder.setDocumentURI("DocumentURI" + index);
		builder.setSourceAlgorithmName("SourceAlgorithM" + index);
		builder.setSourceLanguage("SourceLanguage" + index);
		builder.setValue(value);
		return builder;
	}

	protected static KBEntityMentionProvenance.InsertionBuilder generateProvenance(String value, int
			index, String entityMentionType) {
		KBTextProvenance.InsertionBuilder builder = generateProvenance(value,index);
		KBEntityMentionProvenance.InsertionBuilder entityMentionBuilder =
				new KBEntityMentionProvenance.InsertionBuilder(builder);
		entityMentionBuilder.setType(entityMentionType);
		return entityMentionBuilder;
	}
}
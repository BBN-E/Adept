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

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.Type;
import adept.kbapi.KBEntity;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

import com.google.common.base.Optional;

public class TestEntity extends KBUnitTest {
	// initial entity values
	private final String entityType = "org";
	private final String expectedResultEntityType = "Organization";
	private final String entityMentionType = "NAME";
	private final float entityConfidence = 0.8f;
	private final float entityMentionConfidence = 0.9f;
	private final float entityCanonicalMentionConfidence = 0.54f;
	private final List<Pair<String, Double>> entityTypesWithConfidences;

	private final float updatedEntityMentionConfidence = 0.8f;
	private final float updatedEntityConfidence = 0.3f;
	private final float updatedEntityCanonicalMentionConfidence = 0.21f;

	public TestEntity() {
		entityTypesWithConfidences = new ArrayList<Pair<String, Double>>();
		entityTypesWithConfidences.add(new Pair<String, Double>("per", 0.22));
		entityTypesWithConfidences.add(new Pair<String, Double>("loc", 0.33));
	}

	@Test
	public void testEntity() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// Create the entity object
		Pair<Entity, List<EntityMention>> entityWithMentions = createTestEntityWithMentions(
				entityType, entityConfidence, entityMentionType, entityMentionConfidence,
				entityCanonicalMentionConfidence, entityTypesWithConfidences);

		// Initial insert
		KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
				entityWithMentions.getL(), entityWithMentions.getR(),
				KBOntologyMap.getTACOntologyMap());
		insertionBuilder.addExternalKBId(new KBID("External_Entity_ID", "ExampleKB"));
		insertionBuilder.addExternalKBId(new KBID("External_Entity_ID2", "ExampleKB2"));
		insertionBuilder.addExternalKBId(new KBID("External_Entity_ID2", "ExampleKB3"));
		insertionBuilder.addExternalKBId(new KBID("External_Entity_ID3", "ExampleKB2"));
		KBEntity kbEntity = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbEntity);

		// Run queries
		testQueryEntityById(kbEntity.getKBID(), entityType, entityConfidence,
				entityCanonicalMentionConfidence, entityTypesWithConfidences);
		testQueryEntitiesByType(expectedResultEntityType);
		testQueryEntitiesByStringRef("BBN Technologies");
		testQueryEntitiesByCaseSensitiveRegexMatch("^BBN");
		testQueryEntitiesByCaseInsensitiveRegexMatch("^bbn");
		testQueryEntitiesByValAndType(entityType);
		testQueryEntityByExternalID(new KBID("External_Entity_ID", "ExampleKB"), entityType,
				entityConfidence, entityCanonicalMentionConfidence, entityTypesWithConfidences);

		// Update entry
		KBEntity.UpdateBuilder entityUpdateBuilder = kbEntity.updateBuilder();
		entityUpdateBuilder.setConfidence(updatedEntityConfidence);
		entityUpdateBuilder.removeProvenance(kbEntity.getProvenances().iterator().next());

		KBTextProvenance.InsertionBuilder canonicalMentionBuilder = KBTextProvenance.builder(
				createTestChunk(), updatedEntityCanonicalMentionConfidence);
		KBTextProvenance.InsertionBuilder otherMentionBuilder = KBTextProvenance.builder(
				createTestChunk(), updatedEntityMentionConfidence);

		entityUpdateBuilder.addProvenance(otherMentionBuilder);
		entityUpdateBuilder.addProvenance(canonicalMentionBuilder);
		entityUpdateBuilder.setNewCanonicalMention(canonicalMentionBuilder,
				updatedEntityCanonicalMentionConfidence);

		final List<Pair<String, Double>> updatedEntityTypesWithConfidences = new ArrayList<Pair<String, Double>>();
		updatedEntityTypesWithConfidences.add(new Pair<String, Double>("gpe", 0.68));
		updatedEntityTypesWithConfidences.add(new Pair<String, Double>("loc", 0.45));

		Optional<OntType> personOntType = KBOntologyMap.getTACOntologyMap().getKBTypeForType(
				new Type("per"));
		Optional<OntType> cityOntType = KBOntologyMap.getTACOntologyMap().getKBTypeForType(
				new Type("gpe"));
		Optional<OntType> locationOntType = KBOntologyMap.getTACOntologyMap().getKBTypeForType(
				new Type("loc"));
		entityUpdateBuilder.removeType(personOntType.get()).addNewType(cityOntType.get(), 0.68f)
				.alterTypeConfidence(locationOntType.get(), 0.45f);

        entityUpdateBuilder.removeExternalKBID(new KBID("External_Entity_ID2", "ExampleKB2"));
        entityUpdateBuilder.addExternalKBID(new KBID("Updated_external_entity_id", "updatedKb"));
                
		KBEntity updatedKbEntity = entityUpdateBuilder.update(kb);

		// Run queries
		testQueryEntityById(updatedKbEntity.getKBID(), entityType, updatedEntityConfidence,
				updatedEntityCanonicalMentionConfidence, updatedEntityTypesWithConfidences);
		testQueryEntitiesByType(expectedResultEntityType);
		testQueryEntitiesByStringRef("BBN Technologies");
		testQueryEntitiesByCaseSensitiveRegexMatch("^BBN");
		testQueryEntitiesByCaseInsensitiveRegexMatch("^bbn");
		testQueryEntitiesByValAndType(entityType);
		testQueryEntityByExternalID(new KBID("External_Entity_ID", "ExampleKB"), entityType,
				updatedEntityConfidence, updatedEntityCanonicalMentionConfidence,
				updatedEntityTypesWithConfidences);
                
        Optional<KBPredicateArgument> externalObject1 = kb.getKBObjectByExternalID(new KBID("External_Entity_ID2", "ExampleKB2"));
        Assert.assertTrue("Deleted external kb id was not removed correctly.", !externalObject1.isPresent());
                
        Optional<KBPredicateArgument> externalObject2 = kb.getKBObjectByExternalID(new KBID("Updated_external_entity_id", "updatedKb"));
        Assert.assertTrue("Updated external kb id was not inserted correctly.", externalObject2.isPresent());

		Assert.assertTrue("Unable to delete entity kb object",
				kb.deleteKBObject(kbEntity.getKBID()));
	}

	private void testQueryEntityById(KBID kbEntityId, String expectedEntityType,
			float expectedEntityConfidence, float expectedCanonicalMentionConfidence,
			List<Pair<String, Double>> expectedEntityTypesWithConfidences) throws KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		boolean assertion = true;
		String assertionFailureText = "";

		KBEntity kbEntity = kb.getEntityById(kbEntityId);

		if (kbEntity == null) {
			assertion = false;
			assertionFailureText += "No entity returned from getEntityById(). ";
		}
		if (null != kbEntity) {
			String entityType = KBOntologyMap.getTACOntologyMap()
					.getTypeForKBType(getTypeWithHighestConfidenceForKBEntity(kbEntity)).get()
					.getType();
			if (!entityType.equals(expectedEntityType)) {
				assertion = false;
				assertionFailureText += "Entity type " + entityType
						+ " does not match expected entity type of " + expectedEntityType + ". ";
			}
	
			if (kbEntity.getConfidence() != expectedEntityConfidence) {
				assertion = false;
				assertionFailureText += "Entity confidence " + kbEntity.getConfidence()
						+ " does not match expected entity confidence of " + expectedEntityConfidence
						+ ". ";
			}
	
			if (kbEntity.getCanonicalMentionConfidence() != expectedCanonicalMentionConfidence) {
				assertion = false;
				assertionFailureText += "Entity canonical mention confidence "
						+ kbEntity.getCanonicalMentionConfidence()
						+ " does not match expected canonical mention confidence of "
						+ expectedCanonicalMentionConfidence + ". ";
			}
	
			for (Pair<String, Double> expectedEntityTypeWithConfidence : expectedEntityTypesWithConfidences) {
				OntType expectedType = KBOntologyMap.getTACOntologyMap()
						.getKBTypeForType(new Type(expectedEntityTypeWithConfidence.getL())).get();
	
				if (!kbEntity.getTypes().containsKey(expectedType)
						|| !kbEntity.getTypes().get(expectedType)
								.equals(expectedEntityTypeWithConfidence.getR().floatValue())) {
					assertion = false;
					assertionFailureText += "No matching entity type of type " + expectedType.getType()
							+ " with confidence " + expectedEntityTypeWithConfidence.getR()
							+ " found. ";
				}
			}
		}
		assertTrue(assertionFailureText, assertion);
	}

	private void testQueryEntitiesByType(String expectedType) throws KBQueryException {
		List<KBEntity> entityIds = kb.getEntitiesByType(new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, expectedType));

		assertTrue("No entities returned.", entityIds.size() > 0);

		for (KBEntity entity : entityIds) {
			if (!getTypeWithHighestConfidenceForKBEntity(entity).getType().equals(expectedType)) {
				Assert.fail("Invalid entity type returned from getEntitiesByType().");
			}
		}
	}

	private void testQueryEntitiesByStringRef(String stringReference) throws KBQueryException {
		List<KBEntity> kbEntities = kb.getEntitiesByStringReference(stringReference);

		int entitiesSize = kbEntities.size();
		assertTrue("Size of entities is not greater than 0.", entitiesSize > 0);

		for (KBEntity kbEntity : kbEntities) {
			// get mentions for entity

			int mentionsSize = kbEntity.getProvenances().size();
			assertTrue("Size of entity mentions is not greater than 0.", mentionsSize > 0);

			// for each mention, check that it matches with the arg string
			for (KBProvenance mention : kbEntity.getProvenances()) {
				KBTextProvenance textMention = (KBTextProvenance) mention;
				if (!textMention.getValue().equals(stringReference)) {
					Assert.fail("Entity mentions do not match the expected argument string. Expected \""
							+ stringReference + "\" got \"" + textMention.getValue() + "\".");
				}
			}
		}
	}

	private void testQueryEntitiesByCaseSensitiveRegexMatch(String regex) throws KBQueryException {
		List<KBEntity> kbEntities = kb.getEntitiesByRegexMatch(regex, true);

		int entitiesSize = kbEntities.size();
		assertTrue("Size of entities is not greater than 0 in getEntitiesByRegexMatch test",
				entitiesSize > 0);

		for (KBEntity kbEntity : kbEntities) {
			int mentionsSize = kbEntity.getProvenances().size();
			assertTrue(mentionsSize > 0);

			// for each mention, check that it matches with the arg string
			for (KBProvenance mention : kbEntity.getProvenances()) {
				KBTextProvenance textMention = (KBTextProvenance) mention;
				if (!textMention.getValue().equals("BBN Technologies")) {
					Assert.fail("Entity mentions do not match the expected argument string. Expected \"BBN Technologies\" got \""
							+ textMention.getValue() + "\".");
				}
			}
		}
	}

	@Test
	public void testNoResultsRegexMatch() throws KBQueryException {
		List<KBEntity> results = kb.getEntitiesByRegexMatch("oogetyboogety", false);
		Assert.assertEquals("No results should be returned", 0, results.size());
	}

	private void testQueryEntitiesByCaseInsensitiveRegexMatch(String regex) throws KBQueryException {
		List<KBEntity> kbEntities = kb.getEntitiesByRegexMatch(regex, false);

		int entitiesSize = kbEntities.size();
		assertTrue("Size of entities is not greater than 0 in getEntitiesByRegexMatch test",
				entitiesSize > 0);

		for (KBEntity kbEntity : kbEntities) {
			int mentionsSize = kbEntity.getProvenances().size();
			assertTrue(mentionsSize > 0);

			// for each mention, check that it matches with the arg string
			for (KBProvenance mention : kbEntity.getProvenances()) {
				KBTextProvenance textMention = (KBTextProvenance) mention;
				if (!textMention.getValue().equals("BBN Technologies")) {
					Assert.fail("Entity mentions do not match the expected argument string. Expected \"BBN Technologies\" got \""
							+ textMention.getValue() + "\".");
				}
			}
		}
	}

	private void testQueryEntitiesByValAndType(String expectedEntityType) throws KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		OntType expectedType = KBOntologyMap.getTACOntologyMap()
				.getKBTypeForType(new Type(expectedEntityType)).get();

		List<KBEntity> kbEntities = kb.getEntitiesByValueAndType("BBN Technologies", expectedType);
		int entitiesSize = kbEntities.size();
		assertTrue("Size of entities is not greater than 0", entitiesSize > 0);

		for (KBEntity kbEntity : kbEntities) {
			assertTrue("Invalid entity type, expected " + expectedType.getType() + " got "
					+ getTypeWithHighestConfidenceForKBEntity(kbEntity).getType(),
					getTypeWithHighestConfidenceForKBEntity(kbEntity).equals(expectedType));

			int mentionsSize = kbEntity.getProvenances().size();
			assertTrue("Size of mentions is not greater than 0", mentionsSize > 0);

			// for each mention, check that it matches with the arg string
			for (KBProvenance mention : kbEntity.getProvenances()) {
				KBTextProvenance textMention = (KBTextProvenance) mention;
				if (!textMention.getValue().equals("BBN Technologies")) {
					Assert.fail("Entity mentions do not match the expected argument string. Expected \"BBN Technologies\" got \""
							+ textMention.getValue() + "\".");
				}
			}
		}
	}

	private void testQueryEntityByExternalID(KBID externalKbId, String expectedEntityType,
			float expectedEntityConfidence, float expectedCanonicalMentionConfidence,
			List<Pair<String, Double>> expectedEntityTypesWithConfidences) throws KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		boolean assertion = true;
		String assertionFailureText = "";

		KBEntity testEntity = (KBEntity) kb.getKBObjectByExternalID(externalKbId).get();

		if (testEntity == null) {
			assertion = false;
			assertionFailureText += "No entity returned from getAdeptEntityByExternalID(). ";
		}
		if (null != testEntity) {
			String entityType = KBOntologyMap.getTACOntologyMap()
					.getTypeForKBType(getTypeWithHighestConfidenceForKBEntity(testEntity)).get()
					.getType();
			if (!entityType.equals(expectedEntityType)) {
				assertion = false;
				assertionFailureText += "Entity type " + entityType
						+ " does not match expected entity type of " + expectedEntityType + ". ";
			}
	
			if (testEntity.getConfidence() != expectedEntityConfidence) {
				assertion = false;
				assertionFailureText += "Entity confidence " + testEntity.getConfidence()
						+ " does not match expected entity confidence of " + expectedEntityConfidence
						+ ". ";
			}
	
			if (testEntity.getCanonicalMentionConfidence() != expectedCanonicalMentionConfidence) {
				assertion = false;
				assertionFailureText += "Entity canonical mention confidence "
						+ testEntity.getCanonicalMentionConfidence()
						+ " does not match expected canonical mention confidence of "
						+ expectedCanonicalMentionConfidence + ". ";
			}
	
			for (Pair<String, Double> expectedEntityTypeWithConfidence : expectedEntityTypesWithConfidences) {
				OntType expectedType = KBOntologyMap.getTACOntologyMap()
						.getKBTypeForType(new Type(expectedEntityTypeWithConfidence.getL())).get();
				if (!testEntity.getTypes().containsKey(expectedType)
						|| !testEntity.getTypes().get(expectedType)
								.equals(expectedEntityTypeWithConfidence.getR().floatValue())) {
					assertion = false;
					assertionFailureText += "No matching entity type of type " + expectedType
							+ " with confidence " + expectedEntityTypeWithConfidence.getR()
							+ " found. ";
				}
			}
		}
		assertTrue(assertionFailureText, assertion);
	}

	private static OntType getTypeWithHighestConfidenceForKBEntity(KBEntity kbEntity) {
		OntType currentOntType = null;
		float highestConfidence = Float.MIN_VALUE;
		for (OntType type : kbEntity.getTypes().keySet()) {
			if (kbEntity.getTypes().get(type) > highestConfidence) {
				highestConfidence = kbEntity.getTypes().get(type);
				currentOntType = type;
			}
		}
		return currentOntType;
	}
	
	@Test
	public void testInvalidEntityType() throws KBQueryException {
		KBEntity.InsertionBuilder entity = KBEntity.entityInsertionBuilder(
						Collections.singletonMap(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "INVALID_TYPE"), .1f),
						generateProvenance("invalidType"),
						.1f, .1f);
		KBUpdateException exception = null;
		try{
			entity.insert(kb);
		}catch(KBUpdateException e){
			//This should be thrown!
			exception = e;
		}
		Assert.assertTrue("Inserting entity with bad type should throw exception.", exception != null);
	}
	
	@Test 
	public void testGetPersonByExternalID() throws KBQueryException, KBUpdateException{
		KBEntity.InsertionBuilder personBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person"), .1f), generateProvenance("Person"), .1f, .1f);
		KBID testExternalKBID = new KBID("test","testid");
		personBuilder.addExternalKBId(testExternalKBID);
		
		KBEntity person = personBuilder.insert(kb);
		
		Optional<KBPredicateArgument> queryResult = kb.getKBObjectByExternalID(testExternalKBID);
		
		Assert.assertTrue("Person by external Id should exist", queryResult.isPresent());
		Assert.assertEquals("Queried person should be same as inserted person", person, queryResult.get()); 
		
	}
	
	@Test 
	public void testGetFemaleByExternalID() throws KBQueryException, KBUpdateException{
		KBEntity.InsertionBuilder personBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Female"), .1f), generateProvenance("Person"), .1f, .1f);
		KBID testExternalKBID = new KBID("test","testid");
		personBuilder.addExternalKBId(testExternalKBID);
		
		KBEntity person = personBuilder.insert(kb);
		
		Optional<KBPredicateArgument> queryResult = kb.getKBObjectByExternalID(testExternalKBID);
		
		Assert.assertTrue("Person by external Id should exist", queryResult.isPresent());
		Assert.assertEquals("Queried person should be same as inserted person", person, queryResult.get()); 
		
	}
	
	@Test 
	public void testGetMaleByExternalID() throws KBQueryException, KBUpdateException{
		KBEntity.InsertionBuilder personBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Male"), .1f), generateProvenance("Person"), .1f, .1f);
		KBID testExternalKBID = new KBID("test","testid");
		personBuilder.addExternalKBId(testExternalKBID);
		
		KBEntity person = personBuilder.insert(kb);
		
		Optional<KBPredicateArgument> queryResult = kb.getKBObjectByExternalID(testExternalKBID);
		
		Assert.assertTrue("Person by external Id should exist", queryResult.isPresent());
		Assert.assertEquals("Queried person should be same as inserted person", person, queryResult.get()); 
		
	}

}

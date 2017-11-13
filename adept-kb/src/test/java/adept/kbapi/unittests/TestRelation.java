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

import com.google.common.base.Optional;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import adept.common.Chunk;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.RelationMention;
import adept.common.Type;
import adept.kbapi.KBEntity;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;

import static org.junit.Assert.assertTrue;

public class TestRelation extends KBUnitTest {
	// Initial relation values
	private final String relationType = "per:cities_of_residence";
	private final String expectedResultType = "Resident";
	private final float initialRelationConfidence = 0.9f;
	private final float relationMentionConfidence = 0.9f;

	// Updated relation values
	private final float updatedRelationConfidence = 0.8f;
	private final float updatedRelationMentionConfidence = 0.5f;
	private final float updatedArgumentConfidence = 0.2f;

	@Test
	public void testRelation() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// insert entities into KB
		KBEntity kbEntity1 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);
		KBEntity kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		// create the document relation
		Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
				kbEntity1, kbEntity2, relationType, initialRelationConfidence,
				relationMentionConfidence);

		KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getTACOntologyMap());
		insertionBuilder.addExternalKBId(new KBID("External_Resident_Relation", "ExampleKB"));
        insertionBuilder.addExternalKBId(new KBID("External_Resident_Relation2", "ExampleKB2"));
		KBRelation kbRelation = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);

		testQueryRelationById(kbRelation.getKBID(), relationType, 1, initialRelationConfidence, 2,
				relationMentionConfidence);
		testQueryRelationByType(expectedResultType);
		testQueryRelationByArg(kbEntity1.getKBID());
		testQueryRelationByArgs(kbEntity1.getKBID(), kbEntity2.getKBID());
		Assert.assertTrue("Should be no relations with Jimmy argument", kb.getRelationsByArgs(kbEntity1.getKBID(), new KBID("Jimmy", KBOntologyModel.DATA_INSTANCES_PREFIX)).size() ==0);
		testQueryRelationByArgAndType(kbEntity1.getKBID(), "LivesIn", 0);
		testQueryRelationByStringRef("BBN Technologies");
		testQueryRelationByRegex("BBN.*", false);

		KBTextProvenance.InsertionBuilder newArgumentProvenanceBuilder = KBTextProvenance.builder(
				createTestChunk(), 0.3f);//createTestChunk
		// creates a NAME-type mention/chunk
		KBTextProvenance.InsertionBuilder newRelationProvenanceBuilder = KBTextProvenance.builder(
				createTestChunk(), updatedRelationMentionConfidence);

		KBRelationArgument argument = kbRelation.getArguments().iterator().next();

		KBRelation ownerRelation = kb.getKBRelationByKBRelationArgument(argument.getKBID());
		Assert.assertEquals("Owner Relation should be newly created relation",
				kbRelation.getKBID(), ownerRelation.getKBID());

		KBRelationArgument.UpdateBuilder argumentUpdateBuilder = argument.updateBuilder();
		argumentUpdateBuilder.setConfidence(updatedArgumentConfidence);
		argumentUpdateBuilder.addProvenance(newArgumentProvenanceBuilder);

		KBRelation.UpdateBuilder updateBuilder = (KBRelation.UpdateBuilder) kbRelation
				.updateBuilder();
		updateBuilder.addUpdatedArgument(argumentUpdateBuilder);
		updateBuilder.setConfidence(updatedRelationConfidence);
		updateBuilder.removeProvenance(kbRelation.getProvenances().iterator().next());
		updateBuilder.addProvenance(newRelationProvenanceBuilder);

		Pair<Entity, List<EntityMention>> newEntityAndMentions = createTestEntityWithMentions(
				"org", .3f, "NAME", .3f, .3f,
				Collections.singletonList(new Pair<String, Double>("org", .3)));
		KBEntity newArgEntity = KBEntity.entityInsertionBuilder(newEntityAndMentions.getL(),
				newEntityAndMentions.getR(), KBOntologyMap.getTACOntologyMap()).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(newArgEntity);
		updateBuilder.addNewArgument(KBRelationArgument.insertionBuilder(new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, "person"), newArgEntity, 0.05f));

        updateBuilder.removeExternalKBID(new KBID("External_Resident_Relation2", "ExampleKB2"));
        updateBuilder.addExternalKBID(new KBID("New_External_Resident_Relation", "New_ExampleKB"));

		KBRelation updatedRelation = updateBuilder.update(kb);

		testQueryRelationById(updatedRelation.getKBID(), relationType, 1,
				updatedRelationConfidence, 3, updatedRelationMentionConfidence);
		testQueryRelationByType(expectedResultType);
		testQueryRelationByArg(kbEntity1.getKBID());
		testQueryRelationByArgAndType(kbEntity1.getKBID(), "LivesIn", 0);
		testQueryRelationByStringRef("BBN Technologies");

        Optional<KBPredicateArgument> externalObject1 = kb.getKBObjectByExternalID(new KBID("External_Resident_Relation2", "ExampleKB2"));
        Assert.assertTrue("Deleted external kb id was not removed correctly.", !externalObject1.isPresent());

        Optional<KBPredicateArgument> externalObject2 = kb.getKBObjectByExternalID(new KBID("New_External_Resident_Relation", "New_ExampleKB"));
        Assert.assertTrue("Updated external kb id was not inserted correctly.", externalObject2.isPresent());
	}

	private void testQueryRelationById(KBID relationId, String expectedRelationType,
			int expectedNumberOfProvenances, float expectedRelationConfidence,
			int expectedNumberOfArguments, float expectedRelationMentionConfidence)
			throws KBQueryException, InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		KBRelation relation = kb.getRelationById(relationId);

		assertTrue("Relation returned is null.", relation != null);

		if (KBOntologyMap.getTACOntologyMap().getKBTypeForType(new Type(expectedRelationType))
				.isPresent()) {
			expectedRelationType = KBOntologyMap.getTACOntologyMap()
					.getKBTypeForType(new Type(expectedRelationType)).get().getType();
		}
		assertTrue("Unexpected relation type " + relation.getType().getType() + " expected "
				+ expectedRelationType + ".",
				relation.getType().getType().equals(expectedRelationType));

		assertTrue("Expected " + expectedNumberOfProvenances
				+ " document relation provenances, got " + relation.getProvenances().size(),
				relation.getProvenances().size() == expectedNumberOfProvenances);

		assertTrue("Expected document relation confidence " + expectedRelationConfidence + " got "
				+ relation.getConfidence() + ".",
				relation.getConfidence() == expectedRelationConfidence);

		assertTrue("Expected " + expectedNumberOfArguments + " arguments in relation got "
				+ relation.getArguments().size() + ".",
				relation.getArguments().size() == expectedNumberOfArguments);

		for (KBProvenance provenance : relation.getProvenances()) {
			KBTextProvenance textProvenance = (KBTextProvenance) provenance;
			assertTrue("Expected relation mention confidence " + expectedRelationMentionConfidence
					+ " got " + relation.getConfidence() + ".",
					textProvenance.getConfidence() == expectedRelationMentionConfidence);
		}
	}

	private void testQueryRelationByType(String expectedType) throws KBQueryException {
		List<KBRelation> relationIds = kb.getRelationsByType(new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, expectedType));

		assertTrue("No relations returned.", relationIds.size() > 0);

		for (KBRelation relation : relationIds) {
			if (!relation.getType().getType().equals(expectedType)) {
				Assert.fail("Invalid relation type returned from getRelationsByType().");
			}
		}
	}

	private void testQueryRelationByArg(KBID entityKbId) throws KBQueryException {
		boolean assertion = false;

		List<KBRelation> relationIds = kb.getRelationsByArg(entityKbId);
		int size = relationIds.size();
		assertTrue("Size of relations is not > 0", size > 0);

		for (KBRelation relation : relationIds) {
			List<KBID> externalKbIds = kb.getExternalKBIDs(relation.getKBID());
			for (KBID kbId : externalKbIds) {
				if (kbId.getObjectID().equals("External_Resident_Relation")) {
					assertion = true;
					break;
				}
			}

		}

		assertTrue("Expected external kb id \"External_Resident_Relation\" not found", assertion);
	}

	private void testQueryRelationByArgs(KBID entityKbId, KBID otherArgKBID) throws KBQueryException {
		boolean assertion = false;

		List<KBRelation> relationIds = kb.getRelationsByArgs(entityKbId, otherArgKBID);
		int size = relationIds.size();
		assertTrue("Size of relations is not > 0", size > 0);

		for (KBRelation relation : relationIds) {
			List<KBID> externalKbIds = kb.getExternalKBIDs(relation.getKBID());
			for (KBID kbId : externalKbIds) {
				if (kbId.getObjectID().equals("External_Resident_Relation")) {
					assertion = true;
					break;
				}
			}

		}

		assertTrue("Expected external kb id \"External_Resident_Relation\" not found", assertion);
	}

	private void testQueryRelationByArgAndType(KBID entityId, String expectedType,
			int expectedNumberOfIds) throws KBQueryException {
		List<KBRelation> relations = kb.getRelationsByArgAndType(entityId, new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, expectedType));
		int size = relations.size();
		assertTrue("Invalid number of relations returned, expected " + expectedNumberOfIds
				+ " got " + size, size == expectedNumberOfIds);
	}

	/**
	 * this query method looks for the relation justification chunk. If this is
	 * not correctly set, the query returns empty handed.
	 */
	private void testQueryRelationByStringRef(String stringReference) throws KBQueryException {
		List<KBRelation> relations = kb.getRelationsByStringReference(stringReference);

		int size = relations.size();
		assertTrue("Size of relations is not greater than zero", size > 0);

		for (KBRelation relation : relations) {

			int mentionsSize = relation.getProvenances().size();
			assertTrue("Size of relation mentions is not greater than 0.", mentionsSize > 0);

			// for each mention, check presence of justification
			for (KBProvenance provenance : relation.getProvenances()) {
				KBTextProvenance textProvenance = (KBTextProvenance) provenance;
				assertTrue("Relation justification is null", textProvenance.getValue() != null);
				if (!textProvenance.getValue().equals(stringReference)) {
					Assert.fail("Relation mentions do not match the expected argument string. Expected \""
							+ stringReference + "\" got \"" + textProvenance.getValue() + "\".");
				}
			}
		}
	}

	/**
	 * this query method looks for the relation justification chunk. If this is
	 * not correctly set, the query returns empty handed.
	 */
	private void testQueryRelationByRegex(String regex, boolean caseSensitive) throws KBQueryException {
		List<KBRelation> relations = kb.getRelationsByRegexMatch(regex, caseSensitive);

		int size = relations.size();
		assertTrue("Size of relations is not greater than zero", size > 0);

		for (KBRelation relation : relations) {

			int mentionsSize = relation.getProvenances().size();
			assertTrue("Size of relation mentions is not greater than 0.", mentionsSize > 0);

			// for each mention, check presence of justification
			for (KBProvenance provenance : relation.getProvenances()) {
				KBTextProvenance textProvenance = (KBTextProvenance) provenance;
				assertTrue("Relation justification is null", textProvenance.getValue() != null);
			}
		}
	}

	@Test
	public void testSpouseRelation() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// insert entity into KB
		KBEntity kbEntity1 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);
		KBEntity kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		// create the document relation
		Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createSpouseRelation(
				kbEntity1, kbEntity2);

		KBRelation.InsertionBuilder insertionBuilder = KBRelation.relationInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getTACOntologyMap());
		KBRelation kbRelation = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);
	}

	@Test
	public void testEmploymentMembershipRelation() throws KBQueryException, KBUpdateException {
		OntType personType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Person");
		OntType orgType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Organization");
		OntType employmentMembership = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX,
				"EmploymentMembership");
		OntType employeeMemberRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX,
				"employeeMember");
		OntType organizationRole = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "organization");

		KBEntity.InsertionBuilder employeeBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(personType, 1f),
				generateProvenance
						("John Smith","NAME"), 1f, 1f);

		KBEntity.InsertionBuilder employerBuilder = KBEntity.entityInsertionBuilder(
				Collections.singletonMap(orgType, 1f), generateProvenance
						("MegaCorp","NAME"), 1f, 1f);

		KBEntity johnSmith = employeeBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(johnSmith);
		KBEntity megaCorp = employerBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(megaCorp);

		KBRelation.InsertionBuilder relationBuilder = KBRelation.relationInsertionBuilder(
				employmentMembership, 1f);
		relationBuilder.addArgument(KBRelationArgument.insertionBuilder(employeeMemberRole,
				johnSmith, 1f));
		relationBuilder.addArgument(KBRelationArgument.insertionBuilder(organizationRole, megaCorp,
				1f));
		relationBuilder.addProvenance(generateProvenance("John Smith works for "
				+ "MegaCorp"));

		KBRelation relation = relationBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(relation);
	}

	protected Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> createSpouseRelation(
			KBEntity kbEntity1, KBEntity kbEntity2) {

		// entity and entity mention creation
		Entity person1entity = new Entity(1, new Type("Person"));
		Entity person2entity = new Entity(2, new Type("Person"));

		// create mention
		EntityMention person1mention = new EntityMention(0, testTokenOffset, testTokenStream);
		person1mention.addEntityConfidencePair(person1entity.getEntityId(), 0.9f);
		person1mention.setMentionType(new Type("NAME"));
		person1mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		EntityMention person2mention = new EntityMention(1, testTokenOffset, testTokenStream);
		person2mention.addEntityConfidencePair(person2entity.getEntityId(), 0.9f);
		person2mention.setMentionType(new Type("NAME"));
		person2mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		// set canonical mention and confidences on entities
		person1entity.setCanonicalMentions(person1mention);
		person1entity.setEntityConfidence(0.8f);
		person1entity.setCanonicalMentionConfidence(0.63);

		person2entity.setCanonicalMentions(person2mention);
		person2entity.setEntityConfidence(0.87);
		person2entity.setCanonicalMentionConfidence(0.1);

		// create relation mention
		RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type(
				"per:spouse"));
		relationMentionBuilder.setConfidence(relationMentionConfidence);
		relationMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));
		RelationMention.Filler arg1Filler = RelationMention.Filler.fromEntityMention(
				person2mention, new Type("arg-1"), 0.51f);
		relationMentionBuilder.addArgument(arg1Filler);
		RelationMention.Filler arg2Filler = RelationMention.Filler.fromEntityMention(
				person1mention, new Type("arg-2"), 0.52f);
		relationMentionBuilder.addArgument(arg2Filler);

		List<RelationMention> provenances = new ArrayList<RelationMention>();
		provenances.add(relationMentionBuilder.build());

		// create document relation
		DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(new Type(
				"per:spouse"));
		documentRelationBuilder.addProvenances(provenances);
		documentRelationBuilder.setConfidence(0.23f);
		DocumentRelationArgument.Builder arg1Builder = DocumentRelationArgument.builder(new Type(
				"arg-1"), DocumentRelationArgument.Filler.fromEntity(person2entity), 0.51f);
		arg1Builder.addProvenance(arg1Filler);
		documentRelationBuilder.addArgument(arg1Builder.build());

		DocumentRelationArgument.Builder arg2Builder = DocumentRelationArgument.builder(new Type(
				"arg-2"), DocumentRelationArgument.Filler.fromEntity(person1entity), 0.52f);
		arg2Builder.addProvenance(arg2Filler);
		documentRelationBuilder.addArgument(arg2Builder.build());

		// map from document entities to Adept KB entities
		HashMap<Item, KBPredicateArgument> entityMap = new HashMap<Item, KBPredicateArgument>();
		entityMap.put(person2entity, kbEntity2);
		entityMap.put(person1entity, kbEntity1);

		DocumentRelation documentRelation = documentRelationBuilder.build();

		return new Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>>(documentRelation,
				entityMap);
	}

	@Test
	public void testInvalidID() {
		try {
			kb.getRelationById(new KBID("x", "y"));
			Assert.fail("Should have thrown a KBQueryException!");
		} catch (KBQueryException e) {
			// Expected behavior
		}
	}
}
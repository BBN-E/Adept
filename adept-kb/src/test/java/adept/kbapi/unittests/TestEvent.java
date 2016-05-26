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

package adept.kbapi.unittests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import adept.common.DocumentEvent;
import adept.common.DocumentEventArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.Type;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEvent;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

public class TestEvent extends KBUnitTest {
	// Initial relation values
	private final String eventType = "BeBorn";
	private final String expectedResultType = "BeBorn";
	private final String externalID = "External_Born_ID";
	private final float initialEventConfidence = 0.9f;
	private final float eventMentionConfidence = 0.9f;

	// Updated relation values
	private final float updatedEventConfidence = 0.8f;
	private final float updatedEventMentionConfidence = 0.5f;
	private final float updatedArgumentConfidence = 0.2f;

	@Test
	public void testEvent() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		// insert entity into KB
		KBEntity kbEntity1 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);
		KBEntity kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		// create the document relation
		Pair<DocumentEvent, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestEventWithEntityMap(
				kbEntity1, kbEntity2, eventType, initialEventConfidence, eventMentionConfidence);

		KBEvent.InsertionBuilder insertionBuilder = KBEvent.eventInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getRichEREOntologyMap());
		insertionBuilder.addExternalKBId(new KBID(externalID, "ExampleKB"));
		OntType genericRealis = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Generic");
		// insertionBuilder.addRealisType(genericRealis, .2f);
		KBEvent kbEvent = insertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbEvent);

		Assert.assertEquals("Event has realis: ", .2f, kbEvent.getRealisTypes().get(genericRealis),
				0.0001f);
		testQueryEventById(kbEvent.getKBID(), eventType, 1, initialEventConfidence, 2,
				eventMentionConfidence, Collections.singletonMap(genericRealis, .2f));
		testQueryEventByType(expectedResultType);
		testQueryEventByArg(kbEntity1.getKBID());
		OntType expectedResultOntType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX,
				expectedResultType);
		testQueryEventByArgAndType(kbEntity1.getKBID(), expectedResultOntType, 1);
		testQueryEventByStringRef("BBN Technologies");

		KBTextProvenance.InsertionBuilder newArgumentProvenanceBuilder = KBTextProvenance.builder(
				createTestChunk(), 0.3f);
		KBTextProvenance.InsertionBuilder newRelationProvenanceBuilder = KBTextProvenance.builder(
				createTestChunk(), updatedEventMentionConfidence);

		KBRelationArgument argument = null;
		for (KBRelationArgument possibleArgument : kbEvent.getArguments()) {
			if (possibleArgument.getRole().getType().equals("place")) {
				argument = possibleArgument;
			}
		}
		Assert.assertNotNull("Could not find place argument on event", argument);
		checkObjectConfidence(argument, 0.52f);

		KBRelationArgument.UpdateBuilder argumentUpdateBuilder = argument.updateBuilder();
		argumentUpdateBuilder.setConfidence(updatedArgumentConfidence);
		argumentUpdateBuilder.addProvenance(newArgumentProvenanceBuilder);

		KBEvent.UpdateBuilder updateBuilder = kbEvent.updateBuilder();
		updateBuilder.addUpdatedArgument(argumentUpdateBuilder);
		updateBuilder.setConfidence(updatedEventConfidence);
		updateBuilder.removeProvenance(kbEvent.getProvenances().iterator().next());
		updateBuilder.addProvenance(newRelationProvenanceBuilder);
		OntType futureRealisType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Future");
		updateBuilder.addNewRealisType(futureRealisType, .3f);

		KBEvent updatedEvent = updateBuilder.update(kb);
		checkObjectConfidence(argument, updatedArgumentConfidence);

		HashMap<OntType, Float> expectedRealis = new HashMap<OntType, Float>();
		expectedRealis.put(genericRealis, .2f);
		expectedRealis.put(futureRealisType, .3f);

		testQueryEventById(updatedEvent.getKBID(), eventType, 1, updatedEventConfidence, 2,
				updatedEventMentionConfidence, expectedRealis);

		testQueryEventByType(expectedResultType);
		testQueryEventByArg(kbEntity1.getKBID());
		testQueryEventByArgAndType(kbEntity1.getKBID(), expectedResultOntType, 1);
		testQueryEventByStringRef("BBN Technologies");

		updateBuilder = updatedEvent.updateBuilder();
		updateBuilder.removeRealisType(futureRealisType);
		updateBuilder.alterRealisTypeConfidence(genericRealis, .4f);
		updatedEvent = updateBuilder.update(kb);
		expectedRealis.remove(futureRealisType);
		expectedRealis.put(genericRealis, .4f);

		Assert.assertEquals("Updated realis didn't match", expectedRealis,
				updatedEvent.getRealisTypes());

	}

	private void testQueryEventById(KBID eventId, String expectedEventType,
			int expectedNumberOfProvenances, float expectedEventConfidence,
			int expectedNumberOfArguments, float expectedEventMentionConfidence,
			Map<OntType, Float> expectedRealis) throws KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		KBEvent event = kb.getEventById(eventId);

		assertTrue("Event returned is null.", event != null);

		if (KBOntologyMap.getTACOntologyMap().getKBTypeForType(new Type(expectedEventType))
				.isPresent()) {
			expectedEventType = KBOntologyMap.getTACOntologyMap()
					.getKBTypeForType(new Type(expectedEventType)).get().getType();
		}
		assertTrue("Unexpected event type " + event.getType().getType() + " expected "
				+ expectedEventType + ".", event.getType().getType().equals(expectedEventType));

		assertTrue("Expected " + expectedNumberOfProvenances + " document event provenances, got "
				+ event.getProvenances().size(),
				event.getProvenances().size() == expectedNumberOfProvenances);

		assertTrue("Expected document event confidence " + expectedEventConfidence + " got "
				+ event.getConfidence() + ".", event.getConfidence() == expectedEventConfidence);

		assertTrue("Expected " + expectedNumberOfArguments + " arguments in event mention, got "
				+ event.getArguments().size() + ".",
				event.getArguments().size() == expectedNumberOfArguments);

		for (KBProvenance provenance : event.getProvenances()) {
			KBTextProvenance textProvenance = (KBTextProvenance) provenance;
			assertTrue("Expected event mention confidence " + expectedEventMentionConfidence
					+ " got " + textProvenance.getConfidence() + ".",
					textProvenance.getConfidence() == expectedEventMentionConfidence);
		}

		Assert.assertEquals("Realis types don't match", expectedRealis, event.getRealisTypes());

	}

	private void testQueryEventByType(String expectedType) throws KBQueryException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException {
		List<KBEvent> kbEvents = kb.getEventsByType(new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, expectedType));

		assertTrue("No events returned.", kbEvents.size() > 0);

		if (KBOntologyMap.getTACOntologyMap().getKBTypeForType(new Type(expectedType)).isPresent()) {
			expectedType = KBOntologyMap.getTACOntologyMap()
					.getKBTypeForType(new Type(expectedType)).get().getType();
		}

		for (KBEvent event : kbEvents) {
			if (!event.getType().getType().equals(expectedType)) {
				Assert.fail("Invalid event type returned from getRelationsByType().");
			}
		}
	}

	private void testQueryEventByArg(KBID entityKbId) throws KBQueryException {
		boolean assertion = false;

		List<KBEvent> kbEvents = kb.getEventsByArg(entityKbId);
		int size = kbEvents.size();
		assertTrue("Size of events is not > 0", size > 0);

		for (KBEvent kbEvent : kbEvents) {
			List<KBID> externalKbIds = kb.getExternalKBIDs(kbEvent.getKBID());
			for (KBID kbId : externalKbIds) {
				if (kbId.getObjectID().equals(externalID)) {
					assertion = true;
					break;
				}
			}

		}

		assertTrue("Expected external kb id \"" + externalID + "\" not found", assertion);
	}

	private void testQueryEventByArgAndType(KBID entityId, OntType expectedType,
			int expectedNumberOfIds) throws KBQueryException {
		List<KBEvent> relations = kb.getEventsByArgAndType(entityId, expectedType);
		int size = relations.size();
		assertTrue("Invalid number of relations returned, expected " + expectedNumberOfIds
				+ " got " + size, size == expectedNumberOfIds);
	}

	/**
	 * this query method looks for the relation justification chunk. If this is
	 * not correctly set, the query returns empty handed.
	 */
	private void testQueryEventByStringRef(String stringReference) throws KBQueryException {
		List<KBEvent> relations = kb.getEventsByStringReference(stringReference);

		int size = relations.size();
		assertTrue("Size of relations is not greater than zero", size > 0);

		for (KBEvent relation : relations) {

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

	@Test
	public void testRichEREEventMappings() throws IOException, KBQueryException, KBUpdateException {
		Pair<Entity, List<EntityMention>> testEntityWithMentions = createTestEntityWithMentions(
				"per", .3f, "per", .3f, .3f,
				Collections.singletonList(new Pair<String, Double>("per", .2d)));
		Entity finedEntity = testEntityWithMentions.getL();

		KBOntologyMap richEREMap = KBOntologyMap.getRichEREOntologyMap();
		KBEntity kbFinedEntity = KBEntity.entityInsertionBuilder(finedEntity,
				testEntityWithMentions.getR(), richEREMap).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbFinedEntity);

		Assert.assertTrue("Entity type should be mapped to Person", kbFinedEntity.getTypes()
				.keySet().iterator().next().getType().equals("Person"));

		DocumentEvent.Builder fineBuilder = DocumentEvent.builder(new Type("Fine")).setScore(.3f);
		fineBuilder.addArgument(DocumentEventArgument
				.builder(new Type("Fine"), new Type("entity"),
						DocumentEventArgument.Filler.fromEntity(finedEntity)).setScore(0.3f)
				.build());

		DocumentEvent fineEvent = fineBuilder.build();

		KBEvent.InsertionBuilder kbFineBuilder = KBEvent.eventInsertionBuilder(fineEvent,
				Collections.<Item, KBPredicateArgument> singletonMap(finedEntity, kbFinedEntity),
				richEREMap);

		Assert.assertTrue("Event type should be mapped to Fine", kbFineBuilder.getType().getType()
				.equals("Fine"));
		Assert.assertTrue("Event arg type should be mapped to finedEntity", kbFineBuilder
				.getArguments().iterator().next().getRole().getType().equals("finedEntity"));

	}

	@Test
	public void testInvalidID() {
		try {
			kb.getEventById(new KBID("x", "y"));
			Assert.fail("Should have thrown a KBQueryException!");
		} catch (KBQueryException e) {
			// Expected behavior
		}
	}

}

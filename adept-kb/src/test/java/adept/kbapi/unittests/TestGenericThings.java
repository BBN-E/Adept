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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;

import org.junit.Assert;
import org.junit.Test;

import adept.common.Chunk;
import adept.common.DocumentEvent;
import adept.common.DocumentEventArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.EventMention;
import adept.common.EventMentionArgument;
import adept.common.EventText;
import adept.common.GenericThing;
import adept.common.Item;
import adept.common.OntType;
import adept.common.Type;
import adept.kbapi.KBEvent;
import adept.kbapi.KBGenericThing;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;

public class TestGenericThings extends KBUnitTest {

	@Test
	public void testGenericThingsMapped() throws KBUpdateException,
			InvalidPropertiesFormatException, FileNotFoundException, IOException, KBQueryException {

		// entity and entity mention creation
		Entity personentity = new Entity(2, new Type("per"));

		GenericThing larceny = new GenericThing(new Type("Crime"), "larceny");

		Type eventType = new Type("Convict");
		Type defendantRole = new Type("defendant");
		Type crimeRole = new Type("crime");

		EntityMention personmention = new EntityMention(1, testTokenOffset, testTokenStream);
		personmention.addEntityConfidencePair(personentity.getEntityId(), 0.9f);
		personmention.setMentionType(new Type("NAME"));
		personmention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));

		personentity.setCanonicalMentions(personmention);
		personentity.setEntityConfidence(0.87);
		personentity.setCanonicalMentionConfidence(0.1);

		Chunk defendantChunk = new Chunk(testTokenOffset, testTokenStream);
		Chunk crimeChunk = new Chunk(testTokenOffset, testTokenStream);

		KBGenericThing.InsertionBuilder larcenyBuilder = KBGenericThing
				.genericThingInsertionBuilder(larceny, Collections.singletonList(crimeChunk),
						KBOntologyMap.getRichEREOntologyMap());
		KBGenericThing kbLarceny = larcenyBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbLarceny);

		// create relation mention
		EventMention.Builder eventMentionBuilder = EventMention.builder(eventType);
		Chunk mentionTextChunk = new Chunk(testTokenOffset, testTokenStream);
		eventMentionBuilder.setScore(1f);
		eventMentionBuilder.setProvenance(EventText.builder(eventType, mentionTextChunk)
				.setScore(.23f).build());
		EventMentionArgument mentionArg1 = EventMentionArgument
				.builder(eventType, defendantRole, defendantChunk).setScore(.32f).build();
		eventMentionBuilder.addArgument(mentionArg1);
		EventMentionArgument mentionArg2 = EventMentionArgument
				.builder(eventType, crimeRole, crimeChunk).setScore(.30f).build();
		eventMentionBuilder.addArgument(mentionArg2);

		EventMention eventMention = eventMentionBuilder.build();

		// create document relation
		DocumentEvent.Builder documentEventBuilder = DocumentEvent.builder(eventType);
		documentEventBuilder.addProvenanceFromEventMention(eventMention);
		documentEventBuilder.setScore(.8f);
		DocumentEventArgument.Builder arg1Builder = DocumentEventArgument.builder(eventType,
				defendantRole, DocumentEventArgument.Filler.fromEntity(personentity)).setScore(
				0.51f);
		arg1Builder.addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(
				eventMention, mentionArg1));
		documentEventBuilder.addArgument(arg1Builder.build());

		DocumentEventArgument.Builder arg2Builder = DocumentEventArgument.builder(eventType,
				crimeRole, DocumentEventArgument.Filler.fromGenericThing(larceny)).setScore(0.52f);
		arg2Builder.addProvenance(DocumentEventArgument.Provenance.fromArgumentOfEventMention(
				eventMention, mentionArg2));
		documentEventBuilder.addArgument(arg2Builder.build());

		// map from document entities to Adept KB entities
		HashMap<Item, KBPredicateArgument> entityMap = new HashMap<Item, KBPredicateArgument>();
		entityMap.put(personentity, insertDefaultTestEntity());
		assertEqualsAndHashCodeByQueryByKBID(entityMap.get(personentity));
		entityMap.put(larceny, kbLarceny);

		DocumentEvent documentEvent = documentEventBuilder.build();

		KBEvent.InsertionBuilder eventBuilder = KBEvent.eventInsertionBuilder(documentEvent,
				entityMap, KBOntologyMap.getRichEREOntologyMap());
		KBEvent event = eventBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(event);

		event = kb.getEventById(event.getKBID());
		KBRelationArgument crimeArgument = getArgumentByRole(event, new OntType(
				KBOntologyModel.ONTOLOGY_CORE_PREFIX, "crime"));
		Assert.assertNotNull("Result event should have a crime argument", crimeArgument);
		Assert.assertEquals("Crime argument should be equal to kb generic thing for crime",
				kbLarceny.getKBID(), crimeArgument.getTarget().getKBID());
	}

	@Test
	public void testGenericThingsDirect() throws KBQueryException, KBUpdateException,
			FileNotFoundException {
		OntType crimeType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Crime");
		KBGenericThing.InsertionBuilder builder = KBGenericThing.genericThingInsertionBuilder(
				crimeType, "larceny");

		KBTextProvenance.InsertionBuilder provenanceBuilder = getTestKBTextProvenanceBuilder();
		builder.addProvenance(provenanceBuilder);
		KBGenericThing larceny = builder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(larceny);
		KBGenericThing valueQueriedLarceny = kb.getGenericThingByTypeAndValue(crimeType, "larceny")
				.get();
		Assert.assertEquals("valueQueriedLarceny is not equal to larceny", valueQueriedLarceny,
				larceny);
	}

	@Test
	public void testGenericThingExistingProvenances() throws KBUpdateException, KBQueryException {
		OntType type = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Crime");
		KBGenericThing thing = KBGenericThing.genericThingInsertionBuilder(type, "larceny")
				.addProvenance(generateProvenance("three")).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(thing);

		Assert.assertEquals("Wrong number of provenances", 1, thing.getProvenances().size());

		KBGenericThing thing2 = KBGenericThing.genericThingInsertionBuilder(type, "larceny")
				.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(thing2);

		Assert.assertEquals("Wrong number of provenances", 1, thing2.getProvenances().size());
		Assert.assertEquals("Dates should equal", thing, thing2);
		KBGenericThing queriedThing = kb.getGenericThingByTypeAndValue(type, "larceny").get();

		Assert.assertEquals("Wrong number of provenances", 1, thing2.getProvenances().size());
		Assert.assertEquals("queriedThing is not equal to thing", thing, queriedThing);

	}

	/**
	 * @return
	 */
	private KBTextProvenance.InsertionBuilder getTestKBTextProvenanceBuilder() {

		return KBTextProvenance.builder().setBeginOffset(1).setEndOffset(2)
				.setCorpusID("TEST_CORPUS_ID").setCorpusName("TEST_CORPUS_NAME")
				.setCorpusType("TEST_CORPUS_TYPE").setCorpusURI("TEST_CORPUS_URI")
				.setDocumentID("TEST_DOCUMENT_ID").setDocumentPublicationDate("2015-11-18")
				.setSourceLanguage("TEST_SOURCE_LANGUAGE");
	}
	
	@Test
	public void testGenericThingWithQuotes() throws KBUpdateException, KBQueryException{
		String stringWithQuotes = "quote \" crime";
		OntType type = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Crime");
		KBGenericThing.InsertionBuilder builder = KBGenericThing.genericThingInsertionBuilder(type, stringWithQuotes);
		builder.addProvenance(generateProvenance("stuff"));
		KBGenericThing result = builder.insert(kb);
		
		KBGenericThing queryResult = kb.getGenericThingByTypeAndValue(type, stringWithQuotes).get();
		Assert.assertEquals("Inserted and Queried Generic Things don't match", result, queryResult);
		
	}
}

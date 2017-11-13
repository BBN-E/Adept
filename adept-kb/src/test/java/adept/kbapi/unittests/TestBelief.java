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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import adept.common.BeliefMention;
import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentBelief;
import adept.common.DocumentMentalStateArgument;
import adept.common.DocumentRelation;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.KBID;
import adept.common.NumberPhrase;
import adept.common.NumericValue;
import adept.common.Pair;
import adept.common.RelationMention;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.io.Reader;
import adept.kbapi.KBBelief;
import adept.kbapi.KBEntity;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
import adept.utilities.DocumentMaker;

public class TestBelief extends KBUnitTest {
	private DocumentBelief documentBelief;
	private Map<Item, KBPredicateArgument> kbArgumentsMap;

	private final float initialBeliefConfidence = 0.6f;
	private final float beliefMentionConfidence = 0.4f;

	private final float updatedBeliefConfidence = 0.7f;

	private KBEntity kbEntity1;
	private KBEntity kbEntity2;

	@Test
	public void testBelief() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, IOException, URISyntaxException {
		// create belief
		buildBelief(initialBeliefConfidence, beliefMentionConfidence);

		KBBelief.InsertionBuilder beliefInsertionBuilder = KBBelief.beliefInsertionBuilder(
				documentBelief, kbArgumentsMap, KBOntologyMap.getTACOntologyMap());
		beliefInsertionBuilder.addExternalKBId(new KBID("External_Belief", "ExampleKB"));
		KBBelief kbBelief = beliefInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbBelief);
		List<KBRelationArgument> arguments = new ArrayList<KBRelationArgument>(kbBelief.getArguments());
		
		testQueryBeliefById(kbBelief.getKBID(), 1, initialBeliefConfidence, documentBelief
				.getArguments().size(), beliefMentionConfidence);
		testQueryBeliefByArg(kbEntity1.getKBID());
		Assert.assertTrue("Should find KBBelief by two arguments", kb.getBeliefsByArgs(arguments.get(0).getTarget().getKBID(), arguments.get(1).getTarget().getKBID()).size() > 0);
		Assert.assertTrue("Should find KBBelief by two arguments", kb.getBeliefsByArgs(arguments.get(0).getTarget().getKBID(), arguments.get(1).getTarget().getKBID()).get(0).getKBID().equals(kbBelief.getKBID()));
		Assert.assertTrue("Should not find KBBelief with bad argument", kb.getBeliefsByArgs(arguments.get(0).getKBID(), new KBID("Jimmy", KBOntologyModel.DATA_INSTANCES_PREFIX)).size() == 0);
		testQueryBeliefByStringRef("BBN Technologies");

		KBRelationArgument argument = kbBelief.getArguments().iterator().next();
		KBRelationArgument.UpdateBuilder argumentUpdateBuilder = argument.updateBuilder();
		argumentUpdateBuilder.setConfidence(0.2f);

		KBBelief.UpdateBuilder updateBuilder = kbBelief.updateBuilder();
		updateBuilder.addUpdatedArgument(argumentUpdateBuilder);
		updateBuilder.setConfidence(updatedBeliefConfidence);

		KBBelief updatedBelief = updateBuilder.update(kb);

		testQueryBeliefById(updatedBelief.getKBID(), 1, updatedBeliefConfidence, documentBelief
				.getArguments().size(), beliefMentionConfidence);
		testQueryBeliefByArg(kbEntity1.getKBID());
		testQueryBeliefByStringRef("BBN Technologies");
	}

	private void buildBelief(float beliefConfidence, float beliefMentionConfidence)
			throws KBUpdateException, KBQueryException, InvalidPropertiesFormatException,
			IOException, URISyntaxException {
		// create and insert entity into KB
		Pair<Entity, List<EntityMention>> entityWithMentions = createTestEntityWithMentions(
				defaultEntityType, defaultEntityConfidence, defaultEntityMentionType,
				defaultEntityMentionConfidence, defaultEntityCanonicalMentionConfidence,
				defaultEntityTypesWithConfidences);
		KBEntity.InsertionBuilder entityInsertionBuilder = KBEntity.entityInsertionBuilder(
				entityWithMentions.getL(), entityWithMentions.getR(),
				KBOntologyMap.getTACOntologyMap());
		kbEntity1 = entityInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbEntity1);

		kbEntity2 = insertDefaultTestEntity();
		assertEqualsAndHashCodeByQueryByKBID(kbEntity2);

		// create and insert strength value into KB
		HltContentContainer hltContentContainer = new HltContentContainer();
		Document document = DocumentMaker
				.getInstance()
				.createDocument(
						"sample_numbers_2.txt",
						null,
						"Text",
						"sample_numbers_2.txt",
						"English",
						Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_numbers_2.txt"),
						hltContentContainer);
		CharOffset numberCharOffset = new CharOffset(11, 12);
		String numberText = "2";
		Token numberToken = new Token(1, numberCharOffset, numberText);
		TokenOffset numberTokenOffset = new TokenOffset(0, 0);
		TokenStream testNumericTokenStream = new TokenStream(TokenizerType.WHITESPACE,
				TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
		testNumericTokenStream.add(numberToken);
		NumberPhrase numberPhrase = new NumberPhrase(numberTokenOffset, testNumericTokenStream);
		NumericValue strengthNumericValue = new NumericValue(new Integer(2));
		numberPhrase.setNumber(strengthNumericValue.asNumber());

		KBNumber.InsertionBuilder numberInsertionBuilder = KBNumber.numberInsertionBuilder(
				strengthNumericValue, Collections.singletonList(numberPhrase));
		KBNumber kbStrength = numberInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbStrength);

		// create and insert relation into KB
		Pair<DocumentRelation, HashMap<Item, KBPredicateArgument>> relationWithEntityMap = createTestRelationWithEntityMap(
				kbEntity1, kbEntity2, defaultRelationType, defaultRelationConfidence,
				defaultRelationMentionConfidence);
		RelationMention relationMention = relationWithEntityMap.getL().getProvenances().iterator()
				.next();
		RelationMention.Filler relationMentionArg = relationMention.getArguments().iterator()
				.next();

		KBRelation.InsertionBuilder relationInsertionBuilder = KBRelation.relationInsertionBuilder(
				relationWithEntityMap.getL(), relationWithEntityMap.getR(),
				KBOntologyMap.getTACOntologyMap());
		KBRelation kbRelation = relationInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);
		KBRelationArgument kbRelationArg = kbRelation.getArguments().iterator().next();

		BeliefMention.Builder beliefMentionBuilder = BeliefMention.builder();
		beliefMentionBuilder.setConfidence(beliefMentionConfidence);
		beliefMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));
		beliefMentionBuilder.addArgument(BeliefMention.Filler.createSourceArgument(
				entityWithMentions.getR().get(0), 0.51f));
		beliefMentionBuilder.addArgument(BeliefMention.Filler.createStrengthArgument(numberPhrase,
				0.64f));
		beliefMentionBuilder.addArgument(BeliefMention.Filler.createTargetArgument(relationMention,
				0.86f));
		beliefMentionBuilder.addArgument(BeliefMention.Filler.createTargetArgument(relationMention,
				relationMentionArg, 0.72f));

		List<BeliefMention> provenances = new ArrayList<BeliefMention>();
		provenances.add(beliefMentionBuilder.build());

		/*
		 * XSDDate startDate = XSDDate.fromString("2015-09-04"); XSDDate endDate
		 * = XSDDate.fromString("2015-09-05"); TemporalSpan validTemporalSpan =
		 * TemporalSpan.builder() .setBeginDate(startDate) .setEndDate(endDate)
		 * .build();
		 * 
		 * 
		 * Map<TemporalValue, KBID> insertedDates = new HashMap<TemporalValue,
		 * KBID>(); insertedDates.put(startDate, kb.insertXSDDate(startDate,
		 * Collections.<TemporalResolution>emptyList()));
		 * insertedDates.put(endDate, kb.insertXSDDate(endDate,
		 * Collections.<TemporalResolution>emptyList())); KBID spanID =
		 * kb.insertTemporalSpan(validTemporalSpan, insertedDates);
		 */

		// create document belief
		DocumentBelief.Builder documentBeliefBuilder = DocumentBelief.builder();
		documentBeliefBuilder.addProvenances(provenances);
		documentBeliefBuilder.setConfidence(beliefConfidence);

		DocumentMentalStateArgument.Builder sourceArgBuilder = DocumentMentalStateArgument.builder(
				new Type("source"),
				DocumentMentalStateArgument.Filler.createSourceArgument(entityWithMentions.getL()),
				0.51f);
		sourceArgBuilder.addProvenance(provenances.get(0).getArguments().asList().get(0));

		documentBeliefBuilder.addArgument(sourceArgBuilder.build());
		documentBeliefBuilder.addArgument(DocumentMentalStateArgument.builder(new Type("strength"),
				DocumentMentalStateArgument.Filler.createStrengthArgument(strengthNumericValue),
				0.64f).build());
		documentBeliefBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("target"),
				DocumentMentalStateArgument.Filler.createTargetArgument(relationWithEntityMap
						.getL()), 0.86f).build());
		documentBeliefBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("target"),
				DocumentMentalStateArgument.Filler.createTargetArgument(
						relationWithEntityMap.getL(), relationWithEntityMap.getL().getArguments()
								.iterator().next()), 0.72f).build());
		// documentBeliefBuilder.addArgument(DocumentBelief.Filler.createValidTemporalSpanArgument(validTemporalSpan,
		// 0.23f));

		// map to Adept KB entities
		kbArgumentsMap = new HashMap<Item, KBPredicateArgument>();
		kbArgumentsMap.put(entityWithMentions.getL(), kbEntity1);
		kbArgumentsMap.put(relationWithEntityMap.getL(), kbRelation);
		kbArgumentsMap.put(strengthNumericValue, kbStrength);
		kbArgumentsMap.put(relationWithEntityMap.getL().getArguments().iterator().next()
				, kbRelationArg);

		documentBelief = documentBeliefBuilder.build();
	}

	private void testQueryBeliefById(KBID beliefId, int expectedNumberOfProvenances,
			float expectedBeliefConfidence, int expectedNumberOfArguments,
			float expectedBeliefMentionConfidence) throws KBQueryException {
		KBBelief belief = kb.getBeliefById(beliefId);

		assertTrue("Belief returned is null.", belief != null);

		assertTrue("Expected " + expectedNumberOfProvenances
				+ " document relation provenances, got " + belief.getProvenances().size(), belief
				.getProvenances().size() == expectedNumberOfProvenances);

		assertTrue("Expected document belief confidence " + expectedBeliefConfidence + " got "
				+ belief.getConfidence() + ".", belief.getConfidence() == expectedBeliefConfidence);

		assertTrue("Expected " + expectedNumberOfArguments + " arguments in belief mention, got "
				+ belief.getArguments().size() + ".",
				belief.getArguments().size() == expectedNumberOfArguments);

		Iterator<KBProvenance> iterator = belief.getProvenances().iterator();
		while (iterator.hasNext()) {
			KBTextProvenance provenance = (KBTextProvenance) iterator.next();
			assertTrue("Expected belief mention confidence " + expectedBeliefMentionConfidence
					+ " got " + provenance.getConfidence() + ".",
					provenance.getConfidence() == expectedBeliefMentionConfidence);
		}
	}

	private void testQueryBeliefByArg(KBID entityId) throws KBQueryException {
		boolean assertion = false;

		List<KBBelief> beliefs = kb.getBeliefsByArg(entityId);
		int size = beliefs.size();
		assertTrue("Size of beliefs is not > 0", size > 0);

		for (KBBelief belief : beliefs) {
			List<KBID> externalKbIds = kb.getExternalKBIDs(belief.getKBID());
			for (KBID kbId : externalKbIds) {
				if (kbId.getObjectID().equals("External_Belief")) {
					assertion = true;
					break;
				}
			}
		}

		assertTrue("assertion is false in testQueryBeliefByArg", assertion);
	}

	private void testQueryBeliefByStringRef(String stringReference) throws KBQueryException {
		List<KBBelief> beliefs = kb.getBeliefsByStringReference(stringReference);

		int size = beliefs.size();
		assertTrue("Size of beliefs is not greater than zero", size > 0);

		for (KBBelief belief : beliefs) {
			int mentionsSize = belief.getProvenances().size();
			assertTrue("Size of belief mentions is not greater than 0.", mentionsSize > 0);

			// for each mention, check presence of justification
			for (KBProvenance provenance : belief.getProvenances()) {
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
	public void testInvalidID() {
		try {
			kb.getBeliefById(new KBID("x", "y"));
			Assert.fail("Should have thrown a KBQueryException!");
		} catch (KBQueryException e) {
			// Expected behavior
		}
	}
}
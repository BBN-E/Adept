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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentMentalStateArgument;
import adept.common.DocumentRelation;
import adept.common.DocumentSentiment;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.KBID;
import adept.common.NumberPhrase;
import adept.common.NumericValue;
import adept.common.Pair;
import adept.common.RelationMention;
import adept.common.SentimentMention;
import adept.common.TemporalResolution;
import adept.common.TemporalSpan;
import adept.common.TemporalValue;
import adept.common.TimePhrase;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.common.XSDDate;
import adept.io.Reader;
import adept.kbapi.KBDate;
import adept.kbapi.KBEntity;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBRelationArgument;
import adept.kbapi.KBSentiment;
import adept.kbapi.KBTemporalSpan;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

import com.google.common.base.Optional;

public class TestSentiment extends KBUnitTest {
	private DocumentSentiment documentSentiment;
	private Map<Item, KBPredicateArgument> kbArgumentsMap;

	private final float initialSentimentConfidence = 0.9f;
	private final float sentimentMentionConfidence = 0.8f;

	private final float updatedSentimentConfidence = 0.7f;

	private KBEntity kbEntity1;
	private KBEntity kbEntity2;

	@Test
	public void testSentiment() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, IOException {
		// create sentiment
		buildSentiment(initialSentimentConfidence, sentimentMentionConfidence);

		KBSentiment.InsertionBuilder sentimentInsertionBuilder = KBSentiment
				.sentimentInsertionBuilder(documentSentiment, kbArgumentsMap,
						KBOntologyMap.getTACOntologyMap());
		sentimentInsertionBuilder.addExternalKBId(new KBID("External_Sentiment", "ExampleKB"));
		KBSentiment kbSentiment = sentimentInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbSentiment);

		testQuerySentimentById(kbSentiment.getKBID(), 1, initialSentimentConfidence,
				documentSentiment.getArguments().size(), sentimentMentionConfidence);
		testQuerySentimentByArg(kbEntity1.getKBID());
		testQuerySentimentByStringRef("BBN Technologies");

		KBRelationArgument argument = kbSentiment.getArguments().iterator().next();
		KBRelationArgument.UpdateBuilder argumentUpdateBuilder = argument.updateBuilder();
		argumentUpdateBuilder.setConfidence(0.2f);

		KBSentiment.UpdateBuilder updateBuilder = kbSentiment.updateBuilder();
		updateBuilder.addUpdatedArgument(argumentUpdateBuilder);
		updateBuilder.setConfidence(updatedSentimentConfidence);

		KBSentiment updatedSentiment = updateBuilder.update(kb);

		testQuerySentimentById(updatedSentiment.getKBID(), 1, updatedSentimentConfidence,
				documentSentiment.getArguments().size(), sentimentMentionConfidence);
		testQuerySentimentByArg(kbEntity1.getKBID());
		testQuerySentimentByStringRef("BBN Technologies");
	}

	private void buildSentiment(float sentimentConfidence, float sentimentMentionConfidence)
			throws KBUpdateException, KBQueryException, InvalidPropertiesFormatException,
			IOException {
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
				.createDefaultDocument(
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

		SentimentMention.Builder sentimentMentionBuilder = SentimentMention.builder();
		sentimentMentionBuilder.setConfidence(sentimentMentionConfidence);
		sentimentMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));
		sentimentMentionBuilder.addArgument(SentimentMention.Filler.createSourceArgument(
				entityWithMentions.getR().get(0), 0.51f));
		sentimentMentionBuilder.addArgument(SentimentMention.Filler.createStrengthArgument(
				numberPhrase, 0.64f));
		sentimentMentionBuilder.addArgument(SentimentMention.Filler.createTargetArgument(
				relationMention, 0.86f));
		sentimentMentionBuilder.addArgument(SentimentMention.Filler.createTargetArgument(
				relationMention, relationMentionArg, 0.72f));

		List<SentimentMention> provenances = new ArrayList<SentimentMention>();
		provenances.add(sentimentMentionBuilder.build());

		HltContentContainer dateHltContentContainer = new HltContentContainer();
		Document dateDocument = DocumentMaker.getInstance().createDefaultDocument(
				"sample_date.txt", null, "Text", "sample_date_1.txt", "English",
				Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_date.txt"),
				dateHltContentContainer);
		CharOffset testCharOff = new CharOffset(16, 24);
		String testText = "9/1/2015";
		Token testToken = new Token(0, testCharOff, testText);

		CharOffset test2CharOff = new CharOffset(46, 54);
		String test2Text = "9/3/2015";
		Token test2Token = new Token(1, test2CharOff, test2Text);
		TokenStream testTemporalSpanTokenStream = new TokenStream(TokenizerType.WHITESPACE,
				TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, dateDocument);
		TokenOffset testTemporalSpanTokenOffset = new TokenOffset(0, 0);
		TokenOffset test2TemporalSpanTokenOffset = new TokenOffset(1, 1);
		testTemporalSpanTokenStream.add(testToken);
		testTemporalSpanTokenStream.add(test2Token);

		SourceAlgorithm testSourceAlgorithm = new SourceAlgorithm("UnitTestAlgorithm", "BBN");

		XSDDate date1 = XSDDate.fromString("2015-09-01");

		TimePhrase timePhrase1 = new TimePhrase(testTemporalSpanTokenOffset,
				testTemporalSpanTokenStream, null);

		TemporalResolution temporalResolution1 = TemporalResolution.builder()
				.setResolvedTemporalValue(date1).setConfidence(0.5f).setTimePhrase(timePhrase1)
				.setSourceAlgorithm(testSourceAlgorithm).build();
		Set<TemporalResolution> temporalResolutions1 = new HashSet<TemporalResolution>();
		temporalResolutions1.add(temporalResolution1);

		KBDate.InsertionBuilder dateInsertionBuilder1 = KBDate.xsdDateInsertionBuilder(date1,
				temporalResolutions1);
		KBDate kbDate1 = dateInsertionBuilder1.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbDate1);
		assertTrue("Date KBID returned from insertXSDDate() is null", kbDate1.getKBID() != null);
		System.out.println("Inserted xsd date: " + kbDate1.getKBID().getObjectID());

		XSDDate date2 = XSDDate.fromString("2015-09-03");

		TimePhrase timePhrase2 = new TimePhrase(test2TemporalSpanTokenOffset,
				testTemporalSpanTokenStream, null);

		TemporalResolution temporalResolution2 = TemporalResolution.builder()
				.setResolvedTemporalValue(date2).setConfidence(0.5f).setTimePhrase(timePhrase2)
				.setSourceAlgorithm(testSourceAlgorithm).build();
		Set<TemporalResolution> temporalResolutions2 = new HashSet<TemporalResolution>();
		temporalResolutions2.add(temporalResolution2);

		KBDate.InsertionBuilder dateInsertionBuilder2 = KBDate.xsdDateInsertionBuilder(date2,
				temporalResolutions2);
		KBDate kbDate2 = dateInsertionBuilder2.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbDate2);
		assertTrue("Date KBID returned from insertXSDDate() is null", kbDate2.getKBID() != null);
		System.out.println("Inserted xsd date: " + kbDate2.getKBID().getObjectID());

		HashMap<TemporalValue, KBDate> datesMap = new HashMap<TemporalValue, KBDate>();
		datesMap.put(date1, kbDate1);
		datesMap.put(date2, kbDate2);

		TemporalSpan temporalSpan1 = TemporalSpan.builder().setBeginDate(date1).setEndDate(date2)
				.build();

		KBTemporalSpan.InsertionBuilder temporalSpanInsertionBuilder1 = KBTemporalSpan
				.temporalSpanInsertionBuilder(temporalSpan1, 1f, datesMap,
						Optional.of(new Float(1)), Optional.of(new Float(1)));
		KBTemporalSpan kbSpan1 = temporalSpanInsertionBuilder1.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbSpan1);

		// create document sentiment
		DocumentSentiment.Builder documentSentimentBuilder = DocumentSentiment.builder();
		documentSentimentBuilder.addProvenances(provenances);
		documentSentimentBuilder.setConfidence(sentimentConfidence);

		DocumentMentalStateArgument.Builder sourceArgBuilder = DocumentMentalStateArgument.builder(
				new Type("source"),
				DocumentMentalStateArgument.Filler.createSourceArgument(entityWithMentions.getL()),
				0.51f);
		sourceArgBuilder.addProvenance(provenances.get(0).getArguments().asList().get(0));

		documentSentimentBuilder.addArgument(sourceArgBuilder.build());
		documentSentimentBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("strength"),
				DocumentMentalStateArgument.Filler.createStrengthArgument(strengthNumericValue),
				0.64f).build());
		documentSentimentBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("target"),
				DocumentMentalStateArgument.Filler.createTargetArgument(relationWithEntityMap
						.getL()), 0.86f).build());
		documentSentimentBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("target"),
				DocumentMentalStateArgument.Filler.createTargetArgument(
						relationWithEntityMap.getL(), relationWithEntityMap.getL().getArguments()
								.iterator().next()), 0.72f).build());
		documentSentimentBuilder.addArgument(DocumentMentalStateArgument.builder(
				new Type("validTemporalSpan"),
				DocumentMentalStateArgument.Filler.createValidTemporalSpanArgument(temporalSpan1),
				0.44f).build());

		// map to Adept KB entities
		kbArgumentsMap = new HashMap<Item, KBPredicateArgument>();
		kbArgumentsMap.put(entityWithMentions.getL(), kbEntity1);
		kbArgumentsMap.put(relationWithEntityMap.getL(), kbRelation);
		kbArgumentsMap.put(strengthNumericValue, kbStrength);
		kbArgumentsMap.put(relationWithEntityMap.getL().getArguments().iterator().next()
				, kbRelationArg);
		kbArgumentsMap.put(temporalSpan1, kbSpan1);

		documentSentiment = documentSentimentBuilder.build();
	}

	private void testQuerySentimentById(KBID sentimentId, int expectedNumberOfProvenances,
			float expectedSentimentConfidence, int expectedNumberOfArguments,
			float expectedSentimentMentionConfidence) throws KBQueryException {
		KBSentiment sentiment = kb.getSentimentById(sentimentId);

		assertTrue("Sentiment returned is null.", sentiment != null);

		assertTrue("Expected " + expectedNumberOfProvenances
				+ " document relation provenances, got " + sentiment.getProvenances().size(),
				sentiment.getProvenances().size() == expectedNumberOfProvenances);

		assertTrue("Expected document sentiment confidence " + expectedSentimentConfidence
				+ " got " + sentiment.getConfidence() + ".",
				sentiment.getConfidence() == expectedSentimentConfidence);

		assertTrue("Expected " + expectedNumberOfArguments
				+ " arguments in sentiment mention, got " + sentiment.getArguments().size() + ".",
				sentiment.getArguments().size() == expectedNumberOfArguments);

		Iterator<KBProvenance> iterator = sentiment.getProvenances().iterator();
		while (iterator.hasNext()) {
			KBTextProvenance provenance = (KBTextProvenance) iterator.next();
			assertTrue("Expected sentiment mention confidence "
					+ expectedSentimentMentionConfidence + " got " + provenance.getConfidence()
					+ ".", provenance.getConfidence() == expectedSentimentMentionConfidence);
		}
	}

	private void testQuerySentimentByArg(KBID entityId) throws KBQueryException {
		boolean assertion = false;

		List<KBSentiment> sentiments = kb.getSentimentsByArg(entityId);
		int size = sentiments.size();
		assertTrue("Size of sentiments is not > 0", size > 0);

		for (KBSentiment sentiment : sentiments) {
			List<KBID> externalKbIds = kb.getExternalKBIDs(sentiment.getKBID());
			for (KBID kbId : externalKbIds) {
				if (kbId.getObjectID().equals("External_Sentiment")) {
					assertion = true;
					break;
				}
			}
		}

		assertTrue("assertion is false in testQuerySentimentByArg", assertion);
	}

	private void testQuerySentimentByStringRef(String stringReference) throws KBQueryException {
		List<KBSentiment> sentiments = kb.getSentimentsByStringReference(stringReference);

		int size = sentiments.size();
		assertTrue("Size of sentiments is not greater than zero", size > 0);

		for (KBSentiment sentiment : sentiments) {
			int mentionsSize = sentiment.getProvenances().size();
			assertTrue("Size of sentiment mentions is not greater than 0.", mentionsSize > 0);

			// for each mention, check presence of justification
			for (KBProvenance provenance : sentiment.getProvenances()) {
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
			kb.getSentimentById(new KBID("x", "y"));
			Assert.fail("Should have thrown a KBQueryException!");
		} catch (KBQueryException e) {
			// Expected behavior
		}
	}
}

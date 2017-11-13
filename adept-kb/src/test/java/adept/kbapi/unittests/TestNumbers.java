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


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.NumberPhrase;
import adept.common.NumericValue;
import adept.common.RelationMention;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.io.Reader;
import adept.kbapi.KBEntity;
import adept.kbapi.KBNumber;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBRelation;
import adept.kbapi.KBUpdateException;
import adept.utilities.DocumentMaker;

import static org.junit.Assert.assertTrue;

public class TestNumbers extends KBUnitTest {

	@Test
	public void testNumbers() throws KBQueryException, KBUpdateException,
			InvalidPropertiesFormatException, IOException, URISyntaxException {
		HltContentContainer hltContentContainer = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDocument("sample_numbers.txt",
				null, "Text", "sample_numbers.txt", "English",
				Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_numbers.txt"),
				hltContentContainer);

		CharOffset bbnCharOffset = new CharOffset(0, 17);
		String bbnText = "BBN Technologies";
		Token bbnToken = new Token(0, bbnCharOffset, bbnText);
		TokenOffset bbnTokenOffset = new TokenOffset(0, 0);

		CharOffset numberCharOffset = new CharOffset(35, 38);
		String numberText = "700";
		Token numberToken = new Token(1, numberCharOffset, numberText);
		TokenOffset numberTokenOffset = new TokenOffset(1, 1);

		TokenStream testTokenStream = new TokenStream(TokenizerType.WHITESPACE,
				TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
		testTokenStream.add(bbnToken);
		testTokenStream.add(numberToken);

		NumericValue numericValue = new NumericValue(new Integer(700));

		NumberPhrase numberPhrase = new NumberPhrase(numberTokenOffset, testTokenStream);

		KBNumber.InsertionBuilder numberInsertionBuilder1 = KBNumber.numberInsertionBuilder(
				numericValue, Collections.singletonList(numberPhrase));
		KBNumber number1 = numberInsertionBuilder1.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(number1);
		assertTrue("Number KBID returned from insertNumber() is null", number1.getKBID() != null);
		System.out.println("Inserted number: " + number1.getKBID().getObjectID());

		KBNumber.InsertionBuilder numberInsertionBuilder2 = KBNumber.numberInsertionBuilder(
				new NumericValue(new Integer(700)), new ArrayList<NumberPhrase>());
		KBNumber number2 = numberInsertionBuilder2.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(number2);

		// Inserting the same number twice should yield the same id
		Assert.assertEquals("Inserting the same number twice did not yield same id",
				number1.getKBID(), number2.getKBID());

		KBNumber value = kb.getNumberValueByID(number1.getKBID());
		Assert.assertTrue("Number returned by getNumberValueByID() is not an Integer",
				value.getNumber() instanceof Integer);
		Assert.assertEquals("Incorrect number returned by getNumberValueByID(), expected 700 got "
				+ value.getNumber().intValue(), 700, value.getNumber().intValue());

		EntityMention entityMention = new EntityMention(0, bbnTokenOffset, testTokenStream);
		entityMention.setEntityType(new Type("org"));
		entityMention.setMentionType(new Type("NAME"));

		Entity entity = new Entity(0, new Type("org"));
		entityMention.addEntityConfidencePair(0, 1.0f);
		entity.setCanonicalMentions(entityMention);
		entity.setCanonicalMentionConfidence(0.7f);

		KBEntity.InsertionBuilder entityInsertionBuilder = KBEntity.entityInsertionBuilder(entity,
				Collections.singletonList(entityMention), KBOntologyMap.getTACOntologyMap());
		KBEntity kbEntity = entityInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbEntity);

		RelationMention relationMention = RelationMention
				.builder(new Type("org:number_of_employees_members"))
				.setConfidence(0.9f)
				.addJustification(entityMention)
				.addArgument(
						RelationMention.Filler.fromNumberPhrase(numberPhrase, new Type(
								"org:number_of_employees_members.arg-2"), 0.5f))
				.addArgument(
						RelationMention.Filler.fromEntityMention(entityMention, new Type(
								"org:number_of_employees_members.arg-1"), .4f)).build();

		DocumentRelation documentRelation = DocumentRelation
				.builder(new Type("org:number_of_employees_members"))
				.setConfidence(0.9f)
				.addArgument(
						DocumentRelationArgument.builder(
								new Type("org:number_of_employees_members.arg-1"),
								DocumentRelationArgument.Filler.fromEntity(entity), .4f).build())
				.addArgument(
						DocumentRelationArgument.builder(
								new Type("org:number_of_employees_members.arg-2"),
								DocumentRelationArgument.Filler.fromNumericValue(numericValue),
								0.5f).build()).addProvenance(relationMention).build();

		Map<Item, KBPredicateArgument> kbArgumentMap = new HashMap<Item, KBPredicateArgument>();
		kbArgumentMap.put(entity, kbEntity);
		kbArgumentMap.put(numericValue, number1);

		KBRelation.InsertionBuilder relationInsertionBuilder = KBRelation.relationInsertionBuilder(
				documentRelation, kbArgumentMap, KBOntologyMap.getTACOntologyMap());
		KBRelation kbRelation = relationInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbRelation);

		KBRelation relation2 = kb.getRelationById(kbRelation.getKBID());
		Assert.assertEquals("Incorrect relation arguments size, expected "
				+ documentRelation.getArguments().size() + " got "
				+ relation2.getArguments().size(), documentRelation.getArguments().size(),
				relation2.getArguments().size());

		Assert.assertTrue("Unable to delete number kb object", kb.deleteKBObject(number1.getKBID()));
		Assert.assertTrue("Unable to delete entity kb object",
				kb.deleteKBObject(kbEntity.getKBID()));
		Assert.assertTrue("Unable to delete relation kb object",
				kb.deleteKBObject(kbRelation.getKBID()));
	}

	@Test
	public void testNumberExistingProvenances() throws KBUpdateException, KBQueryException {
		KBNumber number = KBNumber.numberInsertionBuilder(3)
				.addProvenance(generateProvenance("three")).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(number);

		Assert.assertEquals("Wrong number of provenances", 1, number.getProvenances().size());

		KBNumber number2 = KBNumber.numberInsertionBuilder(3).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(number);

		Assert.assertEquals("Wrong number of provenances", 1, number2.getProvenances().size());
		Assert.assertEquals("Numbers should equal", number, number2);
		KBNumber queriedNumber = kb.getNumberByValue(3).get();

		Assert.assertEquals("Wrong number of provenances", 1, number2.getProvenances().size());
		Assert.assertEquals("Numbers should equal", number, queriedNumber);

	}
}
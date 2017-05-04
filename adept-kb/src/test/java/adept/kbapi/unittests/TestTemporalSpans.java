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
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.KBID;
import adept.common.TemporalResolution;
import adept.common.TemporalSpan;
import adept.common.TemporalValue;
import adept.common.TimePhrase;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.XSDDate;
import adept.io.Reader;
import adept.kbapi.KBDate;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTemporalSpan;
import adept.kbapi.KBUpdateException;
import adept.kbapi.sparql.SparqlQueryBuilder;
import adept.kbapi.sparql.SparqlUtils;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

import com.google.common.base.Optional;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class TestTemporalSpans extends KBUnitTest {

	@Test
	public void testTemporalSpans() throws KBQueryException, KBUpdateException,
			FileNotFoundException, URISyntaxException {
		HltContentContainer hltContentContainer = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDocument("sample_date.txt",
				null, "Text", "sample_date_1.txt", "English",
				Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_date.txt"),
				hltContentContainer);
		CharOffset testCharOff = new CharOffset(16, 24);
		String testText = "9/1/2015";
		Token testToken = new Token(0, testCharOff, testText);

		CharOffset test2CharOff = new CharOffset(46, 54);
		String test2Text = "9/3/2015";
		Token test2Token = new Token(1, test2CharOff, test2Text);
		TokenStream testTemporalSpanTokenStream = new TokenStream(TokenizerType.WHITESPACE,
				TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
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

		TemporalSpan temporalSpan2 = TemporalSpan.builder().setBeginDate(date2).build();

		KBTemporalSpan.InsertionBuilder temporalSpanInsertionBuilder2 = KBTemporalSpan
				.temporalSpanInsertionBuilder(temporalSpan2, 1f, datesMap,
						Optional.of(new Float(1)), Optional.<Float> absent());
		KBTemporalSpan kbSpan2 = temporalSpanInsertionBuilder2.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbSpan2);

		TemporalSpan temporalSpan3 = TemporalSpan.builder().setEndDate(date1).build();

		KBTemporalSpan.InsertionBuilder temporalSpanInsertionBuilder3 = KBTemporalSpan
				.temporalSpanInsertionBuilder(temporalSpan3, 1f, datesMap,
						Optional.<Float> absent(), Optional.of(new Float(1)));
		KBTemporalSpan kbSpan3 = temporalSpanInsertionBuilder3.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbSpan3);

		String query = SparqlQueryBuilder.PREFIXES + "SELECT ?temporalSpan ?beginDate ?endDate\r\n"
				+ "WHERE { \r\n" + "	?temporalSpan a adept-base:TemporalSpan\r\n"
				+ "    OPTIONAL{\r\n" + "    	?temporalSpan adept-base:beginDate ?beginDate\r\n"
				+ "    }\r\n" + "    OPTIONAL{\r\n"
				+ "        ?temporalSpan adept-base:endDate ?endDate\r\n" + "  }\r\n" + "}";
		ResultSet resultSet = kb.executeSelectQuery(query);

		boolean foundSpan1 = false;
		boolean foundSpan2 = false;
		boolean foundSpan3 = false;

		while (resultSet.hasNext()) {
			QuerySolution solution = resultSet.next();
			String spanID = SparqlUtils.getLocalName(solution.getResource("?temporalSpan"));
			String beginID = solution.contains("?beginDate") ? SparqlUtils.getLocalName(solution
					.getResource("?beginDate")) : null;
			String endID = solution.contains("?endDate") ? SparqlUtils.getLocalName(solution
					.getResource("?endDate")) : null;

			if (spanID.equals(kbSpan1.getKBID().getObjectID())) {
				foundSpan1 = true;
				Assert.assertEquals("Incorrect beginID for kbSpan1", kbDate1.getKBID()
						.getObjectID(), beginID);
				Assert.assertEquals("Incorrect endID for kbSpan1", kbDate2.getKBID().getObjectID(),
						endID);
			}

			if (spanID.equals(kbSpan2.getKBID().getObjectID())) {
				foundSpan2 = true;
				Assert.assertEquals("Incorrect beginID for kbSpan2", kbDate2.getKBID()
						.getObjectID(), beginID);
				Assert.assertNull("endID is not null for kbSpan2", endID);
			}

			if (spanID.equals(kbSpan3.getKBID().getObjectID())) {
				foundSpan3 = true;
				Assert.assertNull("beginID is not null for kbSpan3", beginID);
				Assert.assertEquals("Incorrect endID for kbSpan3", kbDate1.getKBID().getObjectID(),
						endID);
			}

		}

		Assert.assertTrue("Did not find kbSpan1", foundSpan1);
		Assert.assertTrue("Did not find kbSpan2", foundSpan2);
		Assert.assertTrue("Did not find kbSpan3", foundSpan3);
	}

	@Test
	public void testInvalidID() {
		try {
			kb.getTemporalSpanByID(new KBID("x", "y"));
			Assert.fail("Should have thrown a KBQueryException!");
		} catch (KBQueryException e) {
			// Expected behavior
		}
	}
}

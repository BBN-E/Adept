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
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.TemporalResolution;
import adept.common.TimePhrase;
import adept.common.TimexValue;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.XSDDate;
import adept.io.Reader;
import adept.kbapi.KBDate;
import adept.kbapi.KBProvenance;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

public class TestDates extends KBUnitTest {

	@Test
	public void testDates() throws KBQueryException, KBUpdateException, FileNotFoundException, URISyntaxException {
		HltContentContainer hltContentContainer = new HltContentContainer();
		Document document = DocumentMaker.getInstance().createDocument("sample_date.txt",
				null, "Text", "sample_date_1.txt", "English",
				Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_date.txt"),
				hltContentContainer);
		CharOffset testCharOff = new CharOffset(16, 24);
		String testText = "9/1/2015";
		Token testToken = new Token(0, testCharOff, testText);
		TokenStream testDateTokenStream = new TokenStream(TokenizerType.WHITESPACE,
				TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
		TokenOffset testDateTokenOffset = new TokenOffset(0, 0);
		testDateTokenStream.add(testToken);

		SourceAlgorithm testSourceAlgorithm = new SourceAlgorithm("UnitTestAlgorithm", "BBN");

		XSDDate xsdDate = XSDDate.fromString("2015-09-01");

		TimePhrase timePhrase = new TimePhrase(testDateTokenOffset, testDateTokenStream, null);

		TemporalResolution xsdTemporalResolution = TemporalResolution.builder()
				.setResolvedTemporalValue(xsdDate).setConfidence(0.5f).setTimePhrase(timePhrase)
				.setSourceAlgorithm(testSourceAlgorithm).build();

		Set<TemporalResolution> xsdTemporalResolutions = new HashSet<TemporalResolution>();
		xsdTemporalResolutions.add(xsdTemporalResolution);
		KBDate.InsertionBuilder xsdDateInsertionBuilder = KBDate.xsdDateInsertionBuilder(xsdDate,
				xsdTemporalResolutions);
		KBDate kbXsdDate = xsdDateInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbXsdDate);

		assertTrue("Date KBID returned from KBDate.InsertionBuilder.insert() is null",
				kbXsdDate.getKBID() != null);
		System.out.println("Inserted xsd date: " + kbXsdDate.getKBID().getObjectID());

		TimexValue timexValue = TimexValue.fromString("2015-09-01");
		TimePhrase timexTimePhrase = new TimePhrase(testDateTokenOffset, testDateTokenStream, null);

		TemporalResolution timexTemporalResolution = TemporalResolution.builder()
				.setResolvedTemporalValue(timexValue).setConfidence(0.5f)
				.setTimePhrase(timexTimePhrase).setSourceAlgorithm(testSourceAlgorithm).build();

		Set<TemporalResolution> timexTemporalResolutions = new HashSet<TemporalResolution>();
		timexTemporalResolutions.add(timexTemporalResolution);
		KBDate.InsertionBuilder timexDateInsertionBuilder = KBDate.timexInsertionBuilder(
				timexValue, timexTemporalResolutions);
		KBDate kbTimexDate = timexDateInsertionBuilder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(kbTimexDate);

		assertTrue("Date KBID returned from insertTimexValue() is null",
				kbTimexDate.getKBID() != null);
		System.out.println("Inserted timex date: " + kbTimexDate.getKBID().getObjectID());

		KBDate queriedForDate = kb.getKBDateByDateId(kbTimexDate.getKBID());

		Set<KBProvenance> temporalResolutions = queriedForDate.getProvenances();
		Assert.assertEquals(
				"Incorrect number of temporal resolutions returned from getAssociatedTimePhrasesForDate(), expected 1 got "
						+ temporalResolutions.size(), 2, temporalResolutions.size());

		KBDate xsdDateId2 = kb.getDateByXSDDateValue("2015-09-01").get();

		Assert.assertNotNull("Date KBID returned from getDateByXSDDateValue() is null", xsdDateId2);
		Assert.assertEquals("Incorrect kbid returned from getDateByXSDDateValue()",
				kbXsdDate.getKBID(), xsdDateId2.getKBID());

		KBDate timexDateId2 = kb.getDateByTimex2Value("2015-09-01").get();

		Assert.assertNotNull("Date KBID returned from getDateByTimex2Value() is null", timexDateId2);
		Assert.assertEquals("Incorrect kbid returned from getDateByTimex2Value()",
				kbTimexDate.getKBID(), timexDateId2.getKBID());

		Assert.assertTrue("Unable to delete xsdDate kb object",
				kb.deleteKBObject(kbXsdDate.getKBID()));
		Assert.assertTrue("Unable to delete timexDate kb object",
				kb.deleteKBObject(timexDateId2.getKBID()));
	}

	@Test
	public void testDateExistingProvenances() throws KBUpdateException, KBQueryException {
		KBDate date = KBDate.timexInsertionBuilder("2015-11-23")
				.addProvenance(generateProvenance("three")).insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(date);

		Assert.assertEquals("Wrong number of provenances", 1, date.getProvenances().size());

		KBDate date2 = KBDate.timexInsertionBuilder("2015-11-23").insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(date2);

		Assert.assertEquals("Wrong number of provenances", 1, date2.getProvenances().size());
		Assert.assertEquals("Dates should equal", date, date2);
		KBDate queriedDate = kb.getDateByTimex2Value("2015-11-23").get();

		Assert.assertEquals("Wrong number of provenances", 1, date2.getProvenances().size());
		Assert.assertEquals("Dates should equal", date, queriedDate);

	}
}

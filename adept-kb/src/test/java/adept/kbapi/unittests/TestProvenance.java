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

import org.junit.Assert;
import org.junit.Test;

import adept.kbapi.KBNumber;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBTextProvenance;
import adept.kbapi.KBUpdateException;

/**
 * 
 * @author dkolas
 */
public class TestProvenance extends KBUnitTest {

	@Test
	public void testDocumentID() throws KBUpdateException, KBQueryException {
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(3);

		builder.addProvenance(generateProvenance("three"));
		KBNumber three = builder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(three);

		Assert.assertEquals("Document ID incorrect", "DocumentID1", ((KBTextProvenance) three
				.getProvenances().iterator().next()).getDocumentID());

		KBNumber queriedThree = kb.getNumberByValue(3).get();
		Assert.assertEquals("Document ID incorrect", "DocumentID1",
				((KBTextProvenance) queriedThree.getProvenances().iterator().next())
						.getDocumentID());
		Assert.assertEquals(
				"Inserted number's provenances differ from queried number's provenances",
				three.getProvenances(), queriedThree.getProvenances());
	}

	@Test
	public void testCorpusID() throws KBUpdateException, KBQueryException {
		KBNumber.InsertionBuilder builder = KBNumber.numberInsertionBuilder(3);

		builder.addProvenance(generateProvenance("three"));
		KBNumber three = builder.insert(kb);
		assertEqualsAndHashCodeByQueryByKBID(three);

		Assert.assertEquals("Corpus ID incorrect", "CorpusID1", ((KBTextProvenance) three
				.getProvenances().iterator().next()).getCorpusID());

		KBNumber queriedThree = kb.getNumberByValue(3).get();
		Assert.assertEquals("Document ID incorrect", "CorpusID1", ((KBTextProvenance) queriedThree
				.getProvenances().iterator().next()).getCorpusID());
		Assert.assertEquals(
				"Inserted number's provenances differ from queried number's provenances",
				three.getProvenances(), queriedThree.getProvenances());
	}
}

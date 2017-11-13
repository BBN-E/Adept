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

import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;

/**
 * 
 * @author dkolas
 */
public class TestDocumentText extends KBUnitTest {

	@Test
	public void testDocumentTexts() throws KBUpdateException, KBQueryException {

		String docid1 = "docid1";
		String corpus1 = "corpus1";
		String docid2 = "docid2";
		String corpus2 = "corpus2";
		String text1 = "text1";
		String text2 = "text2";

		Assert.assertTrue("No document text should exist", !kb.getDocumentText(docid1, corpus1)
				.isPresent());
		Assert.assertTrue("No document text should exist", !kb.getDocumentText(docid2, corpus1)
				.isPresent());

		kb.saveDocumentText(docid1, corpus1, text1);
		kb.saveDocumentText(docid2, corpus1, text2);

		Assert.assertEquals("Texts should be the same", text1, kb.getDocumentText(docid1, corpus1)
				.get());
		Assert.assertEquals("Texts should be the same", text2, kb.getDocumentText(docid2, corpus1)
				.get());

		Assert.assertTrue("No document text should exist", !kb.getDocumentText(docid1, corpus2)
				.isPresent());

		kb.deleteDocumentText(docid1, corpus1);
		kb.deleteDocumentText(docid2, corpus1);

		Assert.assertTrue("No document text should exist", !kb.getDocumentText(docid1, corpus1)
				.isPresent());
		Assert.assertTrue("No document text should exist", !kb.getDocumentText(docid2, corpus1)
				.isPresent());
	}

}
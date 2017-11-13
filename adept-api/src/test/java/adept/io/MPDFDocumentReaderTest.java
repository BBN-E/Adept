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

package adept.io;

/*-
 * #%L
 * adept-api
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

import adept.common.Document;
import adept.common.HltContentContainer;

/**
 *
 */
public class MPDFDocumentReaderTest {

  @Test
  public void testCreateMPDFDocument() {
    String pathString =  ClassLoader.getSystemResource("adept/io/sample_mpdf.mpdf.xml").getPath();
    HltContentContainer hltcc = MPDFDocumentReader.getInstance().createMPDFDocument(
        pathString);
    Assert.assertNotNull(hltcc);
    Document mpdfDoc = hltcc.getDocument();
    Assert.assertNotNull(mpdfDoc);
    Assert.assertEquals("000e2b447698f12aeb2bf639e8ae38bd", mpdfDoc.getDocId());
    Assert.assertNull(mpdfDoc.getName());
    Assert.assertEquals(5, hltcc.getPassages().size());
    Assert.assertEquals(5,hltcc.getConversationElements().size());
  }

}
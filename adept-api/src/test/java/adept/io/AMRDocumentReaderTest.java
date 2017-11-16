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

import adept.common.HltContentContainer;

/**
 *
 */
public class AMRDocumentReaderTest {

  @Test
  public void testCreateAMRDocument() {
    String pathString =  ClassLoader.getSystemResource("adept/io/amr-bank-struct-v1.6-test.txt").getPath();
    AMRDocument doc = AMRDocumentReader.getInstance().loadAMRDocument(pathString);
    Assert.assertNotNull(doc);
  }

  @Test
  public void testCreateHltContentContainer() {
    String pathString =  ClassLoader.getSystemResource("adept/io/amr-bank-struct-v1.6-test.txt").getPath();
    HltContentContainer hltcc = AMRDocumentReader.getInstance().createAMRContentContainer(pathString);
    Assert.assertNotNull(hltcc);
    Assert.assertEquals(hltcc.getSentences().size(), 143);
  }
}

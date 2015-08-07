/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.utilities.DocumentMaker;


/**
 * The Class TestLDCCorpusReader.
 */
public class TestLDCCorpusReader {

	/**
	 * Test.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void test(String filename) {
        // TODO HLTCC not used
        HltContentContainer hltContentContainer = new HltContentContainer();
        Document document = DocumentMaker.getInstance().createDefaultDocument( filename, hltContentContainer);
		System.out.println("Metadata: " + document.getDocId() + "\n" + document.getDocType()
				+ "\n" + document.getHeadline());
		//System.out.println("Value: \n" + document.getValue());
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		TestLDCCorpusReader reader = new TestLDCCorpusReader();
		reader.test("adept/module/eng-NG-31-99998-10757220.sgm");
	}

}
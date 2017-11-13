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

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.io.Reader;

// TODO: Auto-generated Javadoc
/**
 * The Class TestLDCCorpusReader.
 */
public class TestCoNLLReader {

	/**
	 * Test.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void test(String filename) {
        HltContentContainer hltcc = Reader.getInstance().CoNLLtoHltContentContainer( filename );
		System.out.println("Document: " + hltcc.getDocument() + "  Doc text: " + hltcc.getDocument().getValue());
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		try
		{
			TestCoNLLReader reader = new TestCoNLLReader();
			reader.test(Reader.getAbsolutePathFromClasspathOrFileSystem("adept/module/wsj_0020.v2_gold_skel"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}

}
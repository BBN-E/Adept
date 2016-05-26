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


package adept.example;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import adept.common.Document;
import adept.common.EntityMention;
import adept.common.Sentence;
import adept.common.TokenOffset;
import adept.common.TokenizerType;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.io.Writer;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;
import adept.utilities.DocumentMaker;


// TODO: Auto-generated Javadoc
/**
 * Simple class to test passage tokenization.
 */
public class ExamplePassageTokenizerTest {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	@Test
	public void TestRawTextMultipleLinesFile() {
		
	    HltContentContainer hltContentContainer = new HltContentContainer();
		
	    Document doc = DocumentMaker.getInstance().createDefaultDocument(null, null, null, null, "English",
				ClassLoader.getSystemResource("ExamplePassageTokenizerTest.txt").getFile(),hltContentContainer);
		
		for(Passage p : hltContentContainer.getPassages()) {			
			System.out.println("PASSAGE::: " + p.getTokenOffset().getBegin() + "  " + p.getTokenOffset().getEnd());
		}
			String xmlFilename = System.getProperty("user.dir") + "/ExamplePassageTokenizerTest.hcc.xml";
			if ( hltContentContainer != null) {
			String xmlTemp = null;
			try {
				XMLSerializer xmlSerializer = new XMLSerializer(SerializationType.XML);
				xmlTemp = xmlSerializer.serializeAsString(hltContentContainer);
				//System.out.println(xmlTemp);
				if ( !isNullOrEmpty(xmlFilename) )
				{
					System.out.println("Writing to:  " + xmlFilename);
					Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}	    	
	}
	
	private boolean isNullOrEmpty(String s) {
		if ( s == null ) return true;
		return s.isEmpty();
	}

}

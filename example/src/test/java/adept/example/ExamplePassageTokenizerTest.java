/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
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

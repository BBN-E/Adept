/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.io;

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.utilities.DocumentMaker;

// TODO: Auto-generated Javadoc
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
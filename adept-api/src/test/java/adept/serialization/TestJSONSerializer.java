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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.serialization;

import java.io.UnsupportedEncodingException;

import adept.common.Document;
import adept.utilities.DocumentMaker;

// TODO: Auto-generated Javadoc
/**
 * The Class TestJSONSerializer.
 */
public class TestJSONSerializer {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		Document document = DocumentMaker.getInstance().createDefaultDocument(
				"001",
				null,
				"text",
				null,
				"English",
				ClassLoader.getSystemResource(
						"adept/module/eng-NG-31-100100-8816523.sgm").getFile(),
                null);

		// Initialize serializer instance
		JSONSerializer jsons = new JSONSerializer(SerializationType.JSON);

		// serialize
		String serialized = jsons.serializeAsString(document);

		// deserialize
		Document deserialized = (Document) jsons.deserializeString(serialized,
				Document.class);

		// test results from deserialization
		System.out
				.println("Printing out the document object just deserialized:");
		System.out.println("Doc ID = " + deserialized.getDocId());
		System.out.println("Doc type is: " + deserialized.getDocType());
		System.out.println("Doc language is: " + deserialized.getLanguage());
		System.out.println("The document text is: " + deserialized.getValue());
		System.out.println("19th token: "
				+ deserialized.getTokenStreamList().get(0).get(18).getValue());
	}
}

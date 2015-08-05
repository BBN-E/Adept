/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.serialization;

import adept.common.Document;
import adept.utilities.DocumentMaker;


/**
 * The Class TestBinaryFromJSONSerializer.
 */
public class TestBinaryFromJSONSerializer {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Exception
	 *             the exception
	 */
	public static void main(String[] args) throws Exception {
		Document document = DocumentMaker.getInstance().createDefaultDocument(
				"001",
				null,
				"text",
				null,
				"English",
				ClassLoader.getSystemResource(
						"adept/test/eng-NG-31-100100-8816523.sgm").getFile(),
                null);

		// Printing out fields of the object just created
		System.out.println("Printing out the document object just created:");
		System.out.println("Doc ID = " + document.getDocId());
		System.out.println("Doc type is: " + document.getDocType());
		System.out.println("Doc language is: " + document.getLanguage());
		// System.out.println("The document text is: " + document.getValue());
		System.out.println("19th token: "
				+ document.getTokenStreamList().get(0).get(18).getValue());

		// Create an instance of BinarySerializer
		BinaryFromJSONSerializer bs = new BinaryFromJSONSerializer(
				SerializationType.BINARY);

		// Serialization
		byte[] serialized = bs.serializeAsByteArray(document);

		// Deserialization
		Document deserialized = (Document) bs.deserializeByteArray(serialized,
				Document.class);

		// Printing out fields of the deserialized object
		System.out.println("\n"
				+ "Printing out the document object just deserialized:");
		System.out.println("document obj: " + deserialized);
		System.out.println("Doc ID = " + deserialized.getDocId());
		System.out.println("Doc type is: " + deserialized.getDocType());
		System.out.println("Doc language is: " + deserialized.getLanguage());
		// System.out.println("The document text is: " +
		// deserialized.getValue());
		System.out.println("19th token: "
				+ deserialized.getTokenStreamList().get(0).get(18).getValue());
	}
}

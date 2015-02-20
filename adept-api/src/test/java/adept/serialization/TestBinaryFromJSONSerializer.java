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

package adept.serialization;

import adept.common.Document;
import adept.utilities.DocumentMaker;

// TODO: Auto-generated Javadoc
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
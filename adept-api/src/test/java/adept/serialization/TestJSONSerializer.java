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
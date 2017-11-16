package adept.serialization;

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

import java.io.UnsupportedEncodingException;
import adept.common.Document;
import adept.utilities.DocumentMaker;

public class TestXMLStringSerializer {
	public static void main(String[] args) throws UnsupportedEncodingException {
		AbstractStringSerializer serializer = new XMLStringSerializer();
		Document document = DocumentMaker.getInstance().createDocument(
				"001",
				null,
				"text",
				null,
				"English",
				ClassLoader.getSystemResource(
						"adept/module/eng-NG-31-100100-8816523.sgm").getFile(),
				null);

		// Printing out fields of the object just created
		System.out.println("Printing out the document object that was just created:");
		System.out.println("Doc ID = " + document.getDocId());
		System.out.println("Doc type is: " + document.getDocType());
		System.out.println("Doc language is: " + document.getLanguage());
		//System.out.println("The document text is: " + document.getValue());
		System.out.println("19th token: "
				+ document.getTokenStreamList().get(0).get(18).getValue());

		// serialize and de-serialize
		String serialized = serializer.serializeToString(document);
		Document deserialized = (Document) serializer.deserializeFromString(serialized, Document.class);

		// test results from deserialization
		System.out.println("Printing out the document object from just deserialized string:");
		System.out.println("Doc ID = " + deserialized.getDocId());
		System.out.println("Doc type is: " + deserialized.getDocType());
		System.out.println("Doc language is: " + deserialized.getLanguage());
		//System.out.println("The document text is: " + deserialized.getValue());
		System.out.println("19th token: "
				+ deserialized.getTokenStreamList().get(0).get(18).getValue());
	}
}

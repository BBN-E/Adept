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

import java.io.UnsupportedEncodingException;

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.utilities.DocumentMaker;

// TODO: Auto-generated Javadoc
/**
 * The Class TestXMLSerializer.
 */
public class TestXMLSerializer {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {

        // TODO HLTCC not used
        HltContentContainer hltContentContainer = new HltContentContainer();
        Document document = DocumentMaker.getInstance().createDefaultDocument( ClassLoader.getSystemResource("adept/test/sample.txt").getFile(),
                hltContentContainer);

		// print out fields of the object just created
		System.out.println("Printing out the document object just created:");
		System.out.println("Doc ID = " + document.getDocId());
		System.out.println("Doc type is: " + document.getDocType());
		System.out.println("Doc language is: " + document.getLanguage());
		System.out.println("The document text is: " + document.getValue());
		System.out.println("Token stream is: " + document.getTokenStreamList());

		// Initialize serializer instance
		XMLSerializer xmls = new XMLSerializer(SerializationType.XML);

		// serialize
		String serialized = xmls.serializeAsString(document);

		// deserialize
		Document deserialized = (Document) xmls.deserializeString(serialized,
				Document.class);

		// print fields of the deserialized object
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
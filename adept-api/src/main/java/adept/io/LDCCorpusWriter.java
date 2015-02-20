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

package adept.io;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// TODO: Auto-generated Javadoc
/**
 * The Class LDCCorpusWriter.
 */
public class LDCCorpusWriter {
	/**
	 * Singleton instance and getter method.
	 */
	private static LDCCorpusWriter instance;

	/**
	 * Gets the single instance of LDCCorpusWriter.
	 * 
	 * @return single instance of LDCCorpusWriter
	 */
	public static LDCCorpusWriter getInstance() {
		if (instance == null)
			instance = new LDCCorpusWriter();
		return instance;
	}

	/**
	 * writes the DOM format document for specific tags.
	 * 
	 * @param doc
	 *            the doc
	 * @return the adept.common. document
	 */
	public adept.common.Document writeCorpus(org.w3c.dom.Document doc) {
		adept.common.Document adeptDocument = null;
		String docID = null, headline = null, docType = null, value = null;
		StringBuffer sb = new StringBuffer();
		doc.getDocumentElement().normalize();

		/** get text */
		Element textElement = (Element) doc.getElementsByTagName("TEXT")
				.item(0);
		NodeList passages = textElement.getElementsByTagName("P");
		if (passages.getLength() > 0) {
			System.out.println("passages: " + passages.toString() + " "
					+ passages.getLength());
			for (int i = 0; i < passages.getLength(); i++) {
				Node passage = passages.item(i).getFirstChild();
				if (passage != null) {
					sb.append(passage.getNodeValue());
					sb.append("\n");
				}
			}

			/** get adept Document ID and type */
			Element DOCElement = (Element) doc.getElementsByTagName("DOC")
					.item(0);
			docID = DOCElement.getAttribute("id");
			docType = DOCElement.getAttribute("type");
		} else {
			Element bodyElement = (Element) doc.getElementsByTagName("BODY")
					.item(0);
			textElement = (Element) bodyElement.getElementsByTagName("TEXT")
					.item(0);
			Element postElement = (Element) textElement.getElementsByTagName(
					"POST").item(0);
			NodeList nodes = postElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node child = nodes.item(i);
				if (child.getNodeType() == Node.TEXT_NODE)
					sb.append(child.getTextContent());
			}

			/** get adept Document ID */
			docID = doc.getElementsByTagName("DOCID").item(0).getFirstChild()
					.getNodeValue();

			/** get adept Document Type */
			docType = doc.getElementsByTagName("DOCTYPE").item(0)
					.getFirstChild().getNodeValue();
		}

		value = sb.toString().trim();
		
		/** convert to utf-8 */
		Charset charset = Charset.forName("UTF-8");
		try
		{			
			ByteBuffer bbuf = charset.newEncoder().encode(CharBuffer.wrap(value));
		    CharBuffer cbuf = charset.newDecoder().decode(bbuf);
		    value = cbuf.toString();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		/** get headline */
		headline = "";
		Element headlineElement = (Element) doc.getElementsByTagName("HEADLINE").item(0);
		if ( headlineElement != null 
			&& headlineElement.getFirstChild() != null) 
		{
			headline = headlineElement.getFirstChild().getNodeValue();
		}
		
		/** create adept document and return */
		adeptDocument = new adept.common.Document(docID, null, docType,
				"English", null);
		adeptDocument.setValue(value);
		adeptDocument.setHeadline(headline);
		return adeptDocument;
	}
}
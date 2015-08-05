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

import adept.common.Corpus;
import adept.utilities.PassageAttributes;

import java.util.*;

import adept.common.*;


public class WMSReader {
	
	/**
	 * Singleton instance and getter method.
	 */
	private static WMSReader instance;

	/**
	 * Gets the single instance of WMSReader.
	 * 
	 * @return single instance of WMSReader
	 */
	public static WMSReader getInstance() {
		if (instance == null)
			instance = new WMSReader();
		return instance;
	}
	
	
	/**
	 * Parse the document and return ADEPT Document instance
	 * @return ADEPT Document object
	 */
    public Document getDocument(org.w3c.dom.Document doc,List<PassageAttributes> passageAttributesList, Corpus corpus, String uri, String language)
    {
    	try
    	{
    		doc.getDocumentElement().normalize();
    		
    		/** get text */
    		StringBuffer docText = new StringBuffer();
    		StringBuffer passageText;
    		Element allPassages = (Element) doc.getElementsByTagName("passages").item(0);
    		NodeList passagesList = allPassages.getElementsByTagName("passage");
    		
    		for(int passageIndex = 0; passageIndex < passagesList.getLength(); passageIndex++)
    		{
    			passageText = new StringBuffer();
    			Node passage = passagesList.item(passageIndex);
    			 
    			if (passage.getNodeType() == Node.ELEMENT_NODE) 
    			{
    			   Element passageElem = (Element) passage;
    			   
    			   Element textPassage = (Element) passageElem.getElementsByTagName("text_passage").item(0);
    			   String passageId = textPassage.getAttribute("passage_id");
    			   
    			   NodeList passageTokens = textPassage.getElementsByTagName("text_token");
    			   for(int tokenIndex = 0; tokenIndex < passageTokens.getLength(); tokenIndex++)
    			   {
    				   Node token = passageTokens.item(tokenIndex);
    				   Element tokenElem = (Element) token;
    				   passageText.append(tokenElem.getAttribute("token_print_form") + " ");
    				   docText.append(tokenElem.getAttribute("token_print_form") + " ");
    			   }
    			   
    			   // create a passageAttributes instance
    			   PassageAttributes passageAttr = new PassageAttributes();
    			   passageAttr.setPassageId(Integer.parseInt(passageId));
    			   passageAttr.setValue(passageText.toString().trim());
    			   passageAttr.setSarcasmValue(null);
    			   passageAttributesList.add(passageAttr);
    			}
    		}
        	
        	/** get Headers */
        	String docID = null, docType = null;
        	Node docHeader = doc.getElementsByTagName("source_text_page").item(0);
        	Element docHeaderElem = (Element) docHeader;
        	
        	docID = docHeaderElem.getAttribute("source_id");
        	docType = "WMS";
        	uri = docHeaderElem.getAttribute("source_uri");
        	language = docHeaderElem.getAttribute("source_language");
        	
    		Document adeptDocument = new adept.common.Document(docID, corpus, docType, uri, language);
    		adeptDocument.setValue(docText.toString().trim());
    		
    		// Debug statements
    		System.out.println("Document ID is: " + docID);
    		System.out.println("URI is: " + uri);
    		System.out.println("Language is: " + language);
    		System.out.println("Document text: " + adeptDocument.getValue());
    		
    		System.out.println("Printing passages...");
    		for(PassageAttributes pa : passageAttributesList)
    		{
    			System.out.println(pa.getPassageId() + "\t" + pa.getValue());
    		}
    		
    		return adeptDocument;
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}
    }
    
    
	
}
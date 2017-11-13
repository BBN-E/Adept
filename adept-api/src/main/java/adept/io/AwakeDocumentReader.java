/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import adept.common.AudioOffset; 
import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.common.Sarcasm;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Sarcasm.Judgment;

import adept.serialization.JSONBinarySerializer;
import adept.serialization.JSONStringSerializer;
import adept.serialization.SerializationType;
import adept.serialization.XMLStringSerializer;

import adept.utilities.PassageAttributes;
import adept.utilities.StanfordTokenizer;
import adept.utilities.printHltContent;



/**
 * The Class AwakeDocumentReader.
 */
public class AwakeDocumentReader {
  private static final Logger logger = LoggerFactory.getLogger(AwakeDocumentReader.class);
	
	protected static final String _XsdFilename = "AWAKE.xsd";
	
	/**
	 * Singleton instance and getter method.
	 */
	private static AwakeDocumentReader instance;

	/**
	 * Gets the single instance of AwakeDocumentReader.
	 * 
	 * @return single instance of AwakeDocumentReader
	 */
	public static AwakeDocumentReader getInstance() {
		if (instance == null)
			instance = new AwakeDocumentReader();
		return instance;
	}
	
	// For smoke test
	public static void main(String[] args) {
		String filename;
		if ( args.length > 0){
			filename = args[0];
		}
		else {
			filename = "../test-classes/adept/io/awakeTest.xml";
		}
		File inputFile = new File(filename);
		if ( !inputFile.exists()){
			System.out.println("Error missing file: " + filename);
			return;
		}
		String nameOnly = inputFile.getName();
		HltContentContainer hltcc = AwakeDocumentReader.getInstance().createAwakeDocument(filename);	
		if ( hltcc == null ) {
			System.out.println("Error: no HltContentContainer.");
			return;
		}
		adept.common.Document adeptDoc = hltcc.getDocument();
		//
		System.out.println("Document ID = " + adeptDoc.getDocId());
		System.out.println("Document name = " + adeptDoc.getName());
		System.out.println("Passage count = " + hltcc.getPassages().size());
		//
		String xmlFilename = nameOnly + ".out.xml"; 
		XMLStringSerializer xmlStringSerializer = new XMLStringSerializer();
		try {
  		String xmlTemp = xmlStringSerializer.serializeToString(hltcc);
  		System.out.println("Writing to:  " + xmlFilename);
  		Writer.getInstance().writeToFile(xmlFilename,xmlTemp);
  		printHltContent.writeFile(xmlFilename, xmlFilename + ".txt");
		} catch (Exception e) {
		  System.err.format("Exception %s caught%n", e.getClass().getName());
		  logger.error("Exception caught: ", e);
		  System.exit(1);
		}
	}	
	
	/**
	 * Creates the default document. The argument filename *must* be non-null.
	 *
	 * @param filename the filename
	 * @return the document in a {@code HltContentContainer}
	 */
	public HltContentContainer createAwakeDocument(
			String filename ) {
		System.out.println("Creating ADEPT document from file: " + filename);
		org.w3c.dom.Document w3cDoc;
		try {
			w3cDoc = Reader.getInstance().readXML(filename);
			if (w3cDoc == null) {
				System.out.println("Error: Awake file not found.");
				return null;
			}
			org.w3c.dom.Document xsdDoc = Reader.getInstance().readXML(_XsdFilename);
			if (xsdDoc == null) {
				System.out.println("Error: XSD file not found.");
			}
			else if ( ! validateXMLSchema( w3cDoc, xsdDoc) ) {
				System.out.println("Awake file failed validation.");
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		System.out.println("Loading input document as XML-formatted Awake file");
		List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();		
		adept.common.Document adeptDocument = AwakeDocumentReader.getInstance().readDocument(w3cDoc, passageAttributesList);
		if(adeptDocument == null ) {
			System.out.println("Unable to read document.");
			return null;
		}
		// First tokenize document.  Then match up first and last tokens using character offsets of passage.
		TokenStream tokenStream = StanfordTokenizer.getInstance().tokenize(adeptDocument.getValue(), adeptDocument);
//		tokenStream.setDocument(adeptDocument);								
//		adeptDocument.addTokenStream(tokenStream);
		System.out.println("Number of passages: " + passageAttributesList.size());				
		//
		int passageStartOffset = 0;
		List<Passage> passageOutList = new ArrayList<Passage>();								
		for(PassageAttributes passageAttr : passageAttributesList) {
			int passageLen = passageAttr.getValue().length();
			int begin = 0;
			int end = 0;
			boolean haveBegin = false;
			for ( int i = 0; i < tokenStream.size(); ++i ){
				Token token = tokenStream.get(i); 
				if ( !haveBegin && token.getCharOffset().getBegin() >=  passageStartOffset){
					begin = i;
					end = i;
					haveBegin = true;
				}
				if ( token.getCharOffset().getEnd() > passageStartOffset + passageLen) break;
				end = i;
			}			
			if ( haveBegin ) {
				TokenOffset tokenOffset = new TokenOffset(begin, end);
				Long passageId = passageAttr.getPassageId();
				Passage passageTemp = new Passage(passageId, tokenOffset, tokenStream);
				passageTemp.setAudioOffset(passageAttr.getAudioOffset());
				passageTemp.setSpeaker(passageAttr.getSpeaker());
				passageOutList.add(passageTemp);
				passageStartOffset += passageLen;			
				//System.out.println("Passage " + seqId + "added successfully! " + passageString + " " + startIndex + " " + (startIndex+passageLevelTokenStream.size()-1));
			}
		}
		//System.out.println("Length of passages arraylist: " + passages.size());
		HltContentContainer hltcc = new HltContentContainer();
		if (passageOutList.size() > 0) {
			hltcc.setPassages(passageOutList);
		}
		return hltcc;
	}

    public static boolean validateXMLSchema(org.w3c.dom.Document xsdPath, org.w3c.dom.Document xmlPath){
        
        try {
            SchemaFactory factory = 
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            DOMSource xsdSource = new DOMSource(xsdPath);
            Schema schema = factory.newSchema(xsdSource);
            Validator validator = schema.newValidator();
            DOMSource xmlSource = new DOMSource(xmlPath);
            validator.validate(xmlSource);
        } catch (IOException  e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        } catch ( SAXException e) {
            System.out.println("Exception: "+e.getMessage());
            return false;
        }
        return true;
    }
	
	protected int addTokenToStream( int docCharStart,
			int tokenId, 
			Token token, 
			TokenStream tokenStream  ) {
		int pasCharBegin = token.getCharOffset().getBegin();
		int pasCharEnd = token.getCharOffset().getEnd();
		int newPasCharBegin = docCharStart + pasCharBegin;
		int newPasCharEnd = newPasCharBegin + (pasCharEnd-pasCharBegin);		        		 
		Token docToken = new Token(tokenId, new CharOffset(newPasCharBegin, newPasCharEnd), token.getValue());
		docToken.setTokenType(TokenType.LEXEME);
		docToken.setLemma(token.getLemma());		        		 
		tokenStream.add(docToken);		        		 
		return newPasCharEnd;
		//System.out.println("oldbegin: " + pasCharBegin + 
		//		 			", oldend: "   + pasCharEnd +
		//		 			", newbegin: " + newPasCharBegin +		        				 			
		//		 			", newend: "	+ newPasCharEnd + ", OldValue: " + token.getValue() + ", NewValue: " + docToken.getValue());
	}

	/**
	 * reads the Awake format document into an ADEPT document.
	 *
	 * @param doc the Awake document
	 * @param passageList the output list of passages
	 * @return the ADEPT document
	 */
	public adept.common.Document readDocument(org.w3c.dom.Document doc, List<PassageAttributes> passageList) {
		Element docElement = doc.getDocumentElement();
		docElement.normalize();
		NodeList passages = docElement.getElementsByTagName("passage");
		StringBuffer sb = new StringBuffer();
		//System.out.println("passages: " + passages.toString() + " " + passages.getLength());
		for (int i = 0; i < passages.getLength(); i++) {
			Element passageElement = (Element)passages.item(i);					
			if (passageElement != null) {
				PassageAttributes pa = makePassage( passageElement, sb );
				if ( pa != null ) {
					passageList.add(pa);
				}
			}
		}
		// TODO - how to reuse existing corpus?
		Corpus corpus = makeCorpus( docElement);

		/** create adept document and return */
 		adept.common.Document adeptDocument = makeDocument( docElement, corpus);
		
		/** cumulate passage values and convert to utf-8 */
 		String value = makeDocumentValue( sb );
		adeptDocument.setValue(value);
		return adeptDocument;
	}

	protected PassageAttributes makePassage( Element passage, StringBuffer sb){
		if ( passage == null || passage.getFirstChild() == null ) return null;
		//
		String passageValue = passage.getFirstChild().getNodeValue();						
		String passageValueSurrogatesRemoved = Reader.checkSurrogates(passageValue);
		if (!passageValueSurrogatesRemoved.equals("")) {
			sb.append(passageValueSurrogatesRemoved);// + "\n");
			//
			PassageAttributes pa = new PassageAttributes();
			long passageId = Long.parseLong(passage.getAttribute("passage_id"));
			pa.setPassageId(passageId);
			String speaker = passage.getAttribute("speaker");
			pa.setSpeaker(speaker);
			pa.setValue(passageValueSurrogatesRemoved);
			Float start = getFloat( passage, "passage_start_offset");
			Float duration = getFloat( passage, "passage_duration");
			if ( start != null && duration != null ) {
				AudioOffset audioOffset = new AudioOffset( start, start+ duration); 
				pa.setAudioOffset( audioOffset );
			}
			return pa;
		} else {
			System.out.println(passageValue);
			return null;
		}
	}
	
	protected Corpus makeCorpus( Element docElement) {
		String corpus_id = docElement.getAttribute("corpus_id");
		String corpus_name = docElement.getAttribute("corpus_name");
		String corpus_type = docElement.getAttribute("corpus_type");
		String corpus_uri = null; // TODO
		return new Corpus( corpus_id,corpus_name,corpus_type, corpus_uri); 
	}
	
	protected adept.common.Document makeDocument( Element docElement, Corpus corpus) {
		String uri = docElement.getAttribute("document_uri");
		String docID = docElement.getAttribute("document_name");
		String docType = docElement.getAttribute("document_type");
		String language = docElement.getAttribute("document_language");
		//	System.out.println("Doc URI: " + uri);
		//	System.out.println("Doc Id: " + docID);
		//	System.out.println("Doc type: " + docType);
		adept.common.Document adeptDocument = new adept.common.Document(docID, corpus, docType, uri, language);
		//
		String captureDate = docElement.getAttribute("document_capture_date");
		adeptDocument.setCaptureDate(captureDate);
		String publicationDate = docElement.getAttribute("document_publication_date");
		adeptDocument.setPublicationDate(publicationDate);
		String genre = docElement.getAttribute("document_genre");
		adeptDocument.setGenre(genre);
		String headline = docElement.getAttribute("document_headline");
		adeptDocument.setHeadline(headline);
		String name = docElement.getAttribute("document_name");
		adeptDocument.setName(name);
		String splitId = docElement.getAttribute("document_split_id");
		adeptDocument.setSplitId(splitId);
		String sourceLanguage = docElement.getAttribute("source_language");
		adeptDocument.setSourceLanguage(sourceLanguage);
		String sourceUri= docElement.getAttribute("source_uri");
		adeptDocument.setSourceUri(sourceUri);
		return adeptDocument;
	}
	
	protected String makeDocumentValue( StringBuffer sb ){
		String value = sb.toString();
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
		return value;
	}
	
	Float getFloat(Element passage, String attribute)  
	{  
		try {  
			String floatAttr = passage.getAttribute(attribute);
			return Float.parseFloat(floatAttr );  
		} catch(NumberFormatException nfe) {  
			return null;  
		}  
	}

}
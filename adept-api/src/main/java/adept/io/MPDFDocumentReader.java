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

import adept.utilities.DocumentMaker;
import com.google.common.base.Optional;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import adept.common.CharOffset;
import adept.common.ConversationElement;
import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.utilities.StanfordTokenizer;


/**
 * Reads Multi-Post Discussion-Forum documents
 */
public class MPDFDocumentReader {

	/**
	 * Singleton instance and getter method.
	 */
	private static MPDFDocumentReader instance;

	/**
	 * Gets the single instance of AwakeDocumentReader.
	 *
	 * @return single instance of AwakeDocumentReader
	 */
	public static MPDFDocumentReader getInstance() {
		if (instance == null)
			instance = new MPDFDocumentReader();
		return instance;
	}

	public HltContentContainer createMPDFDocument(
	    String filename) {
		System.out.println("Creating MPDF document from file: " + filename);
		org.w3c.dom.Document w3cDoc;
		try {
			w3cDoc = Reader.getInstance().readXML(filename);
			if (w3cDoc == null) {
				System.out.println("Error: MPDF file not found.");
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	  	List<PostAttributes> postAttributes = new ArrayList<>();
	  	Document mpdfDocument = createMPDFDocument(w3cDoc,Optional.absent(),
		    postAttributes,Optional.absent(),null,Optional.of("discussion_thread"),null,
		    "English", filename, DocumentMaker.XMLReadMode.DEFAULT);
	  	return createMPDFContainer(mpdfDocument.getTokenStream(TokenizerType.STANFORD_CORENLP),
		    postAttributes, new HltContentContainer());
	}

  public Document createMPDFDocument(org.w3c.dom.Document w3cDoc, Optional<TokenStream>
      tokenStreamIn, List<PostAttributes> postAttributesList, Optional<String> docId, Corpus corpus,
									 Optional<String> docType, String uri, String language, String filename, DocumentMaker.XMLReadMode xmlReadMode) {
    Document mpdfDocument = MPDFDocumentReader.getInstance().readDocument(w3cDoc,
	postAttributesList,docId,corpus,docType,uri,language);
    if (mpdfDocument == null) {
      System.out.println("Unable to read document.");
      return null;
    }
    // First tokenize document.  Then match up first and last tokens using character offsets of passage.
    TokenStream tokenStream = tokenStreamIn.orNull();
    if (tokenStream == null) {
      tokenStream = StanfordTokenizer.getInstance().tokenize(
	  mpdfDocument.getValue(), mpdfDocument);
    }
    if (xmlReadMode != DocumentMaker.XMLReadMode.DEFAULT) {
        String cleanedRawDocumentValue;
        try {
            String rawDocumentValue = Reader.checkSurrogates(Reader.readRawFile(filename));
            if (xmlReadMode == DocumentMaker.XMLReadMode.RAW_XML_TAC) {
                rawDocumentValue = rawDocumentValue.substring(Reader.findXMLTag(rawDocumentValue, "doc"));
            }
            cleanedRawDocumentValue = Reader.cleanRawText(rawDocumentValue, "<(post|headline).*?>.*?<\\/(post|headline)>", new String[]{"<\\/?.*?>"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tokenStream.reAlignToText(cleanedRawDocumentValue);
        mpdfDocument.setValue(cleanedRawDocumentValue);
    }
    mpdfDocument.addTokenStream(tokenStream);
    return mpdfDocument;
  }

  public HltContentContainer createMPDFContainer(TokenStream
      tokenStream, List<PostAttributes> postAttributesList, HltContentContainer hltcc) {
    System.out.println("Number of posts: " + postAttributesList.size());

    List<ConversationElement> conversationElements = new LinkedList<>();
	List<Passage> passages = new LinkedList<>();

	int previousPassageAttributesEnd = 0;
	for (PostAttributes postAttr : postAttributesList) {
		String postValue = postAttr.value().trim();
		TokenOffset tokenOffset = tokenStream.findPhrase(postValue, new TokenOffset(previousPassageAttributesEnd, tokenStream.size() - 1));
		if (tokenOffset == null) {
			throw new NoSuchElementException(postValue);
		}
		ConversationElement conversationElement = new ConversationElement(tokenOffset, tokenStream);
		conversationElement.setAuthoredTime(postAttr.datetime());
		conversationElement.setAuthorId(postAttr.author());
		conversationElements.add(conversationElement);
		passages.add(new Passage(passages.size(), tokenOffset, tokenStream));
		previousPassageAttributesEnd = tokenOffset.getEnd() + 1;
	}

	hltcc.setConversationElements(conversationElements);
	hltcc.setPassages(passages);
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

	private static boolean isNodeOutsidePost(Node node) {
		if (node == null) {
			throw new NullPointerException();
		}
		for (Node n = node; n != null; n = n.getParentNode()) {
			if (n.getNodeName().equals("post")) {
				return false;
			}
		}
		return true;
	}

	/**
	 * reads the Awake format document into an ADEPT document.
	 *
	 * @param doc the Awake document
	 * @param postList the output list of passages
	 * @return the ADEPT document
	 */
	protected Document readDocument(org.w3c.dom.Document doc, List<PostAttributes> postList,
	    Optional<String> docId, Corpus corpus, Optional<String> docType, String uri,
	    String language) {

		// thrown an exception if we encounter unknown tag name(s)
		NodeList nodeList = doc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String tagName = node.getNodeName();
			// only consider tags outside of "post" elements, since tags within the post are
			// actually part of the user-posted text (we filter these out later anyway)
			if (isNodeOutsidePost(node) && !tagName.matches("doc|headline")) {
				throw new RuntimeException(new IOException("input document contains unknown tag '" + tagName + "'"));
			}
		}

		Element docElement = doc.getDocumentElement();
		docElement.normalize();
	  	StringBuffer sb = new StringBuffer();

		Node headlineNode = docElement.getElementsByTagName("headline").item(0);
		if (headlineNode != null) {
			sb.append(Reader.checkSurrogates(headlineNode.getTextContent()));
		}

		NodeList posts= docElement.getElementsByTagName("post");
		//System.out.println("passages: " + passages.toString() + " " + passages.getLength());
		for (int i = 0; i < posts.getLength(); i++) {
			Element postElement = (Element)posts.item(i);
			if (postElement != null) {
				PostAttributes pa = createPost(postElement, sb);
				if ( pa.value() != null && !pa.value().trim().equals("")) {
					postList.add(pa);
				}
			}
		}
		/** create adept document and return */
 		Document mpdfDocument = makeDocument( docElement, docId, corpus, docType, uri,
		    language);

		mpdfDocument.setValue(makeDocumentValue(sb));
		return mpdfDocument;
	}

	protected PostAttributes createPost(Element post, StringBuffer sb){
		if ( post == null ) return null;
		return PostAttributes.create(post.getAttribute("author"),
		    post.getAttribute("datetime"), post.getAttribute("id"),
		    getPostContent(post, sb));
	}

  	private String getPostContent(Element node, StringBuffer sb){
	  	String postText = Reader.checkSurrogates(node.getTextContent());
	  	sb.append(postText);
	  	return postText.trim();
	}

	protected Document makeDocument( Element docElement, Optional<String> docId, Corpus
	    corpus, Optional<String> docType, String uri, String language) {
		String effectiveDocId = docId.or(docElement.getAttribute("id"));
		Document mpdfDocument = new Document(effectiveDocId, corpus, docType.or("discussion_thread"),
		    uri,language);
		Node headlineNode = docElement.getElementsByTagName("headline").item(0);
		if(headlineNode != null) {
		  mpdfDocument.setHeadline(headlineNode.getTextContent());
	  	}
		return mpdfDocument;
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

  	public static final class PostAttributes{
	  final String author;
	  final String datetime;
	  final String id;
	  final String value;
	  PostAttributes(String author, String datetime, String id, String value){
		this.author = author;
	    	this.datetime = datetime;
	    	this.id = id;
	    	this.value = value.replace("\uFEFF", "");
	  }
	  static PostAttributes create(String author, String datetime, String id, String value){
		return new PostAttributes(author,datetime,id,value);
	  }
	  public String author(){
	    return author;
	  }
	  public String datetime(){
	    return datetime;
	  }
	  public String id(){
	    return id;
	  }
	  public String value(){
	    return value;
	  }

  	}

}

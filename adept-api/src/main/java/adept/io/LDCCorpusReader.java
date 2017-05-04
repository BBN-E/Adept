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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import adept.common.Pair;
import adept.common.ConversationElement;
import adept.common.ConversationElementAttributesTypeFactory;
import adept.common.ConversationElementEntityRelationTypeFactory;
import adept.common.ConversationElementRelationTypeFactory;
import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Corpus;
import adept.common.HltContentContainer;
import adept.common.ConversationElementTag;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.utilities.PassageAttributes;
import adept.utilities.StanfordTokenizer;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class LDCCorpusReader.
 */
public class LDCCorpusReader {
    /**
     * Singleton instance and getter method.
     */
    private static LDCCorpusReader instance;

    /**
     * Gets the single instance of LDCCorpusReader.
     *
     * @return single instance of LDCCorpusReader
     */
    public static LDCCorpusReader getInstance() {
        if (instance == null)
            instance = new LDCCorpusReader();
        return instance;
    }

    /**
     * reads text document for specific tags.
     *
     * @param text                  the text of the document
     * @param passageAttributesList the passage attribute list
     * @param corpus                the corpus
     * @param uri                   the uri
     * @param language              the language
     * @return the adept.common. document
     */
    public HltContentContainer readPosts(String text, List<PassageAttributes> passageAttributesList,
                                         Corpus corpus,
                                         String uri,
                                         String language) throws InvalidPropertiesFormatException, IOException {

        String docType = "sgm";
        String docID = uri;
        if (docID == null)
            docID = "UNKNOWN";

        List<ConversationElementTag> tags = new ArrayList<ConversationElementTag>();
        //contains name, attributes, start
        //Stack<Pair<Pair<String, Map<String, String>>, Integer>> tagStack = new Stack<Pair<Pair<String, Map<String, String>>, Integer>>();
        Stack<ConversationElement> postStack = new Stack<ConversationElement>();
        Stack<ConversationElement> quoteStack = new Stack<ConversationElement>();
        List<ConversationElement> postList = new ArrayList<ConversationElement>();

        Pattern tagPattern = Pattern.compile("<.*?>");
        Pattern tagNamePattern = Pattern.compile("\\w+");
        Pattern tagAttrPattern = Pattern.compile("\\s\\w*?=\".*?\"");

        Matcher tagMatcher = tagPattern.matcher(text);

        int lastTagEnd = 0;
        int currTagBegin = 0;

        // TODO null docID
        adept.common.Document adeptDocument = new adept.common.Document(docID, corpus, docType, uri, language);
//		adeptDocument.setValue(value);
        adeptDocument.setValue(text);
//        tokenStream.setDocument(adeptDocument);

        TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
        TranscriptType transcriptType = TranscriptType.SOURCE;
        ChannelName channelName = ChannelName.NONE;
        ContentType contentType = ContentType.TEXT;
        TokenStream tokenStream = new TokenStream(tokenizerType, transcriptType, language, channelName,
                contentType, adeptDocument);

        HashMap<ConversationElement, Map<String, Token>> attrsMap = new HashMap<ConversationElement, Map<String, Token>>();

        //find tags
        while (tagMatcher.find()) {
            int tagStart = tagMatcher.start();
            int tagEnd = tagMatcher.end();
            String tag = tagMatcher.group();

            int tokenBegin = tokenStream.size();

            currTagBegin = tagStart;
            String contentString = text.substring(lastTagEnd, currTagBegin);
            TokenStream contentTokenStream = StanfordTokenizer.getInstance().tokenize(contentString, adeptDocument);
            for (Token t : contentTokenStream) {
                CharOffset tOffset = t.getCharOffset();

                Token newToken = new Token(tokenStream.size(), new CharOffset(lastTagEnd + tOffset.getBegin(), lastTagEnd + tOffset.getEnd()), t.getValue());
                tokenStream.add(newToken);
            }
            lastTagEnd = tagEnd;

            if (tag.contains("<?xml")) // messy
                continue;

            Matcher tagNameMatcher = tagNamePattern.matcher(tag);
            Matcher tagAttrMatcher = tagAttrPattern.matcher(tag);

            // TODO - remove while()
            //get the tag name
            String tagName = "";
            while (tagNameMatcher.find()) {
                tagName = tagNameMatcher.group().trim().toLowerCase();

                break;
            }

            //get the tag attributes
            Map<String, Token> attributes = new HashMap<String, Token>();
            while (tagAttrMatcher.find()) {
                String attr = tagAttrMatcher.group().trim();
                int split = attr.indexOf("=");
                int attrBegin = tagAttrMatcher.start();
                int attrEnd = tagAttrMatcher.end();
                Token token = new Token(tokenStream.size(), new CharOffset(tagStart + split + attrBegin + 3, tagStart + attrEnd - 1), attr.substring(split + 2, attr.length() - 1));
                token.setTokenType(adept.common.TokenType.TAG);
                tokenStream.add(token);

                attributes.put(attr.substring(0, split).toLowerCase(), token);
            }


            boolean isEnd = tag.contains("</"); //closes another
            boolean isUnit = tag.contains("/>"); //is its own

            boolean isPost = (tag.contains("<post") || tag.contains("post>"));
            boolean isQuote = (tag.contains("<quote") || tag.contains("quote>"));

            // TODO - tags variable is unused.
            ConversationElementTag currTag = new ConversationElementTag(tagName, attributes, tagStart, tagEnd);
            tags.add(currTag);

            if (isEnd) {

                if (isQuote) {
                    ConversationElement openQuote = quoteStack.pop();
                    ConversationElement newQuote = new ConversationElement(new TokenOffset(openQuote.getMessageChunk().getTokenOffset().getBegin(), tokenStream.size() - 1), tokenStream);
                    newQuote.getFreeTextAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID"),
                            openQuote.getFreeTextAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID")));
                    newQuote.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"),
                            openQuote.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")));
                    newQuote.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG"),
                            openQuote.getConversationElementTagAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG")));
                    newQuote.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG"),
                            openQuote.getConversationElementTagAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG")));

                    if (!quoteStack.isEmpty()) {
                        ConversationElement superQuote = quoteStack.pop();
                        if (superQuote.getConversationElementRelations().containsKey(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")))
                            superQuote.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")).add(newQuote);
                        else {
                            List<ConversationElement> newQuotes = new ArrayList<ConversationElement>();
                            newQuotes.add(newQuote);
                            superQuote.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"), newQuotes);
                        }
                        quoteStack.push(superQuote);
                    } else {
                        ConversationElement superPost = postStack.pop();
                        if (superPost.getConversationElementRelations().containsKey(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")))
                            superPost.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")).add(newQuote);
                        else {
                            List<ConversationElement> newQuotes = new ArrayList<ConversationElement>();
                            newQuotes.add(newQuote);
                            superPost.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"), newQuotes);
                        }
                        postStack.push(superPost);
                    }
                } else if (isPost) {
                    ConversationElement openPost = postStack.pop();
                    Map<String, Token> openPostAttrs = attrsMap.get(openPost);
                    ConversationElement newPost = new ConversationElement(new TokenOffset(openPost.getMessageChunk().getTokenOffset().getBegin()+openPostAttrs.size(), tokenStream.size() - 1), tokenStream);
                    if(openPostAttrs.containsKey("author")) {
                        newPost.setAuthorId(openPostAttrs.get("author").getValue());
                    }
                    if(openPostAttrs.containsKey("datetime")) {
                        newPost.setAuthoredTime(openPostAttrs.get("datetime").getValue());
                    }
                    newPost.getFreeTextAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID"),
                            openPost.getFreeTextAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID")));
                    newPost.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG"),
                            openPost.getConversationElementTagAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG")));
                    newPost.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG"),
                            openPost.getConversationElementTagAttributes().get(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG")));
                    newPost.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"),
                            openPost.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")));
                    postStack.push(newPost);
                    postList.add(newPost);
                }
//                Pair<Pair<String, Map<String,String>>,Integer> partner = tagStack.pop();
//                tags.add(new Tag(partner.getL().getL(), partner.getL().getR(), partner.getR(), tagEnd));
            }
/*            else if(isUnit)
            {
	        tags.add(new Tag(tagName, attributes, tagStart, tagEnd));
            }*/
            else if (!isUnit) {
                if (isQuote) {
                    String sequenceId;
                    if (!quoteStack.isEmpty()) {
                        ConversationElement superQuote = quoteStack.peek();
                        try {
                            sequenceId = Integer.toString(superQuote.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")).size());
                        } catch (NullPointerException e) {
                            sequenceId = "1";
                        }
                    } else {
                        ConversationElement superPost = postStack.peek();
                        try {
                            sequenceId = Integer.toString(superPost.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES")).size());
                        } catch (NullPointerException e) {
                            sequenceId = "1";
                        }

                    }
                    if (tokenBegin > tokenStream.size() - 1) {
                        tokenBegin = tokenStream.size() - 1;
                    }
                    ConversationElement newQuote = new ConversationElement(new TokenOffset(tokenBegin, tokenStream.size() - 1), tokenStream);
                    newQuote.getFreeTextAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID"),
                            sequenceId);
                    newQuote.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG"), currTag);
                    newQuote.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG"), currTag);
                    quoteStack.push(newQuote);
                } else if (isPost) {
                    String sequenceId = Long.toString(postList.size());

                    ConversationElement newPost = new ConversationElement(new TokenOffset(tokenBegin, tokenStream.size() - 1), tokenStream);
                    newPost.getFreeTextAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("SEQUENCE ID"),
                            sequenceId);
                    newPost.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("OPEN TAG"), currTag);
                    newPost.getConversationElementTagAttributes().put(ConversationElementAttributesTypeFactory.getInstance().getType("CLOSE TAG"), currTag);
                    postStack.push(newPost);
                    attrsMap.put(newPost,attributes);
                }
//                Pair<String, Map<String,String>> nameAndAttrs = new Pair<String, Map<String,String>>(tagName, attributes);
//                Pair<Pair<String, Map<String,String>>,Integer> partial = new Pair<Pair<String, Map<String,String>>,Integer>(nameAndAttrs, tagStart);
//                tagStack.push(partial);
            }

        }

//	System.out.println("Post Count: " + postStack.size());

        //		adeptDocument.setHeadline(headline);

        List<ConversationElement> newPostList = new ArrayList<>();
        for (ConversationElement p : postList) {
            ConversationElement newPost = assignPostToQuotes(p);
            newPostList.add(newPost);
        }

        adeptDocument.addTokenStream(tokenStream);
        HltContentContainer hltcc = new HltContentContainer();
        hltcc.setConversationElements(newPostList);

//		return new Pair<List<Tag>,adept.common.Document>(tags,adeptDocument);

        return hltcc;
    }

    private ConversationElement assignPostToQuotes(ConversationElement post) throws InvalidPropertiesFormatException, IOException {
        List<ConversationElement> quotes = post.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"));
        if (quotes == null || quotes.size() == 0) {
            return post;
        } else {
            List<ConversationElement> newQuotes = new ArrayList<ConversationElement>();
            for (ConversationElement quote : quotes) {
                ConversationElement newQuote = assignPostToSubQuotes(quote, post);
                newQuotes.add(newQuote);
            }
            post.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"), newQuotes);
            return post;
        }
    }

    private ConversationElement assignPostToSubQuotes(ConversationElement quote, ConversationElement post) throws InvalidPropertiesFormatException, IOException {
        List<ConversationElement> subQuotes = quote.getConversationElementRelations().get(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"));
        if (subQuotes == null || subQuotes.size() == 0) {
            List<ConversationElement> posts = new ArrayList<ConversationElement>();
            posts.add(post);
            quote.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("PARENT POST"), posts);
            return quote;
        } else {
            List<ConversationElement> posts = new ArrayList<ConversationElement>();
            posts.add(post);
            List<ConversationElement> newQuotes = new ArrayList<ConversationElement>();
            for (ConversationElement subQuote : subQuotes) {
                ConversationElement newQuote = assignPostToSubQuotes(subQuote, post);
                newQuotes.add(newQuote);
            }
            quote.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("QUOTES"), newQuotes);
            quote.getConversationElementRelations().put(ConversationElementRelationTypeFactory.getInstance().getType("PARENT POST"), posts);
            return quote;
        }
    }


    /**
     * reads the DOM format document for specific tags.
     *
     * @param doc                   the  w3c Document
     * @param passageAttributesList the passage attributes list
     * @param corpus                the corpus
     * @param uri                   the uri
     * @param language              the language
     * @return the adept.common. document
     */
    public adept.common.Document readCorpus(org.w3c.dom.Document doc,
                                            List<PassageAttributes> passageAttributesList,
                                            Corpus corpus,
                                            String uri,
                                            String language) {
        adept.common.Document adeptDocument;    // distinguish from w3c Document type
        String docID = null;
        String docType = null;
        String headline;
        StringBuffer documentTextBuffer = new StringBuffer();
        doc.getDocumentElement().normalize();

        /** get text */
        // TODO - there should be a clearer way of distinguishing XML formats.
        Element textElement = (Element) doc.getElementsByTagName("TEXT").item(0);
        if (textElement == null) {
            // HUB4 format for CoNLL read itemid from element such as this:
            // <newsitem itemid="23489" id="root" date="1996-08-30" xml:lang="en">
            // TODO - should also read "headline" and "dateline" elements?
            textElement = getTextElement(doc);
            NodeList passagesNodeList = getPassagesNodeList(textElement);
            documentTextBuffer = getDocumentTextBuffer(passagesNodeList, documentTextBuffer, passageAttributesList);
            /** get adept Document ID and type */
            docID = getDocID(doc);
            docType = getDocType(doc);

        } else {
            System.out.println("textElement!=NULL");
            NodeList passages = textElement.getElementsByTagName("P");
            if (passages != null && passages.getLength() > 0) {
                //System.out.println("passages: " + passages.toString() + " " + passages.getLength());
                for (int i = 0; i < passages.getLength(); i++) {
                    Element passage = (Element) passages.item(i);
                    if (passage != null) {
                        String passageText = passage.getFirstChild().getNodeValue();
                        long passageId = i;
                        String sarcasmValue = "";
                        if (!passage.getAttribute("pid").equals("")) {
                            passageId = Long.parseLong(passage.getAttribute("pid"));
                        }
                        if (!passage.getAttribute("sarcasm").equals("")) {
                            sarcasmValue = passage.getAttribute("sarcasm");
                        }
                        String passageValueTrimmed = passageText.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                        String passageValueSurrogatesRemoved = Reader.checkSurrogates(passageValueTrimmed);
                        if (!passageValueSurrogatesRemoved.equals("")) {
                            documentTextBuffer.append(passageValueSurrogatesRemoved);
                            documentTextBuffer.append("\n");
                            PassageAttributes pa = new PassageAttributes();
                            pa.setPassageId(passageId);
                            pa.setSarcasmValue(sarcasmValue);
                            pa.setValue(passageValueSurrogatesRemoved);
                            passageAttributesList.add(pa);
                        } else {
                            System.out.println(passageText);
                        }

                    }
                }
                // TODO - for CoNLL read itemid from element such as this:
                // <newsitem itemid="23489" id="root" date="1996-08-30" xml:lang="en">

                // TODO redundant code
                // TODO get more than one POST
            } else if (doc.getElementsByTagName("BODY").item(0) != null) {
                Element bodyElement = (Element) doc.getElementsByTagName("BODY").item(0);
                textElement = (Element) bodyElement.getElementsByTagName("TEXT").item(0);
                Element postElement = (Element) textElement.getElementsByTagName("POST").item(0);
                Element textOrPost = (postElement == null) ? textElement : postElement;
                NodeList nodes = textOrPost.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node child = nodes.item(i);
                    if (child.getNodeType() == Node.TEXT_NODE)
                        documentTextBuffer.append(child.getTextContent());
                }
                getPassages(passageAttributesList, documentTextBuffer.toString().trim());
            } else {
                textElement = (Element) doc.getElementsByTagName("TEXT").item(0);
                Element postElement = (Element) textElement.getElementsByTagName("POST").item(0);
                Element textOrPost = (postElement == null) ? textElement : postElement;
                NodeList nodes = textOrPost.getChildNodes();
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node child = nodes.item(i);
                    if (child.getNodeType() == Node.TEXT_NODE)
                        documentTextBuffer.append(child.getTextContent());
                }
                getPassages(passageAttributesList, documentTextBuffer.toString().trim());
            }

            /** get adept Document ID and type */
            Element DOCElement = (Element) doc.getElementsByTagName("DOC").item(0);
            docID = DOCElement.getAttribute("id");
            docType = DOCElement.getAttribute("type");
            if (docID.equals("")) {
                try {
                    System.out.println("DOCID was empty on the first attempt");
                    /** Get Adept Document ID */
                    NodeList docElements = doc.getElementsByTagName("DOCID");
                    if (docElements != null && docElements.getLength() != 0) {
                        docID = docElements.item(0).getFirstChild().getNodeValue();
                    }
                    if (docElements == null || docElements.getLength() == 0 || docID == null ||
                            (docID != null && docID.equals(""))) {
                        System.out.println("DOCID was empty in the second attempt (DOCID)");
                        docID = doc.getElementsByTagName("DOCNO").item(0).getFirstChild()
                                .getNodeValue();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // Unrecoverable error, so made this an unchecked exception
                    throw new RuntimeException("Unable to set DOC ID. Adept documents are required to have a non-empty document ID.");
                }

            }
            if (docType.equals("")) {
                /** get adept Document Type */
                try {
                    docType = doc.getElementsByTagName("DOCTYPE").item(0)
                            .getFirstChild().getNodeValue();
                } catch (NullPointerException e) {
                    docType = "UNKNOWN";
                }
            }

        }

        String documentText = documentTextBuffer.toString().trim();

        /** convert to utf-8 */
        Charset charset = Charset.forName("UTF-8");
        try {
            ByteBuffer bbuf = charset.newEncoder().encode(CharBuffer.wrap(documentText));
            CharBuffer cbuf = charset.newDecoder().decode(bbuf);
            documentText = cbuf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /** get headline */
        headline = "";
        Element headlineElement = (Element) doc.getElementsByTagName("HEADLINE").item(0);
        if (headlineElement != null && headlineElement.getFirstChild() != null) {
            headline = headlineElement.getFirstChild().getNodeValue();
        } else {
            // HUB4
            headlineElement = (Element) doc.getElementsByTagName("headline").item(0);
            if (headlineElement != null && headlineElement.getFirstChild() != null) {
                headline = headlineElement.getFirstChild().getNodeValue();
            }
        }

        /** create adept document and return */
        // TODO - more languages

        System.out.println("Doc URI: " + uri);
        System.out.println("Doc Id: " + docID);
        System.out.println("Doc type: " + docType);
        adeptDocument = new adept.common.Document(docID, corpus, docType, uri, language);
        adeptDocument.setValue(documentText);
        adeptDocument.setHeadline(headline);    // TODO should this be trimmed?

        return adeptDocument;
    }

    private NodeList getPassagesNodeList(Element textElement) {
        NodeList nodelist;
        nodelist = textElement.getElementsByTagName("p");
        if (nodelist.getLength() == 0)
            nodelist = textElement.getElementsByTagName("post");
        ;
        return nodelist;
    }

    private Element getTextElement(Document doc) {
        Element textElement = (Element) doc.getElementsByTagName("text").item(0);
        if (textElement == null)
            textElement = (Element) doc.getElementsByTagName("doc").item(0);
        return textElement;
    }

    private StringBuffer getDocumentTextBuffer(NodeList passagesNodeList, StringBuffer documentTextBuffer, List<PassageAttributes> passageAttributesList) {

//        System.out.println("passages: " + passagesNodeList.toString() + " " + passagesNodeList.getLength());
        for (int i = 0; i < passagesNodeList.getLength(); i++) {
            Element passage = (Element) passagesNodeList.item(i);
            if (passage != null) {
                String passageValue = passage.getTextContent();
                String passageValueTrimmed = passageValue.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
                String passageValueSurrogatesRemoved = Reader.checkSurrogates(passageValueTrimmed);
                if (!passageValueSurrogatesRemoved.equals("")) {
                    documentTextBuffer.append(passageValueSurrogatesRemoved);
                    documentTextBuffer.append("\n");

                    PassageAttributes pa = new PassageAttributes();
                    pa.setPassageId(Long.parseLong(passage.getAttribute("id").replaceAll("[^0-9.]", "")));
                    pa.setValue(passageValueSurrogatesRemoved);
                    pa.setSpeaker(passage.getAttribute("author"));
                    pa.setPostPassageOffset(0);
                    passageAttributesList.add(pa);
                }
            }
        }
        return documentTextBuffer;
    }

    private String getDocID(Document doc) {
        Element DOCElement = (Element) doc.getElementsByTagName("newsitem").item(0);
        if (DOCElement != null) {
            return DOCElement.getAttribute("itemid");
        } else {
            DOCElement = (Element) doc.getElementsByTagName("doc").item(0);
            if (DOCElement != null) {
                return DOCElement.getAttribute("id");
            }
        }
        return null;
    }

    private String getDocType(Document doc) {
        Element DOCElement = (Element) doc.getElementsByTagName("newsitem").item(0);
        if (DOCElement != null) {
            return "HUB4";//DOCElement.getAttribute("type");
        } else {
            return "MPDF";
        }
    }

    /**
     * Get a {@link List} of passages given some input text {@code text}.
     *
     * @param passageAttributesList The empty list to which to add the passages.
     * @param text                  The input text.
     */
    public void getPassages(List<PassageAttributes> passageAttributesList, String text) {
        BufferedReader bufReader = new BufferedReader(new StringReader(text));
        int postPassageOffset = 0; // used to count up the increasing postPassageOffset for each passage
        StringBuffer value = new StringBuffer(); // used to build up the value string for each passage
        String line; // used to hold each line as we read it in from the buffer
        boolean wasPreviousLineBlank = false; // used to store whether the line before the one that we are currently reading was blank
        try {
            while ((line = bufReader.readLine()) != null) {
                boolean isCurrentLineBlank = line.trim().isEmpty();
                // The defined condition for writing what we have so far (not including the current line) to a new
                // passage is a transition from a blank line to a non-blank line.
                if (wasPreviousLineBlank && !isCurrentLineBlank) {
                    addPassageToList(passageAttributesList, value.toString().trim(), postPassageOffset);
                    postPassageOffset = 0;
                    value.setLength(0);
                }
                if (isCurrentLineBlank) {
                    postPassageOffset += (1 + line.length()); // The blank line could still contain something like a space
                } else {
                    if (value.length() != 0) {
                        value.append(" "); // Make sure to add spaces for line breaks within the same passage
                    }
                    value.append(line);
                }
                wasPreviousLineBlank = isCurrentLineBlank; // Get ready for the next loop iteration
            }
            // We have reached the end of the buffer, but still need to add the final passage
            // that we have been building up to this point.
            if (text.endsWith("\n") || text.endsWith("\r")) {
                // If the last line of the file is a newline or carriage return
                // BufferedReader.readLine() will return null just before
                // giving the last blank line. We correct for this simply by adding 1
                // to the offset.
                postPassageOffset++;
            }
            addPassageToList(passageAttributesList, value.toString().trim(), postPassageOffset);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Consider an IOException here to be fatal
        }
    }

    /* Utility method used by getPassages([...]) */
    private void addPassageToList(List<PassageAttributes> passageAttributesList, String valueString, int postPassageOffset) {
        PassageAttributes passageAttributes = new PassageAttributes();
        passageAttributes.setValue(valueString);
        passageAttributes.setPostPassageOffset(postPassageOffset);
        passageAttributes.setPassageId(passageAttributesList.size());
        passageAttributesList.add(passageAttributes);
    }

}

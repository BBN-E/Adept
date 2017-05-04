package adept.utilities;

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

import static com.google.common.base.Preconditions.checkArgument;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Corpus;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.common.Sarcasm;
import adept.common.Sarcasm.Judgment;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.io.AMRDocumentReader;
import adept.io.LDCCorpusReader;
import adept.io.MPDFDocumentReader;
import adept.io.Reader;
import com.google.common.base.Optional;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentMaker implements ITokenizer {

  private static DocumentMaker instance;
  private static final Logger logger = LoggerFactory.getLogger(DocumentMaker.class);

  private DocumentMaker() {
    System.out.println("Instantiating singleton instance for document creator");
  }

  public static DocumentMaker getInstance() {
    if (instance == null) instance = new DocumentMaker();
    return instance;
  }

  public enum XMLReadMode {
    /**
     * Build the document by parsing out the relevant elements using an XML DOM parser, disregarding the raw XML file
     * as a whole. The character offsets will refer to a contrived document value that is built from concatenated the
     * XML elements.
     */
    DEFAULT,
    /**
     * Similar to DEFAULT, but read the document value by explicitly locating the relevant text in the raw XML
     * file and replacing everything else with whitespace. The character offsets will correspond with this "raw" value.
     */
    RAW_XML,
    /**
     * Like RAW_XML, but read the raw file as if it began at the opening &lt;doc&gt; or &lt;DOC&gt; tag, as per TAC offset guidelines.
     */
    RAW_XML_TAC
  }

  /**
   * Creates the default document. The argument filename *must* be non-null.
   *
   * @param filename the filename
   * @param hltContentContainer the hltContentContainer
   * @return the document
   */
  public Document createDocument(String filename, HltContentContainer hltContentContainer) {
    return createDocument(
        "001", null, "Text", filename, "English", filename, hltContentContainer);
  }

  public Document createDocument(
      String docId,
      Corpus corpus,
      String docType,
      String uri,
      String language,
      String filename,
      HltContentContainer hltContentContainer) {
    return createDocument(
        docId,
        corpus,
        docType,
        uri,
        language,
        filename,
        hltContentContainer,
        TokenizerType.STANFORD_CORENLP);
  }

  public Document createDocument(
          String docId,
          Corpus corpus,
          String docType,
          String uri,
          String language,
          String filename,
          HltContentContainer hltContentContainer,
          TokenizerType tokenizerType) {
    return createDocument(
            docId,
            corpus,
            docType,
            uri,
            language,
            filename,
            hltContentContainer,
            tokenizerType,
            XMLReadMode.DEFAULT);
  }

  /**
   * Creates the default document. The argument filename *must* be non-null.
   *
   * @param docId the doc id
   * @param corpus the corpus
   * @param docType the doc type
   * @param uri the uri
   * @param language the language
   * @param filename the filename
   * @param hltContentContainer the hltContentContainer
   * @param tokenizerType the tokenizer type
   * @param xmlReadMode The {@link XMLReadMode} to use
   * @return the document
   */
  public Document createDocument(
      String docId,
      Corpus corpus,
      String docType,
      String uri,
      String language,
      String filename,
      HltContentContainer hltContentContainer,
      TokenizerType tokenizerType,
      XMLReadMode xmlReadMode) {
    checkArgument(filename != null);
    filename = filename.replaceAll("^file:", "");
    checkArgument(filename.trim().length() > 0);
    boolean tokenize = (tokenizerType != null);
    Document document = null;
    System.out.println("Creating ADEPT document from file: " + filename);

    org.w3c.dom.Document w3cDoc;
    try {
      w3cDoc = Reader.getInstance().readXML(filename);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
	// is XML (and has a 'doc' or 'DOC' tag)?
	if (w3cDoc != null && (w3cDoc.getElementsByTagName("DOC").item(0) != null
            || w3cDoc.getElementsByTagName("doc").item(0) != null)) {
        System.out.println("DOM object obtained successfully");
        // is MPDF?
        if (w3cDoc.getElementsByTagName("post") != null
                && w3cDoc.getElementsByTagName("post").getLength() != 0) {
            System.out.println("Loading input document as MPDF file");
            document = createMPDFDocument(
                    docId,
                    corpus,
                    docType,
                    uri,
                    language,
                    filename,
                    hltContentContainer,
                    true,
                    tokenizerType,
                    w3cDoc,
                    xmlReadMode);
        // fall back to generic SGM
        } else {
            System.out.println("Loading input document as XML-formatted SGM file");
            document = createSGMDocument(
                    docId,
                    corpus,
                    docType,
                    uri,
                    language,
                    filename,
                    hltContentContainer,
                    tokenize,
                    tokenizerType,
                    w3cDoc,
                    xmlReadMode);
        }
    } else if ("amr".equals(docType.toLowerCase())) {
	    System.out.println("Loading input document as AMR document");
       AMRDocumentReader.getInstance().loadAMRContentContainer(filename, hltContentContainer);
    // fall back to raw text
    } else {
        System.out.println("Loading input document as raw text file");
        List<String> textLines = new ArrayList<String>();
        String text = null;
      try {
        text = Reader.getInstance().readFileIntoLines(filename, textLines);
      } catch (Exception e) {
        System.out.format("Error %s reading file '%s': ", e.getClass().getName(), filename);
        logger.error("Exception caught", e);
      }
      document =
          createDocumentFromText(
              docId,
              corpus,
              docType,
              uri,
              language,
              hltContentContainer,
              tokenize,
              tokenizerType,
              text,
              textLines);
    }
    return document;
  }

  private Document createMPDFDocument(
      String docId,
      Corpus corpus,
      String docType,
      String uri,
      String language,
      String filename,
      HltContentContainer hltContentContainer,
      Boolean tokenize,
      TokenizerType tokenizerType,
      org.w3c.dom.Document w3cDoc,
      XMLReadMode xmlReadMode) {

    Document document = null;

    List<MPDFDocumentReader.PostAttributes> postAttributesList =
        new ArrayList<MPDFDocumentReader.PostAttributes>();

    document =
        MPDFDocumentReader.getInstance()
            .createMPDFDocument(
                w3cDoc,
                Optional.absent(),
                postAttributesList,
                Optional.of(docId),
                corpus,
                Optional.of(docType),
                uri,
                language,
                filename,
                xmlReadMode);

    if (document != null && document.getTokenStream(tokenizerType) == null && tokenize) {
      TranscriptType transcriptType = TranscriptType.SOURCE;
      ChannelName channelName = ChannelName.NONE;
      ContentType contentType = ContentType.TEXT;
      TokenStream tokenStream =
          new TokenStream(
              tokenizerType,
              transcriptType,
              document != null ? document.getLanguage() : null,
              channelName,
              contentType,
              document != null ? document : null);
      System.out.println("Number of passages: " + postAttributesList.size());
      int docCharStart = 0;
      int docTokenIndex = 0;
      for (MPDFDocumentReader.PostAttributes post : postAttributesList) {
        String passageValue = post.value();
        TokenStream passageLevelTokenStream = tokenize(passageValue, document);
        int tokenId = docTokenIndex;
        int pCharEnd = 0;
        for (Token token : passageLevelTokenStream) {
          int pasCharBegin = token.getCharOffset().getBegin();
          int pasCharEnd = token.getCharOffset().getEnd();
          // Stanford tokenizer by default sets end to one past the token end.  Make UIUC do same.
          if (tokenizerType == TokenizerType.UIUC && pasCharEnd + 1 < passageValue.length()) {
            ++pasCharEnd;
          }
          int newPasCharBegin = docCharStart + pasCharBegin;
          int newPasCharEnd = newPasCharBegin + (pasCharEnd - pasCharBegin);
          Token docToken =
              new Token(
                  tokenId++, new CharOffset(newPasCharBegin, newPasCharEnd), token.getValue());
          docToken.setTokenType(TokenType.LEXEME);
          docToken.setLemma(token.getLemma());
          tokenStream.add(docToken);
          pCharEnd = newPasCharEnd;
        }
        docCharStart = pCharEnd + 1;
        docTokenIndex = tokenId;
      }
      document.addTokenStream(tokenStream);
    }
    MPDFDocumentReader.getInstance()
        .createMPDFContainer(
            document.getTokenStream(tokenize ? tokenizerType : TokenizerType.STANFORD_CORENLP),
            postAttributesList,
            hltContentContainer);
    return document;
  }

  private Document createSGMDocument(
      String docId,
      Corpus corpus,
      String docType,
      String uri,
      String language,
      String filename,
      HltContentContainer hltContentContainer,
      Boolean tokenize,
      TokenizerType tokenizerType,
      org.w3c.dom.Document w3cDoc,
      XMLReadMode xmlReadMode) {

    Document document = null;

    List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();

    document =
        LDCCorpusReader.getInstance()
            .readCorpus(w3cDoc, passageAttributesList, corpus, uri, language);

    if (document != null && tokenize) {
      TokenStream tokenStream = tokenize(document.getValue(), document);
      if (xmlReadMode != XMLReadMode.DEFAULT) {
        String rawText;
        try {
          rawText = Reader.readRawFile(filename);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        if (xmlReadMode == XMLReadMode.RAW_XML_TAC) {
          rawText = rawText.substring(Reader.findXMLTag(rawText, "DOC"));
        }
        String cleanedRawDocumentValue;
        if (rawText.contains("<POST>")) {
          String[] whiteListedRegexes = {"<POST>.*?<\\/POST>"};
          String[] blackListedRegexes = {
                  "<POSTER?>.*?<\\/POSTER?>",
                  "<\\/?.*?>"
          };
          cleanedRawDocumentValue = Reader.cleanRawText(rawText, whiteListedRegexes, 1, blackListedRegexes);
        } else {
          String[] whiteListedRegexes = {"<DOC.*?<\\/DOC>"};
          String[] blackListedRegexes = {
                  "<DOCID>.*?<\\/DOCID>",
                  "<DOCTYPE.*?>.*?<\\/DOCTYPE>",
                  "<DATETIME>.*?<\\/DATETIME>",
                  "<HEADLINE>.*?<\\/HEADLINE>",
                  "<POSTER>.*?<\\/POSTER>",
                  "<\\/?.*?>",
          };
          cleanedRawDocumentValue = Reader.cleanRawText(rawText, whiteListedRegexes, blackListedRegexes);
        }
        tokenStream.reAlignToText(cleanedRawDocumentValue);
        document.setValue(cleanedRawDocumentValue);
      }
      document.addTokenStream(tokenStream);

      TokenStream theTokenStream = document.getDefaultTokenStream();
      int previousPassageAttributesEnd = 0;
      List<Passage> passages = new LinkedList<>();
      List<Sarcasm> sarcasms = new LinkedList<>();
      for (PassageAttributes passageAttributes : passageAttributesList) {
        TokenOffset passageTokenOffset = theTokenStream.findPhrase(
                passageAttributes.getValue(),
                new TokenOffset(previousPassageAttributesEnd, theTokenStream.size() - 1)
        );
        if (passageTokenOffset == null) {
          throw new NoSuchElementException(passageAttributes.getValue());
        }
        Passage passage = new Passage(passageAttributes.getPassageId(), passageTokenOffset, theTokenStream);
        passage.setSpeaker(passageAttributes.getSpeaker());
        passage.setContentType("Post");
        passages.add(passage);
        if (passageAttributes.getSarcasmValue() != null && !passageAttributes.getSarcasmValue().equals("")) {
          Judgment judgment = Judgment.NONE;
          if (passageAttributes.getSarcasmValue().toLowerCase().equals("yes")) {
            judgment = Judgment.POSITIVE;
          } else if (passageAttributes.getSarcasmValue().toLowerCase().equals("no")) {
            judgment = Judgment.NEGATIVE;
          }
          sarcasms.add(new Sarcasm(sarcasms.size(), passage.getTokenOffset(), theTokenStream, judgment));
        }
        previousPassageAttributesEnd = passageTokenOffset.getEnd() + 1;
      }
      hltContentContainer.setPassages(passages);
      hltContentContainer.setSarcasms(sarcasms);
    }
    return document;
  }

  private Document createDocumentFromText(
      String docId,
      Corpus corpus,
      String docType,
      String uri,
      String language,
      HltContentContainer hltContentContainer,
      boolean tokenize,
      TokenizerType tokenizerType,
      String text,
      List<String> textLines) {

    Document document = null;
    document = new Document(docId, corpus, docType, uri, language);
    document.setValue(text);

    if (text != null && textLines.size() != 0 && tokenize) {
        //TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
        TranscriptType transcriptType = TranscriptType.SOURCE;
        ChannelName channelName = ChannelName.NONE;
        ContentType contentType = ContentType.TEXT;
        TokenStream tokenStream =
            new TokenStream(
                tokenizerType,
                transcriptType,
                document != null ? document.getLanguage() : null,
                channelName,
                contentType,
                document != null ? document : null);
        //				tokenStream.setDocument(document);
        List<Passage> passages = new ArrayList<Passage>();
        System.out.println("Number of text lines loaded as passages: " + textLines.size());
        int docCharStart = 0;
        int docTokenIndex = 0;
        long seqId = 0;
        for (String passage : textLines) {
          //System.out.print("Adding Passage ID: " + seqId);
          TokenStream passageLevelTokenStream = tokenize(passage, document);
          //TokenStream passageLevelTokenStream = StanfordTokenizer.getInstance().tokenize(passage, null);
          int tokenId = docTokenIndex;
          int pCharEnd = 0;
          for (Token token : passageLevelTokenStream) {
            int pasCharBegin = token.getCharOffset().getBegin();
            int pasCharEnd = token.getCharOffset().getEnd();
            int newPasCharBegin = docCharStart + pasCharBegin;
            int newPasCharEnd = newPasCharBegin + (pasCharEnd - pasCharBegin);
            Token docToken =
                new Token(
                    tokenId++, new CharOffset(newPasCharBegin, newPasCharEnd), token.getValue());
            docToken.setTokenType(TokenType.LEXEME);
            docToken.setLemma(token.getLemma());
            tokenStream.add(docToken);
            //System.out.println("oldbegin: " + pasCharBegin +
            //		 			", oldend: "   + pasCharEnd +
            //		 			", newbegin: " + newPasCharBegin +
            //		 			", newend: "	+ newPasCharEnd + ", OldValue: " + token.getValue() + ", NewValue: " + docToken.getValue());
            pCharEnd = newPasCharEnd;
          }
          if (pCharEnd > 0) {
            docCharStart = pCharEnd + 1;
            long passageId = seqId++;
            Passage p =
                new Passage(passageId, new TokenOffset(docTokenIndex, tokenId - 1), tokenStream);
            passages.add(p);
            //System.out.println("Passage " + seqId + "added successfully! " + passageString + " " + startIndex + " " + (startIndex+passageLevelTokenStream.size()-1));
            docTokenIndex = tokenId;
            //System.out.println(", value: " + passageString + ", tokens: " + p.getValue());
            //if (!passageString.equals(p.getValue()))
            //{
            //	 System.out.println("");
            //}
          }
        }
        document.addTokenStream(tokenStream);
        //System.out.println("Length of passages arraylist: " + passages.size());
        if (hltContentContainer != null) {
          if (passages.size() > 0) hltContentContainer.setPassages(passages);
        }
    }
    return document;
  }

  /**
   * Tokenize with Stanford CoreNLP
   * @see StanfordTokenizer#tokenize(String, Document)
   * @see TokenizerType#STANFORD_CORENLP
   */
  @Override
  public TokenStream tokenize(String text, Document document) {
    return StanfordTokenizer.getInstance().tokenize(text, document);
  }

}

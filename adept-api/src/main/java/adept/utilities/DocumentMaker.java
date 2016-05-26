/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.utilities;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.ContentType;
import adept.common.Corpus;
import adept.common.Document;
import adept.common.Sarcasm;
import adept.common.Sarcasm.Judgment;
import adept.common.Token;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenOffset;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Utterance;
import adept.common.Passage;
import adept.common.HltContentContainer;
import adept.io.LDCCorpusReader;
import adept.io.Reader;



/**
 * Implements the white space tokenizer, simple text file parser and provides
 * functionality to create a Document object using the aforementioned utilities.
 */
public class DocumentMaker implements ITokenizer {
	// Whether or not to make this a singleton class?
	// Singleton instance
	/** The instance. */
	private static DocumentMaker instance;

	/**
	 * Instantiates a new document maker.
	 */
	private DocumentMaker() {
		System.out
				.println("Instantiating singleton instance for document creator");
	}

	/**
	 * Gets the single instance of DocumentMaker.
	 * 
	 * @return single instance of DocumentMaker
	 */
	public static DocumentMaker getInstance() {
		if (instance == null)
			instance = new DocumentMaker();
		return instance;
	}
	
	/**
	 * Creates the default document. The argument filename *must* be non-null.
	 *
	 * @param filename the filename
	 * @param hltContentContainer the hltContentContainer
	 * @return the document
	 */
    public Document createDefaultDocument(String filename,
                                          HltContentContainer hltContentContainer) {
        return createDefaultDocument("001",
                null,
                "Text",
                filename,
                "English",
                filename,
                hltContentContainer);
    }
	public Document createDefaultDocument(String docId, 
			Corpus corpus,
			String docType, 
			String uri, 
			String language, 
			String filename, 
			HltContentContainer hltContentContainer) {
        return createDefaultDocument(docId,
                corpus,
                docType,
                uri,
                language,
                filename,
                hltContentContainer,
                TokenizerType.APACHE_OPENNLP);
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
     * @return the document
     */
    public Document createDefaultDocument(String docId,
                    Corpus corpus,
                    String docType,
                    String uri,
                    String language,
                    String filename,
                    HltContentContainer hltContentContainer,
                    TokenizerType tokenizerType) {
            checkArgument(filename!=null&& filename.trim().length()>0);
        boolean tokenize = (tokenizerType !=null );
		Document document = null;		
		System.out.println("Creating ADEPT document from file: " + filename);
		
		//check if file is xml-formatted or plain-text
		org.w3c.dom.Document w3cDoc = Reader.getInstance().readXML(filename);
		
		if(w3cDoc != null)
			System.out.println("DOM object obtained successfully");
		
		// XML and SGML documents
		if(w3cDoc != null
				&& (w3cDoc.getElementsByTagName("DOC").item(0) != null || w3cDoc.getElementsByTagName("doc").item(0) != null)
//				&& (w3cDoc.getElementsByTagName("TEXT").item(0) != null || w3cDoc.getElementsByTagName("text").item(0) != null)
				) {
			System.out.println("Loading input document as XML-formatted SGM file");
			List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();

			document = LDCCorpusReader.getInstance().readCorpus(w3cDoc,
													passageAttributesList,
													corpus,
													uri,
													language);

			if(document != null && tokenize) {				
				TranscriptType transcriptType = TranscriptType.SOURCE;
				ChannelName channelName = ChannelName.NONE;
				ContentType contentType = ContentType.TEXT;
				TokenStream tokenStream = new TokenStream(tokenizerType, transcriptType, document != null ? document.getLanguage() : null, channelName,
						contentType, document != null ? document : null);
//				tokenStream.setDocument(document);								
				List<Passage> passages = new ArrayList<Passage>();								
				System.out.println("Number of passages: " + passageAttributesList.size());
				int docCharStart = 0;
				int docTokenIndex = 0;
				long seqId = 0;
				long sarcasmSeqId = 0;
				List<Sarcasm> sarcasms = new ArrayList<Sarcasm>();
				for(PassageAttributes passage : passageAttributesList) {
					 //System.out.print("Adding Passage ID: " + seqId);
                    String passageValue = passage.getValue();
					TokenStream passageLevelTokenStream = tokenize(passageValue, document, tokenizerType);
					 int tokenId = docTokenIndex; 
					 int pCharEnd = 0;
		        	 for (Token token : passageLevelTokenStream) {		 		        		 
		        		 int pasCharBegin = token.getCharOffset().getBegin();
		        		 int pasCharEnd = token.getCharOffset().getEnd();

                         if ( tokenizerType == TokenizerType.UIUC && pasCharEnd + 1 < passageValue.length()) {
                             ++ pasCharEnd;
                         }
		        		 int newPasCharBegin = docCharStart + pasCharBegin;
		        		 int newPasCharEnd = newPasCharBegin + (pasCharEnd-pasCharBegin);		        		 
		        		 Token docToken = new Token(tokenId++, new CharOffset(newPasCharBegin, newPasCharEnd), token.getValue());
		        		 docToken.setTokenType(TokenType.LEXEME);
		        		 docToken.setLemma(token.getLemma());		        		 
		        		 tokenStream.add(docToken);		        		 
		        		 //System.out.println("oldbegin: " + pasCharBegin + 
		        		//		 			", oldend: "   + pasCharEnd +
		        		//		 			", newbegin: " + newPasCharBegin +		        				 			
		        		//		 			", newend: "	+ newPasCharEnd + ", OldValue: " + token.getValue() + ", NewValue: " + docToken.getValue());
		        		 pCharEnd = newPasCharEnd;
		        	 }
		        	 docCharStart = pCharEnd+1 + passage.getPostPassageOffset(); 
		        	 long passageId = seqId++;
		        	 passageId = passage.getPassageId();
		        	 Passage p = new Passage(passageId, new TokenOffset(docTokenIndex, tokenId-1), tokenStream);
					 p.setSpeaker(passage.getSpeaker());
					 p.setContentType("Post");
		             passages.add(p);
		             //System.out.println("Passage " + seqId + "added successfully! " + passageString + " " + startIndex + " " + (startIndex+passageLevelTokenStream.size()-1));
		             docTokenIndex = tokenId;
		             //System.out.println(", value: " + passageString + ", tokens: " + p.getValue());
		             //if (!passageString.equals(p.getValue()))
		             //{
		             //	 System.out.println("");
		             //}
		             
		             if (passage.getSarcasmValue() != null) {
                                 if(!passage.getSarcasmValue().equals(""))
                                 {
		            	  Judgment judgment = Judgment.NONE;
		            	  if (passage.getSarcasmValue().toLowerCase().equals("yes")) {
		            		 judgment = Judgment.POSITIVE;
		            	  } else if (passage.getSarcasmValue().toLowerCase().equals("no")) {
		            		 judgment = Judgment.NEGATIVE;
		            	  }
		            	  Sarcasm sarcasm = new Sarcasm(sarcasmSeqId++, p.getTokenOffset(), tokenStream, judgment);
		            	  sarcasms.add(sarcasm);
                                 }
		                 }		            
		            }
				//System.out.println("Length of passages arraylist: " + passages.size());
				if(hltContentContainer != null) {
					if (passages.size() > 0) {
                        System.out.println("Number of passages to HLTCC: " + passages.size());
						hltContentContainer.setPassages(passages);
                    }
					if (sarcasms.size() > 0) {
						hltContentContainer.setSarcasms(sarcasms);
				}
				}
				document.addTokenStream(tokenStream);
			}			
		} else {
			System.out.println("Loading input document as raw text file");
			document = new Document(docId, corpus, docType, uri, language);
			List<String> textLines = new ArrayList<String>();
			String text = null;
			try
			{
				text = Reader.getInstance().readFileIntoLines(filename, textLines);
			}
			catch(Exception e)
			{
				System.out.println("Error reading file: " + filename);
				e.printStackTrace();
			}
			document.setValue(text);
			
			if(text != null && textLines.size() != 0 && tokenize) {				
				TranscriptType transcriptType = TranscriptType.SOURCE;
				ChannelName channelName = ChannelName.NONE;
				ContentType contentType = ContentType.TEXT;
				TokenStream tokenStream = new TokenStream(tokenizerType, transcriptType, document != null ? document.getLanguage() : null, channelName,
						contentType, document != null ? document : null);
//				tokenStream.setDocument(document);								
				List<Passage> passages = new ArrayList<Passage>();								
				System.out.println("Number of text lines loaded as passages: " + textLines.size());				
				int docCharStart = 0;
				int docTokenIndex = 0;
				long seqId = 0;
				for(String passage : textLines) {
					 //System.out.print("Adding Passage ID: " + seqId);					 
                    TokenStream passageLevelTokenStream = tokenize(passage, document, tokenizerType);

					 int tokenId = docTokenIndex; 
					 int pCharEnd = 0;
		        	 for (Token token : passageLevelTokenStream) {		 		        		 
		        		 int pasCharBegin = token.getCharOffset().getBegin();
		        		 int pasCharEnd = token.getCharOffset().getEnd();
		        		 int newPasCharBegin = docCharStart + pasCharBegin;
		        		 int newPasCharEnd = newPasCharBegin + (pasCharEnd-pasCharBegin);		        		 
		        		 Token docToken = new Token(tokenId++, new CharOffset(newPasCharBegin, newPasCharEnd), token.getValue());
		        		 docToken.setTokenType(TokenType.LEXEME);
		        		 docToken.setLemma(token.getLemma());		        		 
		        		 tokenStream.add(docToken);		        		 
		        		 //System.out.println("oldbegin: " + pasCharBegin + 
		        		//		 			", oldend: "   + pasCharEnd +
		        		//		 			", newbegin: " + newPasCharBegin +		        				 			
		        		//		 			", newend: "	+ newPasCharEnd + ", OldValue: " + token.getValue() + ", NewValue: " + docToken.getValue());
		        		 pCharEnd = newPasCharEnd;
		        	 }
		        	 if ( pCharEnd > 0 ) {
			        	 docCharStart = pCharEnd+1; 
			        	 long passageId = seqId++;		        	 
			        	 Passage p = new Passage(passageId, new TokenOffset(docTokenIndex, tokenId-1), tokenStream);
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
				if(hltContentContainer != null) {
					if (passages.size() > 0) 
						hltContentContainer.setPassages(passages);
				}				
			}						
		}

		return document;
	}
	
	/**
	 * Creates a Document instance of type Conversation.
	 *
	 * @param docId the doc id
	 * @param corpus the corpus
	 * @param uri the uri
	 * @param language the language
	 * @param filename the filename
	 * @param hltContentContainer the hltContentContainer
	 * @return the document
	 */
	public Document createConversationDocument(String docId, 
			Corpus corpus,
			String uri, 
			String language, 
			String filename, 
			HltContentContainer hltContentContainer)
	{
		Document document = null;
		String data = null;

		System.out.println(filename);
	
		List<String> utterances = new ArrayList<String>();
		List<String> speakers = new ArrayList<String>();
		String title = null;
		String docText = Reader.getInstance().readFileIntoString(filename);
		document = new Document(docId, corpus, "conversation", uri, language);
		document.setValue(docText);
		document.setHeadline(title);
		
		TokenStream tokenStream = OpenNLPTokenizer.getInstance().tokenize(document.getValue(), document);

		document.addTokenStream(tokenStream);
		// DEBUG
		System.out.printf("Tokenized with %d tokens in document URI=%s ID=%s.\n", tokenStream.size(), document.getUri(), document.getDocId());

		// map utterance token offsets to document token offsets.
		TokenStream utteranceLevelTokenStream;
		List<Utterance> utteranceObjects = new ArrayList<Utterance>();
		long seqId = 0;
		int startIndex = 0;
		for(int i=0;i<utterances.size();i++)
         {
			 String utt = utterances.get(i);
			 System.out.println(utt);
        	 utteranceLevelTokenStream = OpenNLPTokenizer.getInstance().tokenize(utt, null);
        	 utteranceObjects.add(new Utterance(new TokenOffset(startIndex,startIndex+utteranceLevelTokenStream.size()-1), tokenStream, seqId++, speakers.get(i), utt));
             //System.out.println("Utterance added successfully! " + utt + " " + startIndex + " " + (startIndex+utteranceLevelTokenStream.size()-1));
             startIndex += utteranceLevelTokenStream.size();
         }
		if(hltContentContainer != null) hltContentContainer.setUtterances(utteranceObjects);
		return document;
	}
	
	// Apache Open NLP tokenizer
	@Override
    public TokenStream tokenize(String text, Document document) {
        return tokenize(text, document, TokenizerType.APACHE_OPENNLP);
    }

            public TokenStream tokenize(String text, Document document, TokenizerType tokenizerType )	{

		tokenizerType = TokenizerType.APACHE_OPENNLP;
		TranscriptType transcriptType = TranscriptType.SOURCE;
		ChannelName channelName = ChannelName.NONE;
		ContentType contentType = ContentType.TEXT;
		TokenStream tokenStream = new TokenStream(tokenizerType, transcriptType, document != null ? document.getLanguage() : null, channelName,
				contentType, document != null ? document : null);
//		tokenStream.setDocument(document);

		Tokenizer tokenizer = SimpleTokenizer.INSTANCE;

		Span tokenSpans[] = tokenizer.tokenizePos(text);

		long tokenSequenceId = 0;

		for (Span span: tokenSpans) {
			int charBegin = span.getStart();
			int charEnd = span.getEnd();
			String word = text.substring(charBegin, charEnd);
			Token token = new Token(tokenSequenceId++, new CharOffset(charBegin, charEnd), word);
			token.setTokenType(TokenType.LEXEME);
			tokenStream.add(token);
		}

		return tokenStream;
	}
	
	// test fixture
	/*public static void main(String[] args)
	{
		DocumentMaker docMaker = new DocumentMaker();
		TokenStream ts = docMaker.tokenize("hi this is me.\nwho are you?", null);
		for(Token t : ts)
		{
			System.out.println(t.getValue() + "  " + t.getCharOffset().getBegin() + "  " + t.getCharOffset().getEnd());
		}
		HltContentContainer hltContentContainer = new HltContentContainer();
		LDCCorpusReader.getInstance().setAddPeriodsOption(true);
		Document doc = DocumentMaker.getInstance().createDefaultDocument("001", null, null, null, "English", "/nfs/mercury-04/u40/deft/osc_1_noperiods/en-wb-en-src-OSC20130806_Worldwide_Open_Source_News.62.10342.0.0.sgm", hltContentContainer, true);
		System.out.println("Doc value:");
		System.out.println(doc.getValue());
	}*/
}


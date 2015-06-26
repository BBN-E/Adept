package adept.utilities;
/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkArgument;

import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetBeginAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.CharacterOffsetEndAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

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


// TODO: Auto-generated Javadoc
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

/*    public Document createCleanedXMLDocument(String docId, 
			Corpus corpus,
			String docType, 
			String uri, 
			String language, 
			String filename, 
			HltContentContainer hltContentContainer, boolean tokenize) {
		Document document = null;		
        List<Tag> tags = null;
		System.out.println("Creating ADEPT document from file: " + filename);

        String rawText = Reader.getInstance().readFileIntoString(filename);

		if(rawText != null)
			System.out.println("File read successfully");

        List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();		
        Pair<List<Tag>,Document> docTagPair = LDCCorpusReader.getInstance().readCorpus(rawText,
													passageAttributesList,
													corpus,
													uri,
													language);
        document = docTagPair.getR();
        tags = docTagPair.getL();

        Set<String> passageTags = new HashSet<String>(Arrays.asList(new String[] {"p", "post"}));
        Set<String> headlineTags = new HashSet<String>(Arrays.asList(new String[] {"headline"}));

        //tokenize the document
        TokenStream tokenStream = StanfordTokenizer.getInstance().tokenize(document.getValue(), document);		     
        //map between charOffsets begins and TokenOffset begins 
        Map<Integer, Integer> charOffsetToTokenIndexMap = new HashMap<Integer, Integer>();
        //from beginning of document till beginning of first token
        for(int offset = 0; offset < tokenStream.get(0).getCharOffset().getBegin(); offset++)
        {
            charOffsetToTokenIndexMap.put(offset,0);
        }
        //from beginning of first token to beginning of last token...
        for(int x = 0; x < tokenStream.size()-1; x++)
        {
            Token currToken = tokenStream.get(x);
            Token nextToken = tokenStream.get(x+1);

            for(int offset = currToken.getCharOffset().getBegin(); offset < nextToken.getCharOffset().getBegin(); offset++)
            {
                if(offset < 20)
                    System.out.println(offset + ":" + x);
                charOffsetToTokenIndexMap.put(offset,x);
            }
        }
        //from beginning of last token until end of document
        for(int offset = tokenStream.get(tokenStream.size()-1).getCharOffset().getBegin(); offset <  document.getValue().length(); offset++)
        {
            charOffsetToTokenIndexMap.put(offset,tokenStream.size()-1);
        }


        long seqId = 0;
        long sarcasmSeqId = 0;
        List<Passage> passages = new ArrayList<Passage>();								            
        List<Sarcasm> sarcasms = new ArrayList<Sarcasm>();								            
        String headline = "";
        for(Tag tag : tags)
        {
            CharOffset tagOffsets = tag.getCharOffset();
            //create Passages
            if(passageTags.contains(tag.getTagName().toLowerCase()))
            {
                long passageId = seqId++;
                System.out.println(tagOffsets.getBegin());
                int start = charOffsetToTokenIndexMap.get(tagOffsets.getBegin());
                int end = charOffsetToTokenIndexMap.get(tagOffsets.getEnd());
                Passage p = new Passage(passageId, new TokenOffset(start,end), tokenStream);
                passages.add(p);

                if(tag.getAttributes().keySet().contains("pid"))
                    passageId = Long.valueOf(tag.getAttributes().get("pid").getValue());

                //get the sarcasm while we're here
                if(tag.getAttributes().keySet().contains("sarcasm"))
                {
                    Judgment judgment = Judgment.NONE;
                    String sarcasmValue = tag.getAttributes().get("sarcasm").getValue();
                    if (sarcasmValue.toLowerCase().equals("yes")) {
                        judgment = Judgment.POSITIVE;
                    } else if (sarcasmValue.toLowerCase().equals("no")) {
                        judgment = Judgment.NEGATIVE;
                    }
                    Sarcasm sarcasm = new Sarcasm(sarcasmSeqId++, p.getTokenOffset(), tokenStream, judgment);
                    sarcasms.add(sarcasm);
                }
            }
            //get the headline
            else if(headlineTags.contains(tag.getTagName().toLowerCase()))
            {
                headline = document.getValue().substring(tagOffsets.getBegin(), tagOffsets.getEnd()).trim();
            }
        }

        document.setHeadline(headline);
        List<TokenStream> tokenStreamList = new ArrayList<TokenStream>();
        tokenStreamList.add(tokenStream);
        document.setTokenStreamList(tokenStreamList);
        hltContentContainer.setPassages(passages);
        hltContentContainer.setSarcasms(sarcasms);

        return document;
    }*/
	
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
                TokenizerType.STANFORD_CORENLP);
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
		if(w3cDoc != null && (w3cDoc.getElementsByTagName("DOC").item(0) != null || w3cDoc.getElementsByTagName("doc").item(0) != null)) {
			System.out.println("Loading input document as XML-formatted SGM file");
			List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();		
			
			document = LDCCorpusReader.getInstance().readCorpus(w3cDoc,
													passageAttributesList,
													corpus,
													uri,
													language);
			
			if(document != null && tokenize) {				
				//TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
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
                    //TokenStream passageLevelTokenStream = StanfordTokenizer.getInstance().tokenize(passage.getValue(), null);
					 int tokenId = docTokenIndex; 
					 int pCharEnd = 0;
		        	 for (Token token : passageLevelTokenStream) {		 		        		 
		        		 int pasCharBegin = token.getCharOffset().getBegin();
		        		 int pasCharEnd = token.getCharOffset().getEnd();
                         // Stanford tokenizer by default sets end to one past the token end.  Make UIUC do same.
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
				//TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
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
					//TokenStream passageLevelTokenStream = StanfordTokenizer.getInstance().tokenize(passage, null);
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
		String docText = Reader.getInstance().readConversationFile(filename, utterances, speakers, title);
		document = new Document(docId, corpus, "conversation", uri, language);
		document.setValue(docText);
		document.setHeadline(title);
		
		TokenStream tokenStream = StanfordTokenizer.getInstance().tokenize(document.getValue(), document);
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
        	 utteranceLevelTokenStream = StanfordTokenizer.getInstance().tokenize(utt, null);
        	 utteranceObjects.add(new Utterance(new TokenOffset(startIndex,startIndex+utteranceLevelTokenStream.size()-1), tokenStream, seqId++, speakers.get(i), utt));
             //System.out.println("Utterance added successfully! " + utt + " " + startIndex + " " + (startIndex+utteranceLevelTokenStream.size()-1));
             startIndex += utteranceLevelTokenStream.size();
         }
		if(hltContentContainer != null) hltContentContainer.setUtterances(utteranceObjects);
		return document;
	}

	
	
	// document can be null in the argument
	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.test.ITokenizer#tokenize(java.lang.String,
	 * adept.common.Document)
	 */
	
	/*public TokenStream tokenize(String text, Document document) {
		TokenStream tokenStream = new TokenStream(TokenizerType.STANFORD_CORENLP, null,
				document != null ? document.getLanguage() : null, null,
				ContentType.TEXT, document != null ? document.getValue() : null);
		tokenStream.setDocument(document);

		int currOffset = 0, seqId = 0;
		String[] temp = text.split(" ");
		for (String token : temp) {
			Token textToken = new Token(seqId++, new CharOffset(currOffset,
					currOffset + token.length() - 1), token);
			tokenStream.add(textToken);
			
			// +1 to take space offset into account
			currOffset += (token.length() + 1);
		}

		return tokenStream;
	}
	*/
	
	// Stanford Core NLP tokenizer
	@Override
    public TokenStream tokenize(String text, Document document) {
        return tokenize(text, document, TokenizerType.STANFORD_CORENLP);
    }

            public TokenStream tokenize(String text, Document document, TokenizerType tokenizerType )	{
		// System.out.println("in tokenize() in DocMaker");
		Properties props = new Properties();
	    props.put("annotators", "tokenize,ssplit,pos,lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		
		//TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
		TranscriptType transcriptType = TranscriptType.SOURCE;
		ChannelName channelName = ChannelName.NONE;
		ContentType contentType = ContentType.TEXT;
		TokenStream tokenStream = new TokenStream(tokenizerType, transcriptType, document != null ? document.getLanguage() : null, channelName,
				contentType, document != null ? document : null);
//		tokenStream.setDocument(document);

		long tokenSequenceId = 0;
		for (CoreMap tokenAnn : annotation.get(TokensAnnotation.class)) {

			// create the token annotation
			int charBegin = tokenAnn.get(CharacterOffsetBeginAnnotation.class);
			int charEnd = tokenAnn.get(CharacterOffsetEndAnnotation.class);

			String lemma = tokenAnn.get(LemmaAnnotation.class);
			String word = tokenAnn.get(TextAnnotation.class);
			Token token = new Token(tokenSequenceId++, new CharOffset(charBegin, charEnd), word);
			token.setTokenType(TokenType.LEXEME);
			token.setLemma(lemma);
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


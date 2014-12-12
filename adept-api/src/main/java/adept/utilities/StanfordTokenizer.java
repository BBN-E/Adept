/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.utilities;


import java.util.Properties;

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
import adept.common.Token;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.io.LDCCorpusReader;
import adept.io.Reader;

// TODO: Auto-generated Javadoc
/**
 * The Class StanfordTokenizer.
 */
public class StanfordTokenizer implements ITokenizer {
	
	/** The tokenizer instance. */
	private static StanfordTokenizer instance;

	/**
	 * Constructor to instantiate a new Stanford tokenizer.
	 */
	private StanfordTokenizer() {
		System.out.println("Instantiating singleton instance for document creator");
	}

	/**
	 * Gets the single instance of StanfordTokenizer.
	 * 
	 * @return single instance of StanfordTokenizer
	 */
	public static StanfordTokenizer getInstance() {
		if (instance == null)
			instance = new StanfordTokenizer();
		return instance;
	}
	
	// Stanford Core NLP tokenizer
	/* (non-Javadoc)
	 * @see adept.utilities.ITokenizer#tokenize(java.lang.String, adept.common.Document)
	 */
	@Override
	public TokenStream tokenize(String text, Document document) {
		Properties props = new Properties();
	    props.put("annotators", "tokenize");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		
		TokenizerType tokenizerType = TokenizerType.STANFORD_CORENLP;
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
	
	
}

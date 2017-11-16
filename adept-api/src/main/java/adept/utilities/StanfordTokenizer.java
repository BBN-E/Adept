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


import java.util.Properties;
import java.util.regex.Pattern;

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
	    props.put("annotators", "tokenize,ssplit,pos,lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(text);
		pipeline.annotate(annotation);
		
		TranscriptType transcriptType = TranscriptType.SOURCE;
		ChannelName channelName = ChannelName.NONE;
		ContentType contentType = ContentType.TEXT;
		TokenStream tokenStream = new TokenStream(TokenizerType.STANFORD_CORENLP, transcriptType, document != null ? document.getLanguage() : null, channelName,
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

		adjustTokenStreamForStrictTreebank3(tokenStream);
		return tokenStream;
    }

	private static void adjustTokenStreamForStrictTreebank3(TokenStream tokenStream) {
		// This is to account for inconsistent behavior of the 'strictTreebank3' rule of the Stanford PTBTokenizer
		// (see https://nlp.stanford.edu/nlp/javadoc/javanlp/edu/stanford/nlp/process/PTBTokenizer.html)
		// In practice, this is so that TokenStream.findStream() works reliably (i.e., always tokenizes the given
		// phrase in a way that matches the target sublist of the stream).
		int currentTokenStreamSize = tokenStream.size();
		for (int i = 0; i < currentTokenStreamSize - 1; i++) {
			Token token = tokenStream.get(i);
			String tokenValue = token.getValue();
			CharOffset tokenCharOffset = token.getCharOffset();
			if (token.getValue().endsWith(".")
					&& tokenCharOffset.getEnd() - tokenCharOffset.getBegin() == tokenValue.length() - 1
					&& tokenStream.get(i + 1).getValue().equals(".")) {
				tokenStream.remove(i + 1);
				tokenStream.set(i, Token.modifyCharOffset(token, new CharOffset(tokenCharOffset.getBegin(), tokenCharOffset.getEnd() + 1)));
				currentTokenStreamSize = tokenStream.size();
			}
		}
		for (int i = 0; i < currentTokenStreamSize; i++) {
			Token token = tokenStream.get(i);
			String tokenValue = token.getValue();
			CharOffset tokenCharOffset = token.getCharOffset();
			if (Pattern.matches("[A-z]\\.", tokenValue)) {
				Token letterToken = Token.modifyCharOffset(Token.modifyValue(token, tokenValue.substring(0, 1)), new CharOffset(tokenCharOffset.getBegin(), tokenCharOffset.getEnd() - 1));
				Token periodToken = Token.modifyCharOffset(Token.modifyValue(token, "."), new CharOffset(tokenCharOffset.getBegin() + 1, tokenCharOffset.getEnd()));
				tokenStream.remove(i);
				tokenStream.add(i, periodToken);
				tokenStream.add(i, letterToken);
				i++;
				currentTokenStreamSize = tokenStream.size();
			}
		}

		for (int i = 0; i < currentTokenStreamSize; i++) {
			tokenStream.set(i, Token.modifySequenceId(tokenStream.get(i), i));
		}
	}
	
	
}

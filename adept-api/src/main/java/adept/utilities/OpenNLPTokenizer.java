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


import java.util.Properties;

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

import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

/**
 * The Class OpenNLPTokenizer.
 */
public class OpenNLPTokenizer implements ITokenizer {
	
	/** The tokenizer instance. */
	private static OpenNLPTokenizer instance;

	/**
	 * Constructor to instantiate a new OpenNLP tokenizer.
	 */
	private OpenNLPTokenizer() {
		System.out.println("Instantiating singleton instance for document creator");
	}

	/**
	 * Gets the single instance of OpenNLPTokenizer.
	 * 
	 * @return single instance of OpenNLPTokenizer
	 */
	public static OpenNLPTokenizer getInstance() {
		if (instance == null)
			instance = new OpenNLPTokenizer();
		return instance;
	}
	
	// Stanford Core NLP tokenizer
	/* (non-Javadoc)
	 * @see adept.utilities.ITokenizer#tokenize(java.lang.String, adept.common.Document)
	 */
	@Override
	public TokenStream tokenize(String text, Document document) {

		TokenizerType tokenizerType = TokenizerType.APACHE_OPENNLP;
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
	
	
}

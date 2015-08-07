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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;


/**
 * The Class TokenStream, which is a list of tokens 
 * and represents the tokenized form of a entire document.  
 * If multiple tokenizers are used then a document 
 * may contain multiple TokenStream objects.
 */
public class TokenStream extends ArrayList<Token> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2407040331925099456L;

	/** The tokenizer type. */
	private final TokenizerType tokenizerType;

	/** The content type. */
	private final TranscriptType transcriptType;

	/** The language. */
	private final String language;

	/** The channel name. */
	private final ChannelName channelName;

	/** The content type. */
	private final ContentType contentType;

	/** The asr name. */
	private AsrName asrName;

	/** The speech unit. */
	private SpeechUnit speechUnit;

	/** The translator name. */
	private TranslatorName translatorName;

	/** The text value. */
	private final String textValue;

	/** The document. */
	private final Document document;

	/**
	 * private constructor.
	 * 
	 * @param tokenizerType
	 *            the tokenizer type
	 * @param transcriptType
	 *            the transcript type
	 * @param language
	 *            the language
	 * @param channelName
	 *            the channel name
	 * @param contentType
	 *            the content type
	 * @param textValue
	 *            the text value
	 */
	/*
	 * private TokenStream() { this(null,null,null,null,null,null); }
	 */

	/**
	 * Instantiates a new token stream.
	 * 
	 * @param tokenizerType
	 *            the tokenizer type
	 * @param transcriptType
	 *            the transcript type
	 * @param language
	 *            the language
	 * @param channelName
	 *            the channel name
	 * @param contentType
	 *            the content type
	 * @param textValue
	 *            the text value
	 */
	public TokenStream(TokenizerType tokenizerType,
						TranscriptType transcriptType, 
						String language,
						ChannelName channelName, 
						ContentType contentType, 
						Document document) {
		super();

        checkArgument(tokenizerType!=null);           
		this.tokenizerType = tokenizerType;

                //TODO: null checks
		this.language = language;
		this.channelName = channelName;
		this.contentType = contentType;
		this.document = document;
        this.transcriptType = transcriptType;
//		document.addTokenStream(this);
		this.textValue = document.getValue();
	}

	/**
	 * Gets the asr name.
	 * 
	 * @return the asr name
	 */
	public AsrName getAsrName() {
		return asrName;
	}

	/**
	 * Sets the asr name.
	 * 
	 * @param asrName
	 *            the new asr name
	 */
	public void setAsrName(AsrName asrName) {
                // TODO: null check
		this.asrName = asrName;
	}

	/**
	 * Gets the speech unit.
	 * 
	 * @return the speech unit
	 */
	public SpeechUnit getSpeechUnit() {
		return speechUnit;
	}

	/**
	 * Sets the speech unit.
	 * 
	 * @param speechUnit
	 *            the new speech unit
	 */
	public void setSpeechUnit(SpeechUnit speechUnit) {
                //TODO: null check
		this.speechUnit = speechUnit;
	}

	/**
	 * Gets the translator name.
	 * 
	 * @return the translator name
	 */
	public TranslatorName getTranslatorName() {
		return translatorName;
	}

	/**
	 * Sets the translator name.
	 * 
	 * @param translatorName
	 *            the new translator name
	 */
	public void setTranslatorName(TranslatorName translatorName) {
                //TODO: null check
		this.translatorName = translatorName;
	}


	/**
	 * Gets the text value.
	 * 
	 * @return the text value
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the tokenizer type.
	 * 
	 * @return the tokenizer type
	 */
	public TokenizerType getTokenizerType() {
		return tokenizerType;
	}

	/**
	 * Gets the transcript type.
	 * 
	 * @return the transcript type
	 */
	public TranscriptType getTranscriptType() {
		return transcriptType;
	}

	/**
	 * Gets the language.
	 * 
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the channel name.
	 * 
	 * @return the channel name
	 */
	public ChannelName getChannelName() {
		return channelName;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	
	/**
	 * Find phrase token offset.
	 *
	 * @param phrase the phrase
	 * @param tokenOffset the token offset
	 * @return the token offset
	 */
	public TokenOffset findPhraseTokenOffset(String phrase, TokenOffset tokenOffset) {				
		String[] words = phrase.trim().split(" ");
		TokenOffset phraseTokenOffset = null;
		int begin = -1;
		int end = -1;		
		for(String word : words) {			
			for(int i = tokenOffset.getBegin(); i <= tokenOffset.getEnd(); i++) {				
				Token token = this.get(i);
				//System.out.println("Token Value: " + token.getValue() + ", word: " + word);
				if (token.getValue().equals(word)) {				
					if (begin == -1) {
						begin = i;
					}
					else {
						end = i;
					    break;    	
					}
				}					
			}		
		}
		if (words.length == 1) {
			end = begin;
		}
        if(end < begin)
        	return null;
		if (begin != -1 && end != -1) {
			phraseTokenOffset = new TokenOffset(begin,end);
		}		
		return phraseTokenOffset;
	}
	
}
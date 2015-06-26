/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class TextTokenStream.
 */
public class TokenStreamHandler extends ArrayList<Token> implements TokenStreamService.Iface {

	private TokenStream myTokenStream;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2407040331925099456L;

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
	 * private TokenStream() { myTokenStream(null,null,null,null,null,null); }
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
	public TokenStreamHandler(TokenizerType tokenizerType,
			TranscriptType transcriptType, String language,
			ChannelName channelName, ContentType contentType, String textValue) {
		super();
		myTokenStream = new TokenStream();
		myTokenStream.tokenizerType = tokenizerType;
		myTokenStream.transcriptType = transcriptType;
		myTokenStream.language = language;
		myTokenStream.channelName = channelName;
		myTokenStream.contentType = contentType;
		myTokenStream.textValue = textValue;
	}

	/**
	 * Gets the asr name.
	 * 
	 * @return myTokenStream.the asr name
	 */
	public AsrName getAsrName() {
		return myTokenStream.asrName;
	}

	/**
	 * Sets the asr name.
	 * 
	 * @param asrName
	 *            the new asr name
	 */
	public void setAsrName(AsrName asrName) {
		myTokenStream.asrName = asrName;
	}

	/**
	 * Gets the speech unit.
	 * 
	 * @return myTokenStream.the speech unit
	 */
	public SpeechUnit getSpeechUnit() {
		return myTokenStream.speechUnit;
	}

	/**
	 * Sets the speech unit.
	 * 
	 * @param speechUnit
	 *            the new speech unit
	 */
	public void setSpeechUnit(SpeechUnit speechUnit) {
		myTokenStream.speechUnit = speechUnit;
	}

	/**
	 * Gets the translator name.
	 * 
	 * @return myTokenStream.the translator name
	 */
	public TranslatorName getTranslatorName() {
		return myTokenStream.translatorName;
	}

	/**
	 * Sets the translator name.
	 * 
	 * @param translatorName
	 *            the new translator name
	 */
	public void setTranslatorName(TranslatorName translatorName) {
		myTokenStream.translatorName = translatorName;
	}

	/**
	 * Gets the text value.
	 * 
	 * @return myTokenStream.the text value
	 */
	public String getTextValue() {
		return myTokenStream.textValue;
	}

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return myTokenStream.the serialversionuid
	 */
	public long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the tokenizer type.
	 * 
	 * @return myTokenStream.the tokenizer type
	 */
	public TokenizerType getTokenizerType() {
		return myTokenStream.tokenizerType;
	}

	/**
	 * Gets the transcript type.
	 * 
	 * @return myTokenStream.the transcript type
	 */
	public TranscriptType getTranscriptType() {
		return myTokenStream.transcriptType;
	}

	/**
	 * Gets the language.
	 * 
	 * @return myTokenStream.the language
	 */
	public String getLanguage() {
		return myTokenStream.language;
	}

	/**
	 * Gets the channel name.
	 * 
	 * @return myTokenStream.the channel name
	 */
	public ChannelName getChannelName() {
		return myTokenStream.channelName;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return myTokenStream.the content type
	 */
	public ContentType getContentType() {
		return myTokenStream.contentType;
	}

	/**
	 * Gets the document.
	 * 
	 * @return myTokenStream.the document
	 */
	public Document getDocument() {
		return myTokenStream.document;
	}

	/**
	 * Sets the document.
	 * 
	 * @param document
	 *            the new document
	 */
	public void setDocument(Document document) {
		myTokenStream.document = document;
	}

	
	public TokenOffset findPhraseTokenOffset(String phrase, TokenOffset tokenOffset) {				
		String[] words = phrase.split(" ");
		TokenOffset phraseTokenOffset = null;
		long begin = -1;
		long end = -1;
		
		for(String word : words) {
			
			for(long i = tokenOffset.beginIndex; i <= tokenOffset.endIndex; i++) {
				
				Token token = get((int)i);
				//System.out.println("Token Value: " + token.getValue() + ", word: " + word);
				if (token.getValue().equals(word)) {				
					if (begin == -1) {
						begin = i;
					}
					else {
						end = i;
					}
				}					
			}		
		}
		if (words.length == 1) {
			end = begin;
		}
		if (begin != -1 && end != -1) {
			phraseTokenOffset = new TokenOffset(begin,end);
		}		
		return phraseTokenOffset;
	}
	
	public TokenStream getTokenStream() {
		return myTokenStream;
	}

}

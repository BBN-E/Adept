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


/**
 * The Class Token.
 */
public class TokenHandler extends ItemHandler implements TokenService.Iface {

	private Token myToken;

	/**
	 * Instantiates a new token.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param charOffset
	 *            the char offset
	 * @param value
	 *            the value
	 */
	public TokenHandler(long sequenceId, CharOffset charOffset, String value) {
		myToken = new Token();
		myToken.sequenceId = sequenceId;
		myToken.charOffset = charOffset;
		myToken.value = value;
		myToken.id = myItem.id;
		myToken.value = myItem.value;
	}

	/**
	 * Gets the char offset.
	 * 
	 * @return myToken.the char offset
	 */
	public CharOffset getCharOffset() {
		return myToken.charOffset;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myToken.the sequence id
	 */
	public long getSequenceId() {
		return myToken.sequenceId;
	}

	/**
	 * Gets the token type.
	 * 
	 * @return myToken.the token type
	 */
	public TokenType getTokenType() {
		return myToken.tokenType;
	}

	/**
	 * Sets the token type.
	 * 
	 * @param tokenType
	 *            the new token type
	 */
	public void setTokenType(TokenType tokenType) {
		myToken.tokenType = tokenType;
	}

	/**
	 * Gets the lemma.
	 * 
	 * @return myToken.the lemma
	 */
	public String getLemma() {
		return myToken.lemma;
	}

	/**
	 * Sets the lemma.
	 * 
	 * @param lemma
	 *            the new lemma
	 */
	public void setLemma(String lemma) {
		myToken.lemma = lemma;
	}

	/**
	 * Gets the audio offset.
	 * 
	 * @return myToken.the audio offset
	 */
	public AudioOffset getAudioOffset() {
		return myToken.audioOffset;
	}

	/**
	 * Sets the audio offset.
	 * 
	 * @param audioOffset
	 *            the new audio offset
	 */
	public void setAudioOffset(AudioOffset audioOffset) {
		myToken.audioOffset = audioOffset;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myToken.the confidence
	 */
	public double getConfidence() {
		return myToken.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		myToken.confidence = confidence;
	}

	public Token getToken() {
		return myToken;
	}

}

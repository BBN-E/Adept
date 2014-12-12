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

// TODO: Auto-generated Javadoc
/**
 * The Class Utterance.
 */
public class UtteranceHandler extends ChunkHandler implements UtteranceService.Iface {

	private Utterance myUtterance;

	/**
	 * Instantiates a new utterance.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param utteranceId
	 *            the utterance id
	 * @param speakerId
	 *            the speaker id
	 * @param annotation
	 *            the annotation
	 */
	public UtteranceHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long utteranceId, long speakerId, String utterance) {
		super(tokenOffset, tokenStream);
		myUtterance = new Utterance();
		myUtterance.utteranceId = utteranceId;
		myUtterance.speakerId = speakerId;
		myUtterance.annotation = utterance;
		myUtterance.id = myItem.id;
		myUtterance.value = myItem.value;
		myUtterance.tokenOffset = myChunk.tokenOffset;
		myUtterance.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the utterance id.
	 * 
	 * @return myUtterance.the utterance id
	 */
	public long getUtteranceId() {
		return myUtterance.utteranceId;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return myUtterance.the speaker id
	 */
	public long getSpeakerId() {
		return myUtterance.speakerId;
	}


	/**
	 * Gets the annotation.
	 * 
	 * @return myUtterance.the annotation
	 */
	public String getAnnotation() {
		return myUtterance.annotation;
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 *            the new annotation
	 */
	public void setAnnotation(String annotation) {
		myUtterance.annotation = annotation;
	}

	public Utterance getUtterance() {
		return myUtterance;
	}

}

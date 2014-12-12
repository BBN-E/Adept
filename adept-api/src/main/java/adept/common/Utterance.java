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
package adept.common;

// TODO: Auto-generated Javadoc
/**
 * The Class Utterance.
 */
public class Utterance extends Chunk {

	/** The utterance id. */
	private final long utteranceId;

	/** The speaker id. */
	private final String speakerId;

	/** The annotation. */
	private String annotation;
	
	/** The utterance. */
	private String utterance;

	/**
	 * Instantiates a new utterance.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param utteranceId the utterance id
	 * @param speakerId the speaker id
	 * @param utterance the utterance
	 */
	public Utterance(TokenOffset tokenOffset, TokenStream tokenStream,
			long utteranceId, String speakerId, String utterance) {
		super(tokenOffset, tokenStream);
		this.utteranceId = utteranceId;
		this.speakerId = speakerId;
		this.utterance = utterance;
	}

	/**
	 * Gets the utterance id.
	 * 
	 * @return the utterance id
	 */
	public long getUtteranceId() {
		return utteranceId;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return the speaker id
	 */
	public String getSpeakerId() {
		return speakerId;
	}
	
	/**
	 * Gets the utterance.
	 * 
	 * @return the utterance
	 */
	public String getUtterance() {
		return utterance;
	}

	/**
	 * Gets the annotation.
	 * 
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 *            the new annotation
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

}

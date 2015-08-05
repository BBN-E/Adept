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

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Passage, which represents a section of text 
 * which has been extracted from a document, particularly 
 * a section of medium length. Every DEFT Document has at
 * least one passage.
 */
public class Passage extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The content type. */
	private String contentType;

	/** The AudioOffset. */
	private AudioOffset audioOffset;

	/** The speaker. */
	private String speaker;

	/**
	 * Instantiates a new passage.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public Passage(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
                //TODO: Check for null
		this.contentType = contentType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}


	/**
	 * Gets the audioOffset.
	 * 
	 * @return the audioOffset
	 */
	public AudioOffset getAudioOffset() {
		return audioOffset;
	}

	/**
	 * Sets the audioOffset.
	 * 
	 * @param audioOffset
	 *            the new audioOffset
	 */
	public void setAudioOffset(AudioOffset audioOffset) {
                //TODO: check null or empty
		this.audioOffset = audioOffset;
	}	
	
	/**
	 * Gets the speaker.
	 * 
	 * @return the speaker
	 */
	public String getSpeaker() {
		return speaker;
	}

	/**
	 * Sets the speaker.
	 * 
	 * @param speaker
	 *            the new speaker
	 */
	public void setSpeaker(String speaker) {
                //TODO: check null or empty
		this.speaker = speaker;
	}

}

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
 * The Class PartOfSpeech.
 */
public class PartOfSpeech extends Chunk {

	/** The pos tag. */
	private IType posTag;

	/** The sequence id. */
	private final long sequenceId;

	/**
	 * Instantiates a new part of speech.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public PartOfSpeech(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return the pos tag
	 */
	public IType getPosTag() {
		return posTag;
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(IType posTag) {
  
                checkArgument(posTag!=null);
		this.posTag = posTag;
	}

	/**
	 * Gets the part of speech tag.
	 * 
	 * @return the part of speech tag
	 */
	public IType getPartOfSpeechTag() {
		return posTag;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

}

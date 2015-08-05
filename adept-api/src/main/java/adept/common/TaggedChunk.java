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
 * The Class TaggedChunk.
 */
public class TaggedChunk extends Chunk {

	/** The tag. */
	private final String tag;

	/** The sequence id. */
	private final long sequenceId;

	
	/**
	 * Instantiates a new tagged chunk.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tag
	 *            the chunk's tag
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public TaggedChunk(long sequenceId, String tag, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
                checkArgument(tag!=null && tag.trim().length()>0);
		this.tag = tag;
	}

	/**
	 * Gets the chunk's tag.
	 * 
	 * @return the chunk's tag
	 */
	public String getTaggedChunkTag() {
		return tag;
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

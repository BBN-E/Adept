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
 * The Class Translation.
 */
public class Translation {

	/** The source chunk. */
	private final Chunk sourceChunk;

	/** The target chunk. */
	private final Chunk targetChunk;

	/**
	 * Instantiates a new translation.
	 * 
	 * @param sourceChunk
	 *            the source chunk
	 * @param targetChunk
	 *            the target chunk
	 */
	public Translation(Chunk sourceChunk, Chunk targetChunk) {
		super();
		this.sourceChunk = sourceChunk;
		this.targetChunk = targetChunk;
	}

	/**
	 * Gets the source chunk.
	 * 
	 * @return the source chunk
	 */
	public Chunk getSourceChunk() {
		return sourceChunk;
	}

	/**
	 * Gets the target chunk.
	 * 
	 * @return the target chunk
	 */
	public Chunk getTargetChunk() {
		return targetChunk;
	}
}

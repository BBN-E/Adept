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
 * The Class SyntacticChunk.
 */
public class SyntacticChunk extends Chunk {

	/** The sc type. */
	private final IType scType;

	/** The sequence id. */
	private final long sequenceId;

	
	/**
	 * Instantiates a new syntactic chunk.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param scType
	 *            the sc type
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public SyntacticChunk(long sequenceId, Type scType, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
		this.scType = scType;
	}

	/**
	 * Gets the syntactic chunk type.
	 * 
	 * @return the syntactic chunk type
	 */
	public IType getSyntacticChunkType() {
		return scType;
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

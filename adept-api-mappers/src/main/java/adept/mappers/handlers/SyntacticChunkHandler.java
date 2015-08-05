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
 * The Class SyntacticChunk.
 */
public class SyntacticChunkHandler extends ChunkHandler implements SyntacticChunkService.Iface {

	private SyntacticChunk mySyntacticChunk;

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
	SyntacticChunkHandler(long sequenceId, Type scType, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		mySyntacticChunk = new SyntacticChunk();
		// TODO Auto-generated constructor stub
		mySyntacticChunk.sequenceId = sequenceId;
		mySyntacticChunk.scType = scType;
		mySyntacticChunk.id = myItem.id;
		mySyntacticChunk.value = myItem.value;
		mySyntacticChunk.tokenOffset = myChunk.tokenOffset;
		mySyntacticChunk.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the syntactic chunk type.
	 * 
	 * @return mySyntacticChunk.the syntactic chunk type
	 */
	public Type getSyntacticChunkType() {
		return mySyntacticChunk.scType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return mySyntacticChunk.the sequence id
	 */
	public long getSequenceId() {
		return mySyntacticChunk.sequenceId;
	}
	public SyntacticChunk getSyntacticChunk() {
		return mySyntacticChunk;
	}

}

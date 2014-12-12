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
 * The Class Passage.
 */
public class PassageHandler extends ChunkHandler implements PassageService.Iface {

	private Passage myPassage;

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
	public PassageHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		myPassage = new Passage();
		// TODO Auto-generated constructor stub
		myPassage.sequenceId = sequenceId;
		myPassage.id = myItem.id;
		myPassage.value = myItem.value;
		myPassage.tokenOffset = myChunk.tokenOffset;
		myPassage.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return myPassage.the content type
	 */
	public String getContentType() {
		return myPassage.contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
		myPassage.contentType = contentType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myPassage.the sequence id
	 */
	public long getSequenceId() {
		return myPassage.sequenceId;
	}

	public Passage getPassage() {
		return myPassage;
	}

}

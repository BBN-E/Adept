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
 * The Class PartOfSpeech.
 */
public class PartOfSpeechHandler extends ChunkHandler implements PartOfSpeechService.Iface {

	private PartOfSpeech myPartOfSpeech;

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
	public PartOfSpeechHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		myPartOfSpeech = new PartOfSpeech();
		// TODO Auto-generated constructor stub
		myPartOfSpeech.sequenceId = sequenceId;
		myPartOfSpeech.id = myItem.id;
		myPartOfSpeech.value = myItem.value;
		myPartOfSpeech.tokenOffset = myChunk.tokenOffset;
		myPartOfSpeech.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return myPartOfSpeech.the pos tag
	 */
	public Type getPosTag() {
		return myPartOfSpeech.posTag;
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(Type posTag) {
		myPartOfSpeech.posTag = posTag;
	}

	/**
	 * Gets the part of speech tag.
	 * 
	 * @return myPartOfSpeech.the part of speech tag
	 */
	public Type getPartOfSpeechTag() {
		return myPartOfSpeech.posTag;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myPartOfSpeech.the sequence id
	 */
	public long getSequenceId() {
		return myPartOfSpeech.sequenceId;
	}

	public PartOfSpeech getPartOfSpeech() {
		return myPartOfSpeech;
	}

}

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
 * The Class Translation.
 */
public class TranslationHandler implements TranslationService.Iface {

	private Translation myTranslation;

	/**
	 * Instantiates a new translation.
	 * 
	 * @param sourceChunk
	 *            the source chunk
	 * @param targetChunk
	 *            the target chunk
	 */
	public TranslationHandler(ChunkUnion sourceChunk, ChunkUnion targetChunk) {
		super();
		myTranslation = new Translation();
		myTranslation.sourceChunk = sourceChunk;
		myTranslation.targetChunk = targetChunk;
	}

	/**
	 * Gets the source chunk.
	 * 
	 * @return myTranslation.the source chunk
	 */
	public ChunkUnion getSourceChunk() {
		return myTranslation.sourceChunk;
	}

	/**
	 * Gets the target chunk.
	 * 
	 * @return myTranslation.the target chunk
	 */
	public ChunkUnion getTargetChunk() {
		return myTranslation.targetChunk;
	}
	public Translation getTranslation() {
		return myTranslation;
	}

}

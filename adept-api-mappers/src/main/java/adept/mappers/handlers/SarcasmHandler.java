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
 * The Class Sarcasm.
 */
public class SarcasmHandler extends ChunkHandler implements SarcasmService.Iface {

	private Sarcasm mySarcasm;

	/**
	 * The Enum Judgment.
	 */
	public enum Judgment {

		/** The positive. */
		POSITIVE,
		/** The negative. */
		NEGATIVE,
		/** The none. */
		NONE,
	}

	/**
	 * Instantiates a new sarcasm.
	 * 
	 * @param sarcasmId
	 *            the sarcasm id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param judgment
	 *            the judgment
	 */
	public SarcasmHandler(long sarcasmId, TokenOffset tokenOffset,
			TokenStream tokenStream, SarcasmJudgment judgment) {
		super(tokenOffset, tokenStream);
		mySarcasm = new Sarcasm();
		mySarcasm.sarcasmId = sarcasmId;
		mySarcasm.judgment = judgment;
		mySarcasm.id = myItem.id;
		mySarcasm.value = myItem.value;
		mySarcasm.tokenOffset = myChunk.tokenOffset;
		mySarcasm.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sarcasm id.
	 * 
	 * @return mySarcasm.the sarcasm id
	 */
	public long getSarcasmId() {
		return mySarcasm.sarcasmId;
	}

	/**
	 * Gets the judgment.
	 * 
	 * @return mySarcasm.the judgment
	 */
	public SarcasmJudgment getJudgment() {
		return mySarcasm.judgment;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return mySarcasm.the confidence
	 */
	public double getConfidence() {
		return mySarcasm.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		mySarcasm.confidence = confidence;
	}

	public Sarcasm getSarcasm() {
		return mySarcasm;
	}

}

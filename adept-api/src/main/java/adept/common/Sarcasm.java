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
 * The Class Sarcasm.
 */
public class Sarcasm extends Chunk {

	/** The sarcasm id. */
	private final long sarcasmId;

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

	/** The judgment. */
	private final Judgment judgment;

	/** The confidence. */
	private float confidence;

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
	public Sarcasm(long sarcasmId, TokenOffset tokenOffset,
			TokenStream tokenStream, Judgment judgment) {
		super(tokenOffset, tokenStream);
		this.sarcasmId = sarcasmId;
		this.judgment = judgment;
	}

	/**
	 * Gets the sarcasm id.
	 * 
	 * @return the sarcasm id
	 */
	public long getSarcasmId() {
		return sarcasmId;
	}

	/**
	 * Gets the judgment.
	 * 
	 * @return the judgment
	 */
	public Judgment getJudgment() {
		return judgment;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

}

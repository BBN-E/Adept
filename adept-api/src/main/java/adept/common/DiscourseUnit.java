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
 * The Class DiscourseUnit.
 */
public class DiscourseUnit extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The discource type. */
	private String discourceType;

	/** The uncertainty confidence. */
	private float uncertaintyConfidence;

	/** The novelty confidence. */
	private float noveltyConfidence;

	/**
	 * Instantiates a new discourse unit.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 * @param discourceType
	 *            the discource type
	 */
	public DiscourseUnit(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String discourceType) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
		this.discourceType = discourceType;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return the uncertainty confidence
	 */
	public float getUncertaintyConfidence() {
		return uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(float uncertaintyConfidence) {
		this.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return the novelty confidence
	 */
	public float getNoveltyConfidence() {
		return noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(float noveltyConfidence) {
		this.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the discource type.
	 * 
	 * @return the discource type
	 */
	public String getDiscourceType() {
		return discourceType;
	}

	/**
	 * Sets the discource type.
	 * 
	 * @param discourceType
	 *            the new discource type
	 */
	public void setDiscourceType(String discourceType) {
		this.discourceType = discourceType;
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

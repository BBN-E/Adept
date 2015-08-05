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
 * The Class DiscourseUnit.
 */
public class DiscourseUnitHandler extends ChunkHandler implements DiscourseUnitService.Iface {

	private DiscourseUnit myDiscourseUnit;

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
	public DiscourseUnitHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String discourceType) {
		super(tokenOffset, tokenStream);
		myDiscourseUnit = new DiscourseUnit();
		myDiscourseUnit.sequenceId = sequenceId;
		myDiscourseUnit.discourceType = discourceType;
		myDiscourseUnit.id = myItem.id;
		myDiscourseUnit.value = myItem.value;
		myDiscourseUnit.tokenOffset = myChunk.tokenOffset;
		myDiscourseUnit.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return myDiscourseUnit.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return myDiscourseUnit.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		myDiscourseUnit.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return myDiscourseUnit.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return myDiscourseUnit.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		myDiscourseUnit.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the discource type.
	 * 
	 * @return myDiscourseUnit.the discource type
	 */
	public String getDiscourceType() {
		return myDiscourseUnit.discourceType;
	}

	/**
	 * Sets the discource type.
	 * 
	 * @param discourceType
	 *            the new discource type
	 */
	public void setDiscourceType(String discourceType) {
		myDiscourseUnit.discourceType = discourceType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myDiscourseUnit.the sequence id
	 */
	public long getSequenceId() {
		return myDiscourseUnit.sequenceId;
	}

	public DiscourseUnit getDiscourseUnit() {
		return myDiscourseUnit;
	}

}

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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;


/**
 * The Class InterPausalUnit.
 */
public class InterPausalUnit extends HltContent {

	/** The sequence id. */
	private final long sequenceId;

	/** The ipu audio offset. */
	private final AudioOffset ipuAudioOffset;

	/** The acoustic features. */
	Map<String, Float> acousticFeatures;

	/**
	 * Instantiates a new inter pausal unit.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param ipuAudioOffset
	 *            the ipu audio offset
	 */
	public InterPausalUnit(long sequenceId, AudioOffset ipuAudioOffset) {
		super();
		this.ipuAudioOffset = ipuAudioOffset;
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the acoustic features.
	 * 
	 * @return the acoustic features
	 */
	public Map<String, Float> getAcousticFeatures() {
		return acousticFeatures;
	}

	/**
	 * Sets the acoustic features.
	 * 
	 * @param acousticFeatures
	 *            the acoustic features
	 */
	public void setAcousticFeatures(Map<String, Float> acousticFeatures) {
                // TODO: Check for null?
		this.acousticFeatures = acousticFeatures;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the ipu audio offset.
	 * 
	 * @return the ipu audio offset
	 */
	public AudioOffset getIpuAudioOffset() {
		return ipuAudioOffset;
	}

}

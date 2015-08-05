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

import java.util.Map;


/**
 * The Class InterPausalUnit.
 */
public class InterPausalUnitHandler extends HltContentHandler implements InterPausalUnitService.Iface {

	private InterPausalUnit myInterPausalUnit;

	/**
	 * Instantiates a new inter pausal unit.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param ipuAudioOffset
	 *            the ipu audio offset
	 */
	public InterPausalUnitHandler(long sequenceId, AudioOffset ipuAudioOffset) {
		super();
		myInterPausalUnit = new InterPausalUnit();
		myInterPausalUnit.ipuAudioOffset = ipuAudioOffset;
		myInterPausalUnit.sequenceId = sequenceId;
		myInterPausalUnit.id = myItem.id;
		myInterPausalUnit.value = myItem.value;
	}

	/**
	 * Gets the acoustic features.
	 * 
	 * @return myInterPausalUnit.the acoustic features
	 */
	public Map<String, Double> getAcousticFeatures() {
		return myInterPausalUnit.acousticFeatures;
	}

	/**
	 * Sets the acoustic features.
	 * 
	 * @param acousticFeatures
	 *            the acoustic features
	 */
	public void setAcousticFeatures(Map<String, Double> acousticFeatures) {
		myInterPausalUnit.acousticFeatures = acousticFeatures;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myInterPausalUnit.the sequence id
	 */
	public long getSequenceId() {
		return myInterPausalUnit.sequenceId;
	}

	/**
	 * Gets the ipu audio offset.
	 * 
	 * @return myInterPausalUnit.the ipu audio offset
	 */
	public AudioOffset getIpuAudioOffset() {
		return myInterPausalUnit.ipuAudioOffset;
	}

	public InterPausalUnit getInterPausalUnit() {
		return myInterPausalUnit;
	}

}

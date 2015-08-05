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
 * The Class AudioOffset.
 */
public final class AudioOffsetHandler implements AudioOffsetService.Iface {

	private AudioOffset myAudioOffset;

	/**
	 * Instantiates a new audio offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public AudioOffsetHandler(double beginIndex, double endIndex) {
		myAudioOffset = new AudioOffset();
		myAudioOffset.beginIndex = beginIndex;
		myAudioOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myAudioOffset.the begin
	 */
	public double getBegin() {
		return myAudioOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myAudioOffset.the end
	 */
	public double getEnd() {
		return myAudioOffset.endIndex;
	}

	public AudioOffset getAudioOffset() {
		return myAudioOffset;
	}

}

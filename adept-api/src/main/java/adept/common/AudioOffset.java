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
 * The Class AudioOffset.
 */
public final class AudioOffset {

	/** The begin. */
	private final float begin;

	/** The end. */
	private final float end;

	/**
	 * Instantiates a new audio offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public AudioOffset(float begin, float end) {
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return the begin
	 */
	public float getBegin() {
		return begin;
	}

	/**
	 * Gets the end.
	 * 
	 * @return the end
	 */
	public float getEnd() {
		return end;
	}

}

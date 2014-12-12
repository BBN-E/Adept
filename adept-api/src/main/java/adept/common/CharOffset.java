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
 * Offset class captures begin and end integer positions of character or token
 * spans. This class is immutable..
 */
public final class CharOffset {

	/** The begin. */
	private final int begin;

	/** The end. */
	private final int end;

	/**
	 * Instantiates a new offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public CharOffset(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * Gets the end.
	 * 
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

}

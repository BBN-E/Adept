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

// TODO: Auto-generated Javadoc
/**
 * Offset class captures begin and end integer positions of character or token
 * spans. This class is immutable..
 */
public final class CharOffsetHandler implements CharOffsetService.Iface {

	private CharOffset myCharOffset;

	/**
	 * Instantiates a new offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public CharOffsetHandler(int beginIndex, int endIndex) {
		myCharOffset = new CharOffset();
		myCharOffset.beginIndex = beginIndex;
		myCharOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myCharOffset.the begin
	 */
	public int getBegin() {
		return myCharOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myCharOffset.the end
	 */
	public int getEnd() {
		return myCharOffset.endIndex;
	}

	public CharOffset getCharOffset() {
		return myCharOffset;
	}

}

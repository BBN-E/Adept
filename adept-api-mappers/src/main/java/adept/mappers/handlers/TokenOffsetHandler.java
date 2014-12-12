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
public final class TokenOffsetHandler implements TokenOffsetService.Iface {

	private TokenOffset myTokenOffset;

	/**
	 * Instantiates a new offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public TokenOffsetHandler(long beginIndex, long endIndex) {
		myTokenOffset = new TokenOffset();
		myTokenOffset.beginIndex = beginIndex;
		myTokenOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myTokenOffset.the begin
	 */
	public long getBegin() {
		return myTokenOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myTokenOffset.the end
	 */
	public long getEnd() {
		return myTokenOffset.endIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(TokenOffsetObject obj) {
		if (obj == null || !(obj instanceof TokenOffsetObject))
			return false;
		TokenOffsetObject tokenOffset = (TokenOffsetObject) obj;
		return (tokenOffset.getTokenOffset().beginIndex == myTokenOffset.beginIndex && tokenOffset
				.getTokenOffset().endIndex == myTokenOffset.endIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d", myTokenOffset.beginIndex, myTokenOffset.endIndex);
		return code.hashCode();
	}

	public TokenOffset getTokenOffset() {
		return myTokenOffset;
	}

}

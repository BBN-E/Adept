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
 * The TokenOffset class captures begin and end 
 * integer positions of character or token spans. 
 * This class is immutable. 
 */
public final class TokenOffset {

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
	public TokenOffset(int begin, int end) {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof TokenOffset))
			return false;
		TokenOffset tokenOffset = (TokenOffset) obj;
		return (tokenOffset.getBegin() == this.getBegin() && tokenOffset
				.getEnd() == this.getEnd());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d", this.getBegin(), this.getEnd());
		return code.hashCode();
	}

}

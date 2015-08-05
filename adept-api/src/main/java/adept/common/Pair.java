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


/**
 * A generic, simple pair class for storing key-value or pair data. This class
 * is immutable.
 * 
 * @param <L>
 *            the generic type
 * @param <R>
 *            the generic type
 */
public final class Pair<L, R> {

	/** The l. */
	private final L l;

	/** The r. */
	private final R r;

	/**
	 * Instantiates a new pair.
	 * 
	 * @param l
	 *            the l
	 * @param r
	 *            the r
	 */
	public Pair(L l, R r) {
                checkArgument(l!=null);
                checkArgument(r!=null);
		this.l = l;
		this.r = r;
	}

	/**
	 * Gets the l.
	 * 
	 * @return the l
	 */
	public L getL() {
		return l;
	}

	/**
	 * Gets the r.
	 * 
	 * @return the r
	 */
	public R getR() {
		return r;
	}

}

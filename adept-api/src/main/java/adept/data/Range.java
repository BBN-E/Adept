/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.data;


/**
 * The Class Range.
 * 
 * @param <T>
 *            the generic type
 */
public final class Range<T extends Comparable<T>> {

	/** The begin. */
	private final T begin;

	/** The end. */
	private final T end;

	/**
	 * Instantiates a new range.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public Range(final T begin, final T end) {
		assert (begin != null && end != null && begin.compareTo(end) > 0);
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Contains.
	 * 
	 * @param object
	 *            the object
	 * @return true, if successful
	 */
	public final boolean contains(final T object) {

		if (this.begin != null && object.compareTo(this.begin) < 0)
			return false;

		if (this.end != null && object.compareTo(this.end) > 0)
			return false;

		return true;
	}

	/**
	 * Contains.
	 * 
	 * @param range
	 *            the range
	 * @return true, if successful
	 */
	public final boolean contains(final Range<T> range) {

		if (this.begin != null
				&& (range.begin == null || range.begin.compareTo(this.begin) < 0))
			return false;

		if (this.end != null
				&& (range.end == null || range.end.compareTo(this.end) > 0))
			return false;

		return true;
	}

	/**
	 * Overlaps.
	 * 
	 * @param range
	 *            the range
	 * @return true, if successful
	 */
	public final boolean overlaps(final Range<T> range) {

		if (this.end != null && range.begin != null
				&& this.end.compareTo(range.begin) < 0)
			return false;

		if (this.begin != null && range.end != null
				&& this.begin.compareTo(range.end) > 0)
			return false;

		return true;
	}

}

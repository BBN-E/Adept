/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

package adept.data;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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
/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The TokenOffset class captures begin and end 
 * integer positions of character or token spans. 
 * Both begin and end offsets are inclusive.
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
	 *            the begin, inclusive.
	 * @param end
	 *            the end, inclusive.
	 */
	public TokenOffset(int begin, int end) {
            checkArgument((end >= begin),
                    "A TokenOffset's end must be no less than its begin, "
                    +"but got begin %s and end %s",
                    begin, end);
		this.begin = begin;
		this.end = end;
	}

	/**
	 * Gets the begin (inclusive).
	 * 
	 * @return the begin
	 */
	public int getBegin() {
		return begin;
	}

	/**
	 * Gets the end (inclusive).
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
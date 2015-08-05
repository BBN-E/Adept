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
            checkArgument((end >= begin),
                    "A CharOffset's end must be no less than its begin, "
                    +"but got begin %s and end %s",
                    begin, end);
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
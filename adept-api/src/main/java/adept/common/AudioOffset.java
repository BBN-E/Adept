/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

/*
 * 
 */
package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * Captures the begin and end time of an audio span.
 * This class is immutable.
 */
public final class AudioOffset {

	/** The begin time. */
	private final float begin;

	/** The end time. */
	private final float end;

	/**
	 * @param begin
	 *            The time that represents the beginning of the audio span.
	 * @param end
	 *            The time that represents the end of the audio span.
	 */
	public AudioOffset(float begin, float end) {
		checkArgument(begin < end, "An AudioOffset's begin must be less than its end, but got begin %s and end %s", begin, end);
		this.begin = begin;
		this.end = end;
	}

	/**
	 * @return The begin time.
	 */
	public float getBegin() {
		return begin;
	}

	/**
	 * @return The end time.
	 */
	public float getEnd() {
		return end;
	}
}

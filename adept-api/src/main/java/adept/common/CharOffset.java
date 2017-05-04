package adept.common;

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

import java.io.Serializable;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * Captures the begin and end integer positions of character spans.
 * The begin position is inclusive, while the end is exclusive.
 * This class is immutable.
 */
public final class CharOffset implements Serializable {

	private static final long serialVersionUID = -9082985333126690567L;

	/** The begin index, which is inclusive. */
	private final int begin;

	/** The end index, which is exclusive. */
	private final int end;

	/**
	 * @param begin
	 *            The (inclusive) index that points to the beginning of the character span.
	 * @param end
	 *            The (exclusive) index that points to the end of the character span.
	 */
	public CharOffset(int begin, int end) {
            checkArgument(begin < end, "A CharOffset's begin must be less than its end, but got begin %s and end %s", begin, end);
            this.begin = begin;
            this.end = end;
	}

	/**
	 * @return The (inclusive) index that points to the beginning of the character span.
	 */
	public int getBegin() {
            return begin;
	}

	/**
	 * @return The (exclusive) index that points to the end of the character span.
	 */
	public int getEnd() {
            return end;
	}
	
	@Override
	public boolean equals(Object o) {
		if (null == o || !(o instanceof CharOffset)) {
			return false;
		}
		CharOffset that = (CharOffset)o;
		return (this.begin == that.begin && this.end == that.end);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(begin, end);
	}
}

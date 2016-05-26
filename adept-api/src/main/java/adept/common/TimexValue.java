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

package adept.common;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a TIMEX temporal value.  This is a placeholder for future elaboration.
 *
 * This class is immutable.
 */
public final class TimexValue extends Item implements TemporalValue {
    private final String timexString;

    private TimexValue(String timexString) {
        this.timexString = checkNotNull(timexString);
        checkArgument(!timexString.isEmpty(), "Timex strings may not be empty");
    }

    @Override
    public String asString() {
        return timexString;
    }

    /**
     * @param timexString Must be a valid TIMEX string. Otherwise, behavior is undefined.
     */
    public static TimexValue fromString(String timexString) {
        return new TimexValue(timexString);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timexString);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final TimexValue other = (TimexValue) obj;
        return Objects.equal(this.timexString, other.timexString);
    }
}

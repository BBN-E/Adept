package adept.common;

import com.google.common.base.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a TIMEX temporal value.  This is a placeholder for future elaboration.
 *
 * This class is immutable.
 */
public final class TimexValue implements TemporalValue {
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

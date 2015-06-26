package adept.common;

/**
 * Represents a temporal value, abstracted from its particular realization in text.
 * This is placeholder which may be elaborated in the future.  Implementations are strongly
 * urged to be immutable.  Implementations are only required to implement {@link #hashCode()}
 * and {@link #equals(Object)} in terms of the structure of that sort of temporal value and are
 * not required to attempt to determine which descriptions refer to equivalent real-world values.
 */
public interface TemporalValue {
    /**
     * @return May never be null.
     */
    public String asString();
}

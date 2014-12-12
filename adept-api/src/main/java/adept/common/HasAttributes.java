package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry boolean flags of type {@link IType}.
 */
public interface HasAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, Float> getAttributes();
}

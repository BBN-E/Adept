package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry float-valued flags of type {@link IType}.
 */
public interface HasScoredUnaryAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, Float> getScoredUnaryAttributes();
    
}

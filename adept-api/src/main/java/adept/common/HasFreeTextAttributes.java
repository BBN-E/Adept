package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type String.
 */
public interface HasFreeTextAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, String> getFreeTextAttributes();
    
    public void setFreeTextAttributes(Map<IType, String> freeTextAttributes);
}

package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type {@link IType}.
 */
public interface HasOntologizedAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, IType> getOntologizedAttributes();
    
    public void setOntologizedAttributes(Map<IType, IType> ontologizedAttributes);
}

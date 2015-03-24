package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type Chunk.
 */
public interface HasChunkAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, Chunk> getChunkAttributes();
    
    public void setChunkAttributes(Map<IType, Chunk> chunkAttributes);
}
package adept.common;

import java.util.Map;
import java.util.List;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type List<Entity>.
 */
public interface IConversationElementEntityRelation {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, List<Entity>> getConversationElementEntityRelations();
    
    public void setConversationElementEntityRelations(Map<IType, List<Entity>> conversationElementEntityRelations);
}

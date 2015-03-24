package adept.common;

import java.util.Map;
import java.util.List;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type List<ConversationElement>.
 */
public interface IConversationElementRelation {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, List<ConversationElement>> getConversationElementRelations();
    
    public void setConversationElementRelations(Map<IType, List<ConversationElement>> conversationElementRelations);
}

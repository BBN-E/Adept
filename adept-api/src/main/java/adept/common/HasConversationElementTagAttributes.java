package adept.common;

import java.util.Map;

/**
 * Indicates a class can carry flags of type {@link IType} mapping to values of type ConversationElementTag.
 */
public interface HasConversationElementTagAttributes {
    /**
     * @return May be empty, but will never contain {@code null}.
     */
    public Map<IType, ConversationElementTag> getConversationElementTagAttributes();
    
    public void setConversationElementTagAttributes(Map<IType, ConversationElementTag> tagAttributes);
}
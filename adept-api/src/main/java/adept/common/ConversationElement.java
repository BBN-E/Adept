/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */
package adept.common;

import java.util.*;
import adept.utilities.*;

/**
 * The Class ConversationElement, which represents the
 * units of Conversation. For example, a single message
 * in an email thread, or a single post in a discussion 
 * forum thread.
 */

public class ConversationElement implements HasOntologizedAttributes, HasFreeTextAttributes, HasConversationElementTagAttributes, IConversationElementRelation, IConversationElementEntityRelation
{
	private Chunk messageChunk;
	
	private String authorId;
	
	private String authoredTime;
	
	/** A set of ontology based attribute-value pairs */
	private Map<IType, IType> ontologizedAttributes = new HashMap<IType, IType>();
	
	/** A set of ontology based attribute, and free text value pairs */
	private Map<IType, String> freeTextAttributes = new HashMap<IType, String>(); 
	
	/** A set of ontology based attribute, and ConversationElementTag pairs */
	private Map<IType, ConversationElementTag> conversationElementTagAttributes = new HashMap<IType, ConversationElementTag>();
	
	/** A set of ontology based attribute, and list of ConversationElement pairs */
	private Map<IType, List<ConversationElement>> conversationElementRelations = new HashMap<IType, List<ConversationElement>>();
	
	/** A set of ontology based attribute, and list of entity pairs */
	private Map<IType, List<Entity>> conversationElementEntityRelations = new HashMap<IType, List<Entity>>();
	
	
	public ConversationElement(TokenOffset tokenOffset, TokenStream tokenStream)
	{
		this. messageChunk = new Chunk(tokenOffset, tokenStream);
	}
	
	
	/*
	 * Gets message chunk.
	 * 
	 * @return message Chunk.
	 */
	public Chunk getMessageChunk()
	{
		return messageChunk;
	}
	
	
	/*
	 * Gets author Id.
	 * 
	 * @return author Id.
	 */
	public String getAuthorId()
	{
		return authorId;
	}
	
	/*
	 * Sets author Id.
	 */
	public void setAuthorId(String authorId)
	{
		this.authorId = authorId;
	}
	
	/*
	 * Gets authored time.
	 * 
	 * @return authored time.
	 */
	public String getAuthoredTime()
	{
		return authoredTime;
	}
	
	/*
	 * Sets authored time.
	 */
	public void setAuthoredTime(String authoredTime)
	{
		this.authoredTime = authoredTime;
	}
	
	/*
	 * Get ontologized attributes
	 * 
	 * @return ontologized attributes map
	 */
	public Map<IType,IType> getOntologizedAttributes()
	{
		return ontologizedAttributes;
	}
	
	/*
	 * Sets ontologized attributes
	 */
	public void setOntologizedAttributes(Map<IType,IType> ontologizedAttributes)
	{
		this.ontologizedAttributes = ontologizedAttributes;
	}
	
	/*
	 * Gets free text attributes
	 * 
	 * @return free text attributes map
	 */
	public Map<IType,String> getFreeTextAttributes()
	{
		return freeTextAttributes;
	}
	
	/*
	 * Sets free text attributes
	 */
	public void setFreeTextAttributes(Map<IType,String> freetextAttributes)
	{
		this.freeTextAttributes = freeTextAttributes;
	}
	
	/*
	 * Gets open and close conversation element tag attributes
	 * 
	 * @return conversation element tag attributes map
	 */
	public Map<IType, ConversationElementTag> getConversationElementTagAttributes()
	{
		return conversationElementTagAttributes;
	}
	
	/*
	 * Sets conversation element tag attributes
	 */
	public void setConversationElementTagAttributes(Map<IType,ConversationElementTag> conversationElementTagAttributes)
	{
		this.conversationElementTagAttributes = conversationElementTagAttributes;
	}
	
	/*
	 * Gets conversation element relations
	 * 
	 * @return conversation element relations map
	 */
	public Map<IType, List<ConversationElement>> getConversationElementRelations()
	{
		return conversationElementRelations;
	}
	
	/*
	 * Sets conversation element relations
	 */
	public void setConversationElementRelations(Map<IType,List<ConversationElement>> conversationElementRelations)
	{
		this.conversationElementRelations = conversationElementRelations;
	}
	
	/*
	 * Gets conversation element to entity relations
	 * 
	 * @return conversation element to entity relations map
	 */
	public Map<IType, List<Entity>> getConversationElementEntityRelations()
	{
		return conversationElementEntityRelations;
	}
	
	/*
	 * Sets conversation element to entity relations
	 */
	public void setConversationElementEntityRelations(Map<IType,List<Entity>> conversationElementEntityRelations)
	{
		this.conversationElementEntityRelations = conversationElementEntityRelations;
	}
}

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class EntityMention, which is a sequence of tokens 
 * that refer to an Entity with mention type that is drawn
 * from an ontology
 */
public class EntityMention extends Chunk {

	/** The mention type. */
	private IType mentionType;

	/** The entity type. */
	private IType entityType;

	/** The sequence id. */
	private final long sequenceId;

	/** The entity id distribution. */
	private Map<Long, Float> entityIdDistribution;

	/** The textual context. */
	protected Chunk context;

	/** The size of the context. */
	protected int contextSize = 2;

	/** The doc id. */
	private String docId;

	/** The tokenizer type. */
	private TokenizerType tokenizerType;
	
	/** Additional attributes that don't come from a fixed Ontology */
	private Map< String, String > attributes;
	
	/**
	 * Instantiates a new entity mention.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public EntityMention(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
        this.docId = tokenStream.getDocument().getDocId();
        this.tokenizerType = tokenStream.getTokenizerType();
        this.entityIdDistribution = new HashMap<Long, Float>();
        this.attributes = new HashMap< String, String >();
        
		setContext();
	}
	
	/**
	 * Sets the context.
	 */
	protected void setContext()
	{
		int begin = Math.max(0,  getTokenOffset().getBegin()-contextSize); 
		int end = Math.min(tokenStream.size()-1,getTokenOffset().getEnd() + contextSize);
		TokenOffset tokenOffset = new TokenOffset(begin,end);
		context = new Chunk(tokenOffset, tokenStream);
	}
	
	/**
	 * Gets the entity id distribution.
	 * 
	 * @return the entity id distribution
	 */
	public Map<Long, Float> getEntityIdDistribution() {
		return entityIdDistribution;
	}

	/**
	 * Sets the entity id distribution.
	 * 
	 * @param entityIdDistribution
	 *            the entity id distribution
	 */
	public void setEntityIdDistribution(Map<Long, Float> entityIdDistribution) {
		this.entityIdDistribution = entityIdDistribution;
	}

	/**
	 * Adds the entity confidence pair.
	 * 
	 * @param entityId
	 *            the entity id
	 * @param confidence
	 *            the confidence
	 */
	public void addEntityConfidencePair(long entityId, float confidence) {
		this.entityIdDistribution.put(entityId, new Float(confidence));
	}

	/**
	 * Gets the confidence.
	 * 
	 * @param entityId
	 *            the entity id
	 * @return the confidence
	 */
	public float getConfidence(long entityId) {
		return this.entityIdDistribution.get(entityId);
	}

	/**
	 * Gets the mention type.
	 * 
	 * @return the mention type
	 */
	public IType getMentionType() {
		return mentionType;
	}

	/**
	 * Sets the mention type.
	 * 
	 * @param mentionType
	 *            the new mention type
	 */
	public void setMentionType(IType mentionType) {
		this.mentionType = mentionType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the entity type.
	 * 
	 * @return the entity type
	 */
	public IType getEntityType() {
		return entityType;
	}

	/**
	 * Sets the entity type.
	 * 
	 * @param entityType
	 *            the new entity type
	 */
	public void setEntityType(IType entityType) {
		this.entityType = entityType;
	}

	/**
	 * Gets the best entity id.
	 *
	 * @return the best entity id. Returns -1 if empty.
	 */
	public long getBestEntityId() {
		long entityId = -1;
		if (entityIdDistribution.size() == 1) {
			entityId = entityIdDistribution.entrySet().iterator().next().getKey();
		} else {
			float score = -1000f;
			for(Map.Entry<Long, Float> entrySet : entityIdDistribution.entrySet()) {
				if (entrySet.getValue() > score) {
					entityId = entrySet.getKey();
					score = entrySet.getValue();
				}
			}			
		}
		return entityId;
	}	
	
	/**
	 * Sets the doc id.
	 * 
	 * @param docId
	 *            the new doc id
	 */
	public void setDocId(String docId) {
		this.docId = docId;
	}

	/**
	 * Sets the doc id.
	 *
	 * @param tokenizerType the new tokenizer type
	 */
	public void setTokenizerType(TokenizerType tokenizerType) {
		this.tokenizerType = tokenizerType;
	}

	/**
	 * Gets the doc id.
	 * 
	 * @return the doc id
	 */
	public String getDocId() {
		return docId;
	}


	/**
	 * Gets the tokenizer type.
	 * 
	 * @return the tokenizer type
	 */
	public TokenizerType getTokenizerType() {
		return tokenizerType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Chunk#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof EntityMention))
			return false;
		EntityMention em = (EntityMention) obj;

		return (super.equals(obj) && em.getEntityType() == this.entityType && em
				.getMentionType() == this.mentionType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Chunk#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d_%s_%s_%s",
				this.tokenOffset.getBegin(), this.tokenOffset.getEnd(),
				this.value, this.entityType, this.mentionType);
		return code.hashCode();
	}

	/**
	 * get mention attributes
	 * 
	 * @return
	 */
	public Map< String, String > getAttributes() {
		return attributes;
	}

	/**
	 * set mention attributes
	 * 
	 * @param attributes
	 */
	
	public void setAttributes(Map< String, String > attributes) {
		this.attributes = attributes;
	}

	/**
	 * set mention attribute
	 * 
	 * @param attribute
	 * @param value
	 */
	
	public void setAttribute(String attribute, String value) 
	{
		this.attributes.put( attribute, value );
	}
	
	/**
	 * get mention attribute
	 * 
	 * @param attribute
	 * @return
	 */
	
	public String getAttribute( String attribute )
	{
		return this.attributes.get(attribute); 
	}

}

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
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.HashMap;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class EntityMention.
 */
public class EntityMentionHandler extends ChunkHandler implements EntityMentionService.Iface {

	private EntityMention myEntityMention;

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
	public EntityMentionHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		myEntityMention = new EntityMention();
		myEntityMention.sequenceId = sequenceId;
		myEntityMention.id = myItem.id;
		myEntityMention.value = myItem.value;
		myEntityMention.tokenOffset = myChunk.tokenOffset;
		myEntityMention.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the entity id distribution.
	 * 
	 * @return myEntityMention.the entity id distribution
	 */
	public Map<Long, Double> getEntityIdDistribution() {
		return myEntityMention.entityIdDistribution;
	}

	/**
	 * Sets the entity id distribution.
	 * 
	 * @param entityIdDistribution
	 *            the entity id distribution
	 */
	public void setEntityIdDistribution(Map<Long, Double> entityIdDistribution) {
		myEntityMention.entityIdDistribution = entityIdDistribution;
	}

	/**
	 * Adds the entity confidence pair.
	 * 
	 * @param entityId
	 *            the entity id
	 * @param confidence
	 *            the confidence
	 */
	public void addEntityConfidencePair(long entityId, double confidence) {
		if (myEntityMention.entityIdDistribution == null) {
			myEntityMention.entityIdDistribution = new HashMap<Long, Double>();
		}
		myEntityMention.entityIdDistribution.put(entityId, new Double(confidence));
	}

	/**
	 * Gets the confidence.
	 * 
	 * @param entityId
	 *            the entity id
	 * @return myEntityMention.the confidence
	 */
	public double getConfidence(long entityId) {
		return myEntityMention.entityIdDistribution.get(entityId);
	}

	/**
	 * Gets the mention type.
	 * 
	 * @return myEntityMention.the mention type
	 */
	public Type getMentionType() {
		return myEntityMention.mentionType;
	}

	/**
	 * Sets the mention type.
	 * 
	 * @param mentionType
	 *            the new mention type
	 */
	public void setMentionType(Type mentionType) {
		myEntityMention.mentionType = mentionType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myEntityMention.the sequence id
	 */
	public long getSequenceId() {
		return myEntityMention.sequenceId;
	}

	/**
	 * Gets the entity type.
	 * 
	 * @return myEntityMention.the entity type
	 */
	public Type getEntityType() {
		return myEntityMention.entityType;
	}

	/**
	 * Sets the entity type.
	 * 
	 * @param entityType
	 *            the new entity type
	 */
	public void setEntityType(Type entityType) {
		myEntityMention.entityType = entityType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Chunk#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(EntityMention obj) {
		if (obj == null || !(obj instanceof EntityMention))
			return false;
		EntityMention em = (EntityMention) obj;

		return (super.equals(obj) && em.entityType == myEntityMention.mentionType && em
				.mentionType == myEntityMention.mentionType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Chunk#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d_%s_%s_%s",
				myEntityMention.tokenOffset.beginIndex, myEntityMention.tokenOffset.endIndex,
				myEntityMention.value, myEntityMention.entityType, myEntityMention.mentionType);
		return code.hashCode();
	}

	public EntityMention getEntityMention() {
		return myEntityMention;
	}

}

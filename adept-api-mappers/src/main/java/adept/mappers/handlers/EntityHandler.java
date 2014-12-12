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

import adept.data.IEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity is represented by a globally unique ID and a canonical
 * mention. The argumentConfidenceMap in ResolvedMention provides a distribution
 * over possible entities for a given Mention
 */
public class EntityHandler extends HltContentHandler implements EntityService.Iface {

	private Entity myEntity;

	/**
	 * Instantiates a new entity.
	 * 
	 * @param entityId
	 *            the entity id
	 * @param entityType
	 *            the entity type
	 */
	public EntityHandler(long entityId, Type entityType) {
		super();
		myEntity = new Entity();
		myEntity.entityId = entityId;
		myEntity.entityType = entityType;
		myEntity.id = myItem.id;
		myEntity.value = myItem.value;
	}

	/**
	 * Gets the entity id.
	 * 
	 * @return myEntity.the entity id
	 */
	public long getEntityId() {
		return myEntity.entityId;
	}

	/**
	 * Gets the canonical mention.
	 * 
	 * @return myEntity.the canonical mention
	 */
	public EntityMention getCanonicalMention() {
		return myEntity.canonicalMention;
	}

	/**
	 * Sets the canonical mentions.
	 * 
	 * @param canonicalMention
	 *            the new canonical mentions
	 */
	public void setCanonicalMentions(EntityMention canonicalMention) {
		myEntity.canonicalMention = canonicalMention;
		myEntity.value = canonicalMention.getValue();
	}

	/**
	 * Gets the entity type.
	 * 
	 * @return myEntity.the entity type
	 */
	public Type getEntityType() {
		return myEntity.entityType;
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.IEntity#getEntity()
	 */
	@Override
	public String getEntity() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(EntityObject obj) {
		if (obj == null || !(obj instanceof EntityObject))
			return false;
		EntityObject entity = (EntityObject) obj;
		return (entity.getEntity().entityId == myEntity.entityId && entity.getEntity().entityType
				.equals(myEntity.entityType));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int id = (int) myEntity.entityId;
		int hash = id * 31;
		return hash;
	}

	public Entity getEntityStruct() {
		return myEntity;
	}

}

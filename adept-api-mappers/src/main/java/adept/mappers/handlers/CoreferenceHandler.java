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

import java.util.List;


/**
 * The Class Coreference.
 */
public class CoreferenceHandler extends HltContentHandler implements CoreferenceService.Iface {

	private Coreference myCoreference;

	/**
	 * Instantiates a new coreference.
	 * 
	 * @param coreferenceId
	 *            the coreference id
	 */
	public CoreferenceHandler(long coreferenceId) {
		myCoreference = new Coreference();
		myCoreference.coreferenceId = coreferenceId;
		myCoreference.id = myItem.id;
		myCoreference.value = myItem.value;
	}

	/**
	 * Gets the coreference id.
	 * 
	 * @return myCoreference.the coreference id
	 */
	public long getCoreferenceId() {
		return myCoreference.coreferenceId;
	}

	/**
	 * Gets the entities.
	 * 
	 * @return myCoreference.the entities
	 */
	public List<Entity> getEntities() {
		return myCoreference.entities;
	}

	/**
	 * Sets the entities.
	 * 
	 * @param entities
	 *            the new entities
	 */
	public void setEntities(List<Entity> entities) {
		myCoreference.entities = entities;
	}

	/**
	 * Gets the resolved mentions.
	 * 
	 * @return myCoreference.the resolved mentions
	 */
	public List<EntityMention> getResolvedMentions() {
		return myCoreference.resolvedEntityMentions;
	}

	/**
	 * Sets the resolved mentions.
	 * 
	 * @param resolvedEntityMentions
	 *            the new resolved mentions
	 */
	public void setResolvedMentions(List<EntityMention> resolvedEntityMentions) {
		myCoreference.resolvedEntityMentions = resolvedEntityMentions;
	}

	public Coreference getCoreference() {
		return myCoreference;
	}

}

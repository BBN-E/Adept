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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;


/**
 * The Class Coreference, which is a relationship between two or more words
 * or phrases in which both refer to the same entity and one stands as a 
 * linguistic antecedent of the other.
 */
public class Coreference extends HltContent {

	/** The coreference id. */
	private final long coreferenceId;

	/** The entities. */
	private List<Entity> entities;

	/** The resolved entity mentions. */
	private List<EntityMention> resolvedEntityMentions;

	/**
	 * Instantiates a new coreference.
	 * 
	 * @param coreferenceId
	 *            the coreference id
	 */
	public Coreference(long coreferenceId) {
		this.coreferenceId = coreferenceId;
	}

	/**
	 * Gets the coreference id.
	 * 
	 * @return the coreference id
	 */
	public long getCoreferenceId() {
		return coreferenceId;
	}

	/**
	 * Gets the entities.
	 * 
	 * @return the entities
	 */
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Sets the entities.
	 * 
	 * @param entities
	 *            the new entities
	 */
	public void setEntities(List<Entity> entities) {
                checkArgument(entities!=null);
		this.entities = entities;
	}

	/**
	 * Gets the resolved mentions.
	 * 
	 * @return the resolved mentions
	 */
	public List<EntityMention> getResolvedMentions() {
		return resolvedEntityMentions;
	}

	/**
	 * Sets the resolved mentions.
	 * 
	 * @param resolvedEntityMentions
	 *            the new resolved mentions
	 */
	public void setResolvedMentions(List<EntityMention> resolvedEntityMentions) {
                checkArgument(resolvedEntityMentions!=null);
		this.resolvedEntityMentions = resolvedEntityMentions;
	}

}

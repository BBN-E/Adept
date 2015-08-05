/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
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
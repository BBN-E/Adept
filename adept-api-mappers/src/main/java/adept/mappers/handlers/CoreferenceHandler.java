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

package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.List;

// TODO: Auto-generated Javadoc
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
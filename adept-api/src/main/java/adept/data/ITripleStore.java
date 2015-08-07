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

package adept.data;

import java.util.List;


/**
 * The Interface IKnowledgeBase.
 */
public interface ITripleStore {

	/**
	 * Gets the values.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @return the values
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<IValue> getValues(IEntity entity, ISlot slot)
			throws AdeptDataException;

	/**
	 * Gets the slots.
	 * 
	 * @param entity
	 *            the entity
	 * @param value
	 *            the value
	 * @return the slots
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<ISlot> getSlots(IEntity entity, IValue value)
			throws AdeptDataException;

	/**
	 * Gets the entities.
	 * 
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 * @return the entities
	 * @throws AdeptDataException
	 *             the adept data exception
	 */
	public List<IEntity> getEntities(ISlot slot, IValue value)
			throws AdeptDataException;
}
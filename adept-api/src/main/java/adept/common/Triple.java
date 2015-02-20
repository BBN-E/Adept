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

import adept.data.IEntity;
import adept.data.ISlot;
import adept.data.IValue;

// TODO: Auto-generated Javadoc
/**
 * The Class Triple.
 */
public class Triple {

	/** The entity. */
	private final IEntity entity;

	/** The slot. */
	private final ISlot slot;

	/** The value. */
	private final IValue tvalue;


	/**
	 * Instantiates a new triple.
	 * 
	 * @param entity
	 *            the entity
	 * @param slot
	 *            the slot
	 * @param value
	 *            the value
	 */
	public Triple(IEntity entity, ISlot slot, IValue value) {
		super();
		this.entity = entity;
		this.slot = slot;
		this.tvalue = value;
	}

	/**
	 * Gets the entity.
	 * 
	 * @return the entity
	 */
	public IEntity getEntity() {
		return entity;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return the slot
	 */
	public ISlot getSlot() {
		return slot;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public IValue getValue() {
		return tvalue;
	}

}
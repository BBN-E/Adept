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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.mappers.handlers;

import thrift.adept.common.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Triple.
 */
public class TripleHandler implements TripleService.Iface {

	private Triple myTriple;

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
	public TripleHandler(Entity entity, Slot slot, String value) {
		super();
		myTriple = new Triple();
		myTriple.entity = entity;
		myTriple.slot = slot;
		myTriple.value = value;
	}

	/**
	 * Gets the entity.
	 * 
	 * @return myTriple.the entity
	 */
	public Entity getEntity() {
		return myTriple.entity;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return myTriple.the slot
	 */
	public Slot getSlot() {
		return myTriple.slot;
	}

	/**
	 * Gets the value.
	 * 
	 * @return myTriple.the value
	 */
	public String getValue() {
		return myTriple.value;
	}

	public Triple getTriple() {
		return myTriple;
	}

}
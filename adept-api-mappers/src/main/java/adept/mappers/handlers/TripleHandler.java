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

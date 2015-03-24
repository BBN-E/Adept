/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.common;

import static com.google.common.base.Preconditions.checkArgument;
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
              
                checkArgument(entity!=null);
                checkArgument(slot!=null);
                checkArgument(value!=null);

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

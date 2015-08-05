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

import adept.data.ISlot;


/**
 * The Class Slot.
 */
public class Slot implements ISlot {

	/** The slot id. */
	private final long slotId;

	/**
	 * Instantiates a new slot.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param slotValue
	 *            the slot value
	 */
	public Slot(long slotId, String slotValue) {
		this.slotId = slotId;
	}

	/**
	 * Gets the slot id.
	 * 
	 * @return the slot id
	 */
	public long getSlotId() {
		return slotId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.ISlot#getSlot()
	 */
	@Override
	public String getSlot() {
		// TODO Auto-generated method stub
		return null;
	}

}

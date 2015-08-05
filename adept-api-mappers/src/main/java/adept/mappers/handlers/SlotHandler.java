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
 * The Class Slot.
 */
public class SlotHandler implements SlotService.Iface {

	private Slot mySlot;

	/**
	 * Instantiates a new slot.
	 * 
	 * @param slotId
	 *            the slot id
	 * @param slotValue
	 *            the slot value
	 */
	public SlotHandler(long slotId, String slotValue) {
		mySlot = new Slot();
		mySlot.slotId = slotId;
	}

	/**
	 * Gets the slot id.
	 * 
	 * @return mySlot.the slot id
	 */
	public long getSlotId() {
		return mySlot.slotId;
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

	public Slot getSlotStruct() {
		return mySlot;
	}

}

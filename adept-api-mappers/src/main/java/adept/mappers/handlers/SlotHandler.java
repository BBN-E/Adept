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


// TODO: Auto-generated Javadoc
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
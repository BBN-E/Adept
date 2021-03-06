package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import adept.data.ISlot;
import java.io.Serializable;


/**
 * The Class Slot.
 */
public class Slot implements ISlot, Serializable {

	private static final long serialVersionUID = -584938411887278182L;
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

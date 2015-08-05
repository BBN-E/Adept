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

package adept.mappers.concrete;

import java.util.UUID;
import java.lang.Math;
import java.math.BigInteger;


/**
 * The Class ID defines an identifier for all instances of objects used in the
 * ADEPT framework. The ID is generated as a universally unique identifier
 * (UUID)
 */
public class ID {

	/** The id. */
	protected UUID id;

	/** The id str. */
	protected String idString = "";

	protected long high;

	protected long low;

	protected long bitField = -1L;

	/**
	 * Instantiates a new id.
	 */
	public ID() {
		id = new UUID(0, 0);
		idString = getId().toString();
		high = 0;
		low = 0;
	}

	public void setId(long inHigh, long inLow) {
		id = new UUID(inHigh, inLow);
		idString = getId().toString();
		high = inHigh;
		low = inLow;
	}

	public void setId(UUID inId) {
		id = inId;
		idString = getId().toString();
		high = inId.getMostSignificantBits();
		low = inId.getLeastSignificantBits();
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return the id string
	 */
	public String getIdString() {
		if (idString == "")
			idString = getId().toString();
		return idString;
	}

}
/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.mappers.concrete;

import java.util.UUID;
import java.lang.Math;
import java.math.BigInteger;

// TODO: Auto-generated Javadoc
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

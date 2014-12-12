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
package adept.common;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class ID defines an identifier for all instances of objects used in the
 * ADEPT framework. The ID is generated as a universally unique identifier
 * (UUID)
 */
public class ID {

	/** The id. */
	private final UUID id;

	/** The id str. */
	private String idStr = "";

	/**
	 * Instantiates a new id.
	 */
	public ID() {
		id = UUID.randomUUID();
		idStr = getId().toString();
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
		if (idStr == "")
			idStr = getId().toString();
		return idStr;
	}

}

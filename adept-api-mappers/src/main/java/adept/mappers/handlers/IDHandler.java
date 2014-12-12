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
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class ID defines an identifier for all instances of objects used in the
 * ADEPT framework. The ID is generated as a universally unique identifier
 * (UUID)
 */
public class IDHandler implements IDService.Iface {

	private ID myID;

	/** The max id. */
	private static long MAX_ID = Long.MAX_VALUE;

	/** The min id. */
	private static long MIN_ID = Long.MIN_VALUE;

	/**
	 * Instantiates a new id.
	 */
	public IDHandler() {
		myID = new ID();
		myID.id = generateID().toString();
		myID.idStr = getId();
	}

	/**
	 * Generate id.
	 * 
	 * @return myID.the uuid
	 */
	private static UUID generateID() {
		if (MIN_ID <= Long.MAX_VALUE)
			MIN_ID++;
		else if (MAX_ID <= Long.MAX_VALUE)
			MAX_ID++;
		else
			System.out.println("generateID(): ran out of IDs!");

		return new UUID(MAX_ID, MIN_ID);
	}

	/**
	 * Gets the id.
	 * 
	 * @return myID.the id
	 */
	public String getId() {
		return myID.id;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myID.the id string
	 */
	public String getIdString() {
		if (myID.idStr == "")
			myID.idStr = getId().toString();
		return myID.idStr;
	}

	public ID getID() {
		return myID;
	}

}

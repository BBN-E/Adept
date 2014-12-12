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
 * An abstract definition of an item, which is composed of an id and a value.
 */
public abstract class ItemHandler implements ItemService.Iface {

	protected Item myItem;

	/** The id. */
	protected IDHandler idHandler;

	/**
	 * Instantiates a new item.
	 */
	public ItemHandler() {
		myItem = new Item();
		IDHandler idHandler = new IDHandler();
		myItem.id = idHandler.getID();
	}

	/**
	 * Gets the value.
	 * 
	 * @return myItem.the value
	 */
	public String getValue() {
		return myItem.value;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myItem.the id string
	 */
	public String getIdString() {
		return idHandler.getIdString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return myItem.the id
	 */
	public String getId() {
		return idHandler.getId();
	}

	public Item getItem() {
		return myItem;
	}

}

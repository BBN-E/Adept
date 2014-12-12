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

import java.util.ArrayList;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class HltContentContainerList.
 */
public class HltContentContainerListHandler extends ArrayList<HltContentContainer> implements HltContentContainerListService.Iface {

	private HltContentContainerList myHltContentContainerList;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 651655831447893195L;

	/** The id. */
	protected IDHandler idHandler;

	/**
	 * Instantiates a new hlt content container list.
	 */
	public HltContentContainerListHandler() {
		myHltContentContainerList = new HltContentContainerList();
		idHandler = new IDHandler();
		myHltContentContainerList.id = idHandler.getID();
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myHltContentContainerList.the id string
	 */
	public String getIdString() {
		return idHandler.getIdString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return myHltContentContainerList.the id
	 */
	public String getId() {
		return idHandler.getId();
	}

	public HltContentContainerList getHltContentContainerList() {
		return myHltContentContainerList;
	}

}

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

import java.util.ArrayList;
import java.util.UUID;

// TODO: Auto-generated Javadoc
/**
 * The Class HltContentContainerList.
 */
public class HltContentContainerList extends ArrayList<HltContentContainer> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 651655831447893195L;

	/** The id. */
	protected ID id;

	/**
	 * Instantiates a new hlt content container list.
	 */
	public HltContentContainerList() {
		this.id = new ID();
	}

	/**
	 * Gets the id string.
	 * 
	 * @return the id string
	 */
	public String getIdString() {
		return this.id.getIdString();
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public UUID getId() {
		return this.id.getId();
	}

}

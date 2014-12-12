/*******************************************************************************
 * Raytheon BBN Technologies Corp., November 2013
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

// TODO: Auto-generated Javadoc
/**
 * The Class EmailAddress.
 */
public class EmailAddress {

	/** The address. */
	private final String address;
	
	/** The display name. */
	private String displayName;

	/**
	 * Instantiates a new email address.
	 *
	 * @param address the address
	 * @param displayName the display name
	 */
	public EmailAddress(String address, String displayName) {
		super();
		this.address = address;
		this.displayName = displayName;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	
	
}

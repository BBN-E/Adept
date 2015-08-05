/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */
package adept.common;

import java.io.Serializable;



/**
 * The Class AdeptException.
 * 
 * @author akumar
 * 
 *         This is the parent class for all exceptions in ADEPT
 */

public class AdeptException extends Exception implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates new AdeptException *.
	 */
	public AdeptException() {

	}
	
	/**
	 * Constructs a new AdeptException with message and cause.
	 *
	 * @param message the message
	 * @param cause the cause
	 */ 
	public AdeptException(String message, Throwable cause) {
		super(message,cause);
	}
	
	/**
	 * Constructs a new AdeptException with message.
	 *
	 * @param message the message
	 */ 
	public AdeptException(String message) {
		super(message);
	}
	
}

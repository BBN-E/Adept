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

// TODO: Auto-generated Javadoc
/**
 * The Class Viewpoint.
 */
public class Viewpoint {

	/** The speaker id. */
	private final String speakerId;

	/** The belief. */
	private final String belief;

	/**
	 * Instantiates a new viewpoint.
	 * 
	 * @param speakerId
	 *            the speaker id
	 * @param belief
	 *            the belief
	 */
	public Viewpoint(String speakerId, String belief) {
		this.speakerId = speakerId;
		this.belief = belief;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return the speaker id
	 */
	public String getSpeakerId() {
		return speakerId;
	}

	/**
	 * Gets the belief.
	 * 
	 * @return the belief
	 */
	public String getBelief() {
		return belief;
	}

}

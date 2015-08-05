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


/**
 * The Class Viewpoint.
 */
public class ViewpointHandler implements ViewpointService.Iface {

	private Viewpoint myViewpoint;

	/**
	 * Instantiates a new viewpoint.
	 * 
	 * @param speakerId
	 *            the speaker id
	 * @param belief
	 *            the belief
	 */
	public ViewpointHandler(String speakerId, String belief) {
		myViewpoint = new Viewpoint();
		myViewpoint.speakerId = speakerId;
		myViewpoint.belief = belief;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return myViewpoint.the speaker id
	 */
	public String getSpeakerId() {
		return myViewpoint.speakerId;
	}

	/**
	 * Gets the belief.
	 * 
	 * @return myViewpoint.the belief
	 */
	public String getBelief() {
		return myViewpoint.belief;
	}

	public Viewpoint getViewpoint() {
		return myViewpoint;
	}

}

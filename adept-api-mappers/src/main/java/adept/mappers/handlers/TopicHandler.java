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
import java.util.List;


/**
 * The Class Topic.
 */
public class TopicHandler implements TopicService.Iface {

	private Topic myTopic;

	/**
	 * The Enum Polarity.
	 */
	public enum Polarity {

		/** The positive. */
		POSITIVE,
		/** The negative. */
		NEGATIVE,
		/** The none. */
		NONE,
	}


	/**
	 * Instantiates a new topic.
	 * 
	 * @param topicId
	 *            the topic id
	 * @param name
	 *            the name
	 */
	public TopicHandler(long topicId, String name) {
		super();
		myTopic = new Topic();
		myTopic.topicId = topicId;
		myTopic.name = name;
	}

	/**
	 * Gets the topic id.
	 * 
	 * @return myTopic.the topic id
	 */
	public long getTopicId() {
		return myTopic.topicId;
	}

	/**
	 * Gets the name.
	 * 
	 * @return myTopic.the name
	 */
	public String getName() {
		return myTopic.name;
	}

	/**
	 * Gets the belief.
	 * 
	 * @return myTopic.the belief
	 */
	public String getBelief() {
		return myTopic.belief;
	}

	/**
	 * Sets the belief.
	 * 
	 * @param belief
	 *            the new belief
	 */
	public void setBelief(String belief) {
		myTopic.belief = belief;
	}

	/**
	 * Adds the viewpoint.
	 * 
	 * @param viewpoint
	 *            the viewpoint
	 */
	public void addViewpoint(Viewpoint viewpoint) {
		if (myTopic.viewpoints == null) {
			myTopic.viewpoints = new ArrayList<Viewpoint>();
		}
		myTopic.viewpoints.add(viewpoint);
	}

	/**
	 * Gets the viewpoints.
	 * 
	 * @return myTopic.the viewpoints
	 */
	public List<Viewpoint> getViewpoints() {
		return myTopic.viewpoints;
	}

	/**
	 * Sets the viewpoints.
	 * 
	 * @param viewpoints
	 *            the new viewpoints
	 */
	public void setViewpoints(List<Viewpoint> viewpoints) {
		myTopic.viewpoints = viewpoints;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return myTopic.the polarity
	 */
	public TopicPolarity getPolarity() {
		return myTopic.polarity;
	}

	/**
	 * Sets the polarity.
	 * 
	 * @param polarity
	 *            the new polarity
	 */
	public void setPolarity(TopicPolarity polarity) {
		myTopic.polarity = polarity;
	}

	public Topic getTopic() {
		return myTopic;
	}

}

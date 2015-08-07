/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;


/**
 * The Class Topic.
 */
public class Topic {

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

	/** The polarity. */
	private Polarity polarity;

	/** The topic id. */
	private final long topicId;

	/** The name. */
	private final String name;

	/** The belief. */
	private String belief;

	/** The viewpoints. */
	private List<Viewpoint> viewpoints;

	/**
	 * Instantiates a new topic.
	 * 
	 * @param topicId
	 *            the topic id
	 * @param name
	 *            the name
	 */
	public Topic(long topicId, String name) {
		super();
		this.topicId = topicId;
            checkArgument(name!=null && name.trim().length()>0);
		this.name = name;
	}

	/**
	 * Gets the topic id.
	 * 
	 * @return the topic id
	 */
	public long getTopicId() {
		return topicId;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the belief.
	 * 
	 * @return the belief
	 */
	public String getBelief() {
		return belief;
	}

	/**
	 * Sets the belief.
	 * 
	 * @param belief
	 *            the new belief
	 */
	public void setBelief(String belief) {
                //TODO: null check
		this.belief = belief;
	}

	/**
	 * Adds the viewpoint.
	 * 
	 * @param viewpoint
	 *            the viewpoint
	 */
	public void addViewpoint(Viewpoint viewpoint) {
		if (viewpoints == null) {
			viewpoints = new ArrayList<Viewpoint>();
		}
                //TODO: null check
		viewpoints.add(viewpoint);
	}

	/**
	 * Gets the viewpoints.
	 * 
	 * @return the viewpoints
	 */
	public List<Viewpoint> getViewpoints() {
		return viewpoints;
	}

	/**
	 * Sets the viewpoints.
	 * 
	 * @param viewpoints
	 *            the new viewpoints
	 */
	public void setViewpoints(List<Viewpoint> viewpoints) {
                //TODO: null check
		this.viewpoints = viewpoints;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return the polarity
	 */
	public Polarity getPolarity() {
		return polarity;
	}

	/**
	 * Sets the polarity.
	 * 
	 * @param polarity
	 *            the new polarity
	 */
	public void setPolarity(Polarity polarity) {
                //TODO: null check
		this.polarity = polarity;
	}

}
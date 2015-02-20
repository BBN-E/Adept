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
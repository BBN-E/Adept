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

import java.util.ArrayList;
import java.util.UUID;


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
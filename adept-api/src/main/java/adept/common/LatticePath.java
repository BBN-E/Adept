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

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class LatticePath.
 */
public class LatticePath {

	/** The weight. */
	private float weight;
	
	/** The token stream list. */
	private List<TokenStream> tokenStreamList;

	/**
	 * Instantiates a new lattice path.
	 */
	public LatticePath() {
		super();
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Gets the token stream list.
	 *
	 * @return the token stream list
	 */
	public List<TokenStream> getTokenStreamList() {
		return tokenStreamList;
	}

	/**
	 * Sets the token stream list.
	 *
	 * @param tokenStreamList the new token stream list
	 */
	public void setTokenStreamList(List<TokenStream> tokenStreamList) {
		this.tokenStreamList = tokenStreamList;
	}
	
	
	
}
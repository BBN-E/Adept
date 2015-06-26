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
 * The Class TimePhrase.
 */
public class TimePhrase extends Chunk {

	/** The type. */
	private IType type;

	/** The resolution type. */
	private IType resolutionType;

	/** The resolution. */
	private String resolution;

	/**
	 * Instantiates a new time phrase.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param type the type
	 */
	public TimePhrase(TokenOffset tokenOffset,
			TokenStream tokenStream, IType type) {
		super(tokenOffset, tokenStream);
		this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type.getType();
	}

	/**
	 * Gets the resolution type.
	 * 
	 * @return the resolution type
	 */
	public String getResolutionType() {
		return resolutionType.getType();
	}

	/**
	 * Gets the resolution.
	 * 
	 * @return the resolution
	 */
	public String getResolution() {
		return resolution;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(IType type) {
		this.type = type;
	}

	/**
	 * Sets the resolution type.
	 *
	 * @param resolutionType the new resolution type
	 */
	public void setResolutionType(IType resolutionType) {
		this.resolutionType = resolutionType;
	}

	/**
	 * Sets the resolution.
	 * 
	 * @param resolution
	 *            the new resolution
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

}
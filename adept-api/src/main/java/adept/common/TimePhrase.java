package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import java.io.Serializable;


/**
 * The Class TimePhrase.
 */
public class TimePhrase extends Chunk implements Serializable {

	private static final long serialVersionUID = 1849809648701519675L;

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
		String type = null;
		if(this.type!=null) {
			type = this.type.getType();
		}
		return type;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((resolution == null) ? 0 : resolution.hashCode());
		result = prime * result + ((resolutionType == null) ? 0 : resolutionType.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimePhrase other = (TimePhrase) obj;
		if (resolution == null) {
			if (other.resolution != null)
				return false;
		} else if (!resolution.equals(other.resolution))
			return false;
		if (resolutionType == null) {
			if (other.resolutionType != null)
				return false;
		} else if (!resolutionType.equals(other.resolutionType))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}


}

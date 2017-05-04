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
 * The Class NumberPhrase.
 */
public class NumberPhrase extends Chunk implements Serializable {

	private static final long serialVersionUID = 5001785970700184353L;
	/** The resolution. */
	private Number number;

	/**
	 * Instantiates a new number phrase.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 */
	public NumberPhrase(TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
	}

	/**
	 * Gets the number.
	 * 
	 * @return the number
	 */
	public Number getNumber() {
		return number;
	}

	/**
	 * Sets the resolution.
	 * 
	 * @param number
	 *            the new number
	 */
	public void setNumber(Number number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		NumberPhrase other = (NumberPhrase) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}
}

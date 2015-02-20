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
 * The Class Paraphrase.
 */
public class Paraphrase extends HltContent implements Comparable<Paraphrase> {

	/** The value. */
	private final String value;

	/** The confidence. */
	private final float confidence;

	/** The pos tag. */
	private Type posTag;

	/**
	 * Instantiates a new paraphrase.
	 * 
	 * @param value
	 *            the value
	 * @param confidence
	 *            the confidence
	 */
	public Paraphrase(String value, float confidence) {
		super();
		this.value = value;
		this.confidence = confidence;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return the pos tag
	 */
	public String getPosTag() {
                if(posTag==null)
                   return null;

		return posTag.getType();
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(Type posTag) {
		this.posTag = posTag;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Item#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Paraphrase pp) {
		
		return this.getConfidence() < pp.getConfidence() ? -1 
			     : this.getConfidence() > pp.getConfidence() ? 1 
			     : 0;
	}
	

}
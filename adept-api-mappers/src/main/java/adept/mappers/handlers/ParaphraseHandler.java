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

package adept.mappers.handlers;

import thrift.adept.common.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Paraphrase.
 */
public class ParaphraseHandler extends HltContentHandler implements ParaphraseService.Iface {

	private Paraphrase myParaphrase;

	/**
	 * Instantiates a new paraphrase.
	 * 
	 * @param value
	 *            the value
	 * @param confidence
	 *            the confidence
	 */
	public ParaphraseHandler(String value, float confidence) {
		super();
		myParaphrase = new Paraphrase();
		myParaphrase.value = value;
		myParaphrase.confidence = confidence;
		myParaphrase.id = myItem.id;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return myParaphrase.the pos tag
	 */
	public String getPosTag() {
		return myParaphrase.posTag.getType();
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(Type posTag) {
		myParaphrase.posTag = posTag;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myParaphrase.the confidence
	 */
	public double getConfidence() {
		return myParaphrase.confidence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Item#getValue()
	 */
	@Override
	public String getValue() {
		return myParaphrase.value;
	}

	public Paraphrase getParaphrase() {
		return myParaphrase;
	}

}
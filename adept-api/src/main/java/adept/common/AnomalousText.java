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
 * The Class AnomalousText.
 */
public class AnomalousText extends HltContent {

	/** The confidence. */
	private final float confidence;

	/** The document. */
	private final Document document;

	/** The explanation. */
	private String explanation;

	/**
	 * Instantiates a new anomalous text.
	 * 
	 * @param confidence
	 *            the confidence
	 * @param document
	 *            the document
	 */
	public AnomalousText(float confidence, Document document) {
		super();
		this.confidence = confidence;
		this.document = document;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Gets the explanation.
	 * 
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * Sets the explanation.
	 * 
	 * @param explanation
	 *            the new explanation
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

}
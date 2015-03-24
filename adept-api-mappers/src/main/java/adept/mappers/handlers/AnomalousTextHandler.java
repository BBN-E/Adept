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
 * The Class AnomalousText.
 */
public class AnomalousTextHandler extends HltContentHandler implements AnomalousTextService.Iface {

	private AnomalousText myAnomalousText;

	/**
	 * Instantiates a new anomalous text.
	 * 
	 * @param confidence
	 *            the confidence
	 * @param document
	 *            the document
	 */
	public AnomalousTextHandler(double confidence, Document document) {
		super();
		myAnomalousText = new AnomalousText();
		myAnomalousText.confidence = confidence;
		myAnomalousText.document = document;
		myAnomalousText.id = myItem.id;
		myAnomalousText.value = myItem.value;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myAnomalousText.the confidence
	 */
	public double getConfidence() {
		return myAnomalousText.confidence;
	}

	/**
	 * Gets the document.
	 * 
	 * @return myAnomalousText.the document
	 */
	public Document getDocument() {
		return myAnomalousText.document;
	}

	/**
	 * Gets the explanation.
	 * 
	 * @return myAnomalousText.the explanation
	 */
	public String getExplanation() {
		return myAnomalousText.explanation;
	}

	/**
	 * Sets the explanation.
	 * 
	 * @param explanation
	 *            the new explanation
	 */
	public void setExplanation(String explanation) {
		myAnomalousText.explanation = explanation;
	}

	public AnomalousText getAnomalousText() {
		return myAnomalousText;
	}

}
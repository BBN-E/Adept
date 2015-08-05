/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.mappers.handlers;

import thrift.adept.common.*;


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

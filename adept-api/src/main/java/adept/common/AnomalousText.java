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
package adept.common;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;

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
		
		checkArgument(document!=null);
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
                checkArgument(explanation!=null && explanation.trim().length()>0);
		this.explanation = explanation;
	}

}

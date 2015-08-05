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

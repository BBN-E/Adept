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

import static com.google.common.base.Preconditions.checkArgument;

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
                checkArgument(value!=null && value.trim().length()>0);
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
                checkArgument(posTag!=null);
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

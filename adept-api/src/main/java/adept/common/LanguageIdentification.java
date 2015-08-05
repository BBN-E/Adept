/*******************************************************************************
 * Raytheon BBN Technologies Corp., November 2013
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

import java.util.Map;


/**
 * The Class LanguageIdentification.
 */
public class LanguageIdentification extends HltContent {

	/** The language probability distribution. */
	private Map<String,Float> languageProbabilityDistribution;

	/**
	 * Instantiates a new language identification.
	 */
	public LanguageIdentification() {
		
	}
	
	/**
	 * Gets the language probability distribution.
	 *
	 * @return the language probability distribution
	 */
	public Map<String,Float> getLanguageProbabilityDistribution() {
		return languageProbabilityDistribution;
	}

	/**
	 * Sets the language probability distribution.
	 *
	 * @param languageProbabilityDistribution the language probability distribution
	 */
	public void setLanguageProbabilityDistribution(
			Map<String,Float> languageProbabilityDistribution) {
                //TODO: Check not null
		this.languageProbabilityDistribution = languageProbabilityDistribution;
	}
	
	/**
	 * Adds the pair.
	 *
	 * @param language the language
	 * @param probability the probability
	 */
	public void addPair(String language, float probability) {
                checkArgument(language!=null && language.trim().length()>0);
		checkArgument((probability >= 0f && probability <= 1f), 
			"Probability was %f but must be between 0 and 1", probability);
		this.languageProbabilityDistribution.put(language, probability);
	}
	
}

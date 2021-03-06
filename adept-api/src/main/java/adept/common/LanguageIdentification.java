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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;


/**
 * The Class LanguageIdentification.
 */
public class LanguageIdentification extends HltContent {

	private static final long serialVersionUID = 204547915310838677L;
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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.data;

import java.util.List;

import adept.common.Paraphrase;


/**
 * The Interface IParaphraseStore.
 */
public interface IParaphraseStore {

	/**
	 * Gets the paraphrases.
	 *
	 * @param phrase the phrase
	 * @return the paraphrases
	 */
	public List<Paraphrase> getParaphrases(String phrase);

}

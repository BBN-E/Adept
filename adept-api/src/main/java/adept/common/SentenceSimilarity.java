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

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class SentenceSimilarity.
 */
public class SentenceSimilarity extends HltContent {

	/** The similarity. */
	private Map<String, Float> similarity;

	/** The sentence1. */
	private final Sentence sentence1;

	/** The sentence2. */
	private final Sentence sentence2;

	/**
	 * Instantiates a new sentence similarity.
	 * 
	 * @param similarity
	 *            the similarity as scored by one or more algorithms
	 * @param sentence1
	 *            the sentence1
	 * @param sentence2
	 *            the sentence2
	 */
	public SentenceSimilarity(Map<String, Float> similarity, Sentence sentence1,
			Sentence sentence2) {
		super();

                checkArgument(similarity!=null);
                checkArgument(sentence1!=null);
                checkArgument(sentence2!=null);
		this.similarity = similarity;
		this.sentence1 = sentence1;
		this.sentence2 = sentence2;
	}

	/**
	 * Gets the similarity.
	 * 
	 * @return the similarity
	 */
	public Map<String, Float> getSimilarity() {
		return similarity;
	}

	/**
	 * Gets the sentence1.
	 * 
	 * @return the sentence1
	 */
	public Sentence getSentence1() {
		return sentence1;
	}

	/**
	 * Gets the sentence2.
	 * 
	 * @return the sentence2
	 */
	public Sentence getSentence2() {
		return sentence2;
	}

}

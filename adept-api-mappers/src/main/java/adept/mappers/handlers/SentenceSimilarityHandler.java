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

// TODO: Auto-generated Javadoc
/**
 * The Class SentenceSimilarity.
 */
public class SentenceSimilarityHandler extends HltContentHandler implements SentenceSimilarityService.Iface {

	private SentenceSimilarity mySentenceSimilarity;

	/**
	 * Instantiates a new sentence similarity.
	 * 
	 * @param similarity
	 *            the similarity
	 * @param sentence1
	 *            the sentence1
	 * @param sentence2
	 *            the sentence2
	 */
	public SentenceSimilarityHandler(float similarity, Sentence sentence1,
			Sentence sentence2) {
		super();
		mySentenceSimilarity.similarity = similarity;
		mySentenceSimilarity.sentence1 = sentence1;
		mySentenceSimilarity.sentence2 = sentence2;
		mySentenceSimilarity.id = myItem.id;
		mySentenceSimilarity.value = myItem.value;
	}

	/**
	 * Gets the similarity.
	 * 
	 * @return mySentenceSimilarity.the similarity
	 */
	public double getSimilarity() {
		return mySentenceSimilarity.similarity;
	}

	/**
	 * Gets the sentence1.
	 * 
	 * @return mySentenceSimilarity.the sentence1
	 */
	public Sentence getSentence1() {
		return mySentenceSimilarity.sentence1;
	}

	/**
	 * Gets the sentence2.
	 * 
	 * @return mySentenceSimilarity.the sentence2
	 */
	public Sentence getSentence2() {
		return mySentenceSimilarity.sentence2;
	}

	public SentenceSimilarity getSentenceSimilarity() {
		return mySentenceSimilarity;
	}

}

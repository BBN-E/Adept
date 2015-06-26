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
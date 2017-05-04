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
import java.io.Serializable;

import java.util.Map;


/**
 * The Class SentenceSimilarity.
 */
public class SentenceSimilarity extends HltContent implements Serializable {

	private static final long serialVersionUID = -6789294966308207856L;

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

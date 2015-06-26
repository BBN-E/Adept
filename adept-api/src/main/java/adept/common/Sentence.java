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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

// TODO: Auto-generated Javadoc
/**
 * The Sentence class extends Chunk and represents the output from sentence
 * boundary detection algorithm.
 */
public class Sentence extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The type. */
	private SentenceType type;

	/** The punctuation. */
	private String punctuation;

	/** The uncertainty confidence. */
	private float uncertaintyConfidence;

	/** The novelty confidence. */
	private float noveltyConfidence;

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public Sentence(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return the uncertainty confidence
	 */
	public float getUncertaintyConfidence() {
		return uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(float uncertaintyConfidence) {
		this.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return the novelty confidence
	 */
	public float getNoveltyConfidence() {
		return noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(float noveltyConfidence) {
		this.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public SentenceType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(SentenceType type) {
                checkArgument(type!=null);
		this.type = type;
	}

	/**
	 * Gets the punctuation.
	 * 
	 * @return the punctuation
	 */
	public String getPunctuation() {
		return punctuation;
	}

	/**
	 * Sets the punctuation.
	 * 
	 * @param punctuation
	 *            the new punctuation
	 */
	public void setPunctuation(String punctuation) {
                //TODO: null check
		this.punctuation = punctuation;
	}

}
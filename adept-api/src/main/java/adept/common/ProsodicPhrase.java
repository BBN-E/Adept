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

// TODO: Auto-generated Javadoc
/**
 * The Class ProsodicPhrase.
 */
public class ProsodicPhrase extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The confidence. */
	private float confidence;

	/** The type. */
	private String type;

	/** The uncertainty confidence. */
	private float uncertaintyConfidence;

	/** The novelty confidence. */
	private float noveltyConfidence;

	/**
	 * Instantiates a new prosodic phrase.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 */
	public ProsodicPhrase(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
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
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

}
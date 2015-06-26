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
 * The Sentence class extends ChunkHandler and represents the output from sentence
 * boundary detection algorithm.
 */
public class SentenceHandler extends ChunkHandler implements SentenceService.Iface {

	private Sentence mySentence;

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
	public SentenceHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		mySentence = new Sentence();
		// TODO Auto-generated constructor stub
		mySentence.sequenceId = sequenceId;
		mySentence.id = myItem.id;
		mySentence.value = myItem.value;
		mySentence.tokenOffset = myChunk.tokenOffset;
		mySentence.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return mySentence.the sequence id
	 */
	public long getSequenceId() {
		return mySentence.sequenceId;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return mySentence.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return mySentence.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		mySentence.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return mySentence.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return mySentence.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		mySentence.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return mySentence.the type
	 */
	public SentenceType getType() {
		return mySentence.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(SentenceType type) {
		mySentence.type = type;
	}

	/**
	 * Gets the punctuation.
	 * 
	 * @return mySentence.the punctuation
	 */
	public String getPunctuation() {
		return mySentence.punctuation;
	}

	/**
	 * Sets the punctuation.
	 * 
	 * @param punctuation
	 *            the new punctuation
	 */
	public void setPunctuation(String punctuation) {
		mySentence.punctuation = punctuation;
	}

	public Sentence getSentence() {
		return mySentence;
	}

}
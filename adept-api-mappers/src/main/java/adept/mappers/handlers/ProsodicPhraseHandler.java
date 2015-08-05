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


/**
 * The Class ProsodicPhrase.
 */
public class ProsodicPhraseHandler extends ChunkHandler implements ProsodicPhraseService.Iface {

	private ProsodicPhrase myProsodicPhrase;

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
	public ProsodicPhraseHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId) {
		super(tokenOffset, tokenStream);
		myProsodicPhrase = new ProsodicPhrase();
		myProsodicPhrase.sequenceId = sequenceId;
		myProsodicPhrase.id = myItem.id;
		myProsodicPhrase.value = myItem.value;
		myProsodicPhrase.tokenOffset = myChunk.tokenOffset;
		myProsodicPhrase.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return myProsodicPhrase.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return myProsodicPhrase.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		myProsodicPhrase.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return myProsodicPhrase.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return myProsodicPhrase.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		myProsodicPhrase.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myProsodicPhrase.the confidence
	 */
	public double getConfidence() {
		return myProsodicPhrase.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		myProsodicPhrase.confidence = confidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return myProsodicPhrase.the type
	 */
	public String getType() {
		return myProsodicPhrase.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		myProsodicPhrase.type = type;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myProsodicPhrase.the sequence id
	 */
	public long getSequenceId() {
		return myProsodicPhrase.sequenceId;
	}

	public ProsodicPhrase getProsodicPhrase() {
		return myProsodicPhrase;
	}

}
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
 * The Class Utterance.
 */
public class UtteranceHandler extends ChunkHandler implements UtteranceService.Iface {

	private Utterance myUtterance;

	/**
	 * Instantiates a new utterance.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param utteranceId
	 *            the utterance id
	 * @param speakerId
	 *            the speaker id
	 * @param annotation
	 *            the annotation
	 */
	public UtteranceHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long utteranceId, long speakerId, String utterance) {
		super(tokenOffset, tokenStream);
		myUtterance = new Utterance();
		myUtterance.utteranceId = utteranceId;
		myUtterance.speakerId = speakerId;
		myUtterance.annotation = utterance;
		myUtterance.id = myItem.id;
		myUtterance.value = myItem.value;
		myUtterance.tokenOffset = myChunk.tokenOffset;
		myUtterance.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the utterance id.
	 * 
	 * @return myUtterance.the utterance id
	 */
	public long getUtteranceId() {
		return myUtterance.utteranceId;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return myUtterance.the speaker id
	 */
	public long getSpeakerId() {
		return myUtterance.speakerId;
	}


	/**
	 * Gets the annotation.
	 * 
	 * @return myUtterance.the annotation
	 */
	public String getAnnotation() {
		return myUtterance.annotation;
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 *            the new annotation
	 */
	public void setAnnotation(String annotation) {
		myUtterance.annotation = annotation;
	}

	public Utterance getUtterance() {
		return myUtterance;
	}

}
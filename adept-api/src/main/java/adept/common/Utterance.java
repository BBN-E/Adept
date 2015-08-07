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


/**
 * The Class Utterance.
 */
public class Utterance extends Chunk {

	/** The utterance id. */
	private final long utteranceId;

	/** The speaker id. */
	private final String speakerId;

	/** The annotation. */
	private String annotation;
	
	/** The utterance. */
	private String utterance;

	/**
	 * Instantiates a new utterance.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param utteranceId the utterance id
	 * @param speakerId the speaker id
	 * @param utterance the utterance
	 */
	public Utterance(TokenOffset tokenOffset, TokenStream tokenStream,
			long utteranceId, String speakerId, String utterance) {
		super(tokenOffset, tokenStream);
		this.utteranceId = utteranceId;
                checkArgument(speakerId!=null && speakerId.trim().length()>0);
		this.speakerId = speakerId;
		this.utterance = utterance;
	}

	/**
	 * Gets the utterance id.
	 * 
	 * @return the utterance id
	 */
	public long getUtteranceId() {
		return utteranceId;
	}

	/**
	 * Gets the speaker id.
	 * 
	 * @return the speaker id
	 */
	public String getSpeakerId() {
		return speakerId;
	}
	
	/**
	 * Gets the utterance.
	 * 
	 * @return the utterance
	 */
	public String getUtterance() {
		return utterance;
	}

	/**
	 * Gets the annotation.
	 * 
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * Sets the annotation.
	 * 
	 * @param annotation
	 *            the new annotation
	 */
	public void setAnnotation(String annotation) {
                //TODO: Null check
		this.annotation = annotation;
	}

}
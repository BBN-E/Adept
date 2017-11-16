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


/**
 * The Class Utterance.
 */
public class Utterance extends Chunk implements Serializable {

	private static final long serialVersionUID = 1387753699657117529L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + ((speakerId == null) ? 0 : speakerId.hashCode());
		result = prime * result + ((utterance == null) ? 0 : utterance.hashCode());
		result = prime * result + (int) (utteranceId ^ (utteranceId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utterance other = (Utterance) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		if (speakerId == null) {
			if (other.speakerId != null)
				return false;
		} else if (!speakerId.equals(other.speakerId))
			return false;
		if (utterance == null) {
			if (other.utterance != null)
				return false;
		} else if (!utterance.equals(other.utterance))
			return false;
		if (utteranceId != other.utteranceId)
			return false;
		return true;
	}

}

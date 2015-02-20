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
 * The Class Passage, which represents a section of text 
 * which has been extracted from a document, particularly 
 * a section of medium length. Every DEFT Document has at
 * least one passage.
 */
public class Passage extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The content type. */
	private String contentType;

	/** The AudioOffset. */
	private AudioOffset audioOffset;

	/** The speaker. */
	private String speaker;

	/**
	 * Instantiates a new passage.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public Passage(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	 * Gets the audioOffset.
	 * 
	 * @return the audioOffset
	 */
	public AudioOffset getAudioOffset() {
		return audioOffset;
	}

	/**
	 * Sets the audioOffset.
	 * 
	 * @param audioOffset
	 *            the new audioOffset
	 */
	public void setAudioOffset(AudioOffset audioOffset) {
		this.audioOffset = audioOffset;
	}	
	
	/**
	 * Gets the speaker.
	 * 
	 * @return the speaker
	 */
	public String getSpeaker() {
		return speaker;
	}

	/**
	 * Sets the speaker.
	 * 
	 * @param speaker
	 *            the new speaker
	 */
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

}
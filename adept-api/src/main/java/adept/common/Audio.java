/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

/*
 * 
 */
package adept.common;

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Audio.
 */
public class Audio {

	/** The uri. */
	private String uri;
	
	/** The audio buffer. */
	private byte[] audioBuffer;
	
	/** The audio file type. */
	private AudioFileType audioType;
	
	/** The audio offset. */
	private AudioOffset audioOffset;

		
	/**
	 * Instantiates a new audio.
	 */
	public Audio() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the uri.
	 *
	 * @param uri the new uri
	 */
	public void setUri(String uri) {
                checkArgument(uri!=null && uri.trim().length()>0);
		this.uri = uri;
	}

	/**
	 * Gets the audio buffer.
	 *
	 * @return the audio buffer
	 */
	public byte[] getAudioBuffer() {
		return audioBuffer;
	}

	/**
	 * Sets the audio buffer.
	 *
	 * @param audioBuffer the new audio buffer
	 */
	public void setAudioBuffer(byte[] audioBuffer) {
                checkArgument(audioBuffer!=null);
		this.audioBuffer = audioBuffer;
	}

	/**
	 * Gets the audio type.
	 *
	 * @return the audio type
	 */
	public AudioFileType getAudioType() {
		return audioType;
	}

	/**
	 * Sets the audio type.
	 *
	 * @param audioType the new audio type
	 */
	public void setAudioType(AudioFileType audioType) {
                //TODO: Add null or empty check
		this.audioType = audioType;
	}

	/**
	 * Gets the audio offset.
	 *
	 * @return the audio offset
	 */
	public AudioOffset getAudioOffset() {
		return audioOffset;
	}

	/**
	 * Sets the audio offset.
	 *
	 * @param audioOffset the new audio offset
	 */
	public void setAudioOffset(AudioOffset audioOffset) {
                checkArgument(audioOffset!=null);
		this.audioOffset = audioOffset;
	}
	
	
	
}

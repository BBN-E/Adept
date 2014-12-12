/*******************************************************************************
 * Raytheon BBN Technologies Corp., November 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

// TODO: Auto-generated Javadoc
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
		this.audioOffset = audioOffset;
	}
	
	
	
}

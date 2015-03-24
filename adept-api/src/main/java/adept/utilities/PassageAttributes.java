package adept.utilities;

import adept.common.AudioOffset;

// TODO: Auto-generated Javadoc
/**
 * The Class PassageAttributes.
 */
public class PassageAttributes {

	/** The value. */
	private String value;
	
	/** The passage id. */
	private long passageId;
	
	/** The sarcasm value. */
	private String sarcasmValue;

	/** The AudioOffset. */
	private AudioOffset audioOffset;

	/** The speaker. */
	private String speaker;

	/** The post-passage offset. */
	private int postPassageOffset = 0;

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the passage id.
	 *
	 * @return the passage id
	 */
	public long getPassageId() {
		return passageId;
	}

	/**
	 * Sets the passage id.
	 *
	 * @param passageId the new passage id
	 */
	public void setPassageId(long passageId) {
		this.passageId = passageId;
	}

	/**
	 * Gets the sarcasm value.
	 *
	 * @return the sarcasm value
	 */
	public String getSarcasmValue() {
		return sarcasmValue;
	}

	/**
	 * Sets the sarcasm value.
	 *
	 * @param sarcasmValue the new sarcasm value
	 */
	public void setSarcasmValue(String sarcasmValue) {
		this.sarcasmValue = sarcasmValue;
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
	
	/**
	 * Gets the speaker.
	 * 
	 * @return the speaker
	 */
	public int getPostPassageOffset() {
		return postPassageOffset;
	}

	/**
	 * Sets the speaker.
	 * 
	 * @param speaker
	 *            the new speaker
	 */
	public void setPostPassageOffset(int postPassageOffset) {
		this.postPassageOffset = postPassageOffset;
	}
	
}

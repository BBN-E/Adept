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

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Token, which is a single word or 
 * other lexically-defined character sequence 
 * within a document.
 */
public class Token extends Item implements Serializable {

	private static final long serialVersionUID = -3369688708523320777L;

	/** The char offset. */
	private final CharOffset charOffset;

    /** The raw char offset. */
    //    private CharOffset rawCharOffset;

	/** The sequence id. */
	private final long sequenceId;

	/** The token type. */
	private TokenType tokenType;

	/** The lemma. */
	private String lemma;

	/** The audio offset. */
	private AudioOffset audioOffset;

	/** The confidence. */
	private float confidence;

	/**
	 * Create a copy of the given token, but with a difference CharOffset
	 * @param t The Token to copy
	 * @param newCharOffset The new CharOffset to use
	 */
	public static Token modifyCharOffset(Token t, CharOffset newCharOffset) {
		Token ret = new Token(t.sequenceId, newCharOffset, t.value);
		ret.setTokenType(t.tokenType);
		ret.setLemma(t.lemma);
		ret.setAudioOffset(t.audioOffset);
		ret.setConfidence(t.confidence);
		return ret;
	}

	/**
	 * Create a copy of the given token, but with a difference value
	 * @param t The Token to copy
	 * @param value The new value to use
	 */
	public static Token modifyValue(Token t, String value) {
		Token ret = new Token(t.sequenceId, t.charOffset, value);
		ret.setTokenType(t.tokenType);
		ret.setLemma(t.lemma);
		ret.setAudioOffset(t.audioOffset);
		ret.setConfidence(t.confidence);
		return ret;
	}

	/**
	 * Create a copy of the given token, but with a difference sequence id
	 * @param t The Token to copy
	 * @param sequenceId The new sequence id to use
	 */
	public static Token modifySequenceId(Token t, long sequenceId) {
		Token ret = new Token(sequenceId, t.charOffset, t.value);
		ret.setTokenType(t.tokenType);
		ret.setLemma(t.lemma);
		ret.setAudioOffset(t.audioOffset);
		ret.setConfidence(t.confidence);
		return ret;
	}

  	private Token() {
	  charOffset = new CharOffset(-1, -1);
	  sequenceId = -1L;
	}

	/**
	 * Instantiates a new token.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param charOffset
	 *            the char offset
	 * @param value
	 *            the value
	 */
	public Token(long sequenceId, CharOffset charOffset, String value) {
		this.sequenceId = sequenceId;
                checkArgument(charOffset!=null);
		this.charOffset = charOffset;
                checkArgument(value!=null && value.trim().length()>0);
		this.value = value;
	}

	/**
	 * Gets the char offset.
	 * 
	 * @return the char offset
	 */
	public CharOffset getCharOffset() {
		return charOffset;
	}

	/**
	 * Gets the raw char offset.
	 * 
	 * @return the raw char offset
	 */
    /*	public CharOffset getRawCharOffset() {
		return rawCharOffset;
        }*/

	/**
	 * Sets the raw char offset.
	 * 
	 * @param rawCharOffset the new raw char offset
	 */
    /*	public void setRawCharOffset(CharOffset rawCharOffset) {
		this.rawCharOffset = rawCharOffset;
        }*/

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the token type.
	 * 
	 * @return the token type
	 */
	public TokenType getTokenType() {
		return tokenType;
	}

	/**
	 * Sets the token type.
	 * 
	 * @param tokenType
	 *            the new token type
	 */
	public void setTokenType(TokenType tokenType) {
                //TODO: null check
		this.tokenType = tokenType;
	}

	/**
	 * Gets the lemma.
	 * 
	 * @return the lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Sets the lemma.
	 * 
	 * @param lemma
	 *            the new lemma
	 */
	public void setLemma(String lemma) {
                //TODO: null and empty check
		this.lemma = lemma;
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
	 * @param audioOffset
	 *            the new audio offset
	 */
	public void setAudioOffset(AudioOffset audioOffset) {
                //TODO: null check
		this.audioOffset = audioOffset;
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

}

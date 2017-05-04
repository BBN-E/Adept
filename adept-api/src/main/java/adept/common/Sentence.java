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

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;


/**
 * The Sentence class extends Chunk and represents the output from sentence
 * boundary detection algorithm.
 */
public class Sentence extends Chunk implements Serializable {

	private static final long serialVersionUID = -6638775786658715800L;

	/** The sequence id. */
	private final long sequenceId;

	/** The type. */
	private SentenceType type;

	/** The punctuation. */
	private String punctuation;

	/** The uncertainty confidence. */
	private float uncertaintyConfidence;

	/** The novelty confidence. */
	private float noveltyConfidence;

	/** The morphological analysis. */
	private MorphSentence morphSentence;

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
	public Sentence(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
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
	 * Gets the uncertainty confidence.
	 * 
	 * @return the uncertainty confidence
	 */
	public float getUncertaintyConfidence() {
		return uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(float uncertaintyConfidence) {
		this.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return the novelty confidence
	 */
	public float getNoveltyConfidence() {
		return noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(float noveltyConfidence) {
		this.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public SentenceType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(SentenceType type) {
                checkArgument(type!=null);
		this.type = type;
	}

	/**
	 * Gets the punctuation.
	 * 
	 * @return the punctuation
	 */
	public String getPunctuation() {
		return punctuation;
	}

	/**
	 * Sets the punctuation.
	 * 
	 * @param punctuation
	 *            the new punctuation
	 */
	public void setPunctuation(String punctuation) {
                //TODO: null check
		this.punctuation = punctuation;
	}

	/**
	 * Gets the morphological information for the sentence.
	 *
	 * @return the morphological information, which may be absent
	 */
	public Optional<MorphSentence> getMorphSentence() {
		return Optional.fromNullable(morphSentence);
	}

	/**
	 * Sets the morphological annotation for the sentence.
	 *
	 * @param morphSentence the new morphological information
	 */
	public void setMorphSentence(MorphSentence morphSentence) {
		this.morphSentence = checkNotNull(morphSentence);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((morphSentence == null) ? 0 : morphSentence.hashCode());
		result = prime * result + Float.floatToIntBits(noveltyConfidence);
		result = prime * result + ((punctuation == null) ? 0 : punctuation.hashCode());
		result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + Float.floatToIntBits(uncertaintyConfidence);
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
		Sentence other = (Sentence) obj;
		if (morphSentence == null) {
			if (other.morphSentence != null)
				return false;
		} else if (!morphSentence.equals(other.morphSentence))
			return false;
		if (Float.floatToIntBits(noveltyConfidence) != Float.floatToIntBits(other.noveltyConfidence))
			return false;
		if (punctuation == null) {
			if (other.punctuation != null)
				return false;
		} else if (!punctuation.equals(other.punctuation))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		if (type != other.type)
			return false;
		if (Float.floatToIntBits(uncertaintyConfidence) != Float.floatToIntBits(other.uncertaintyConfidence))
			return false;
		return true;
	}
}

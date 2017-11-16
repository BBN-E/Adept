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
 * The Class PartOfSpeech.
 */
public class PartOfSpeech extends Chunk implements Serializable {

	private static final long serialVersionUID = -5976555372188415431L;

	/** The pos tag. */
	private IType posTag;

	/** The sequence id. */
	private final long sequenceId;

	/**
	 * Instantiates a new part of speech.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public PartOfSpeech(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return the pos tag
	 */
	public IType getPosTag() {
		return posTag;
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(IType posTag) {
  
                checkArgument(posTag!=null);
		this.posTag = posTag;
	}

	/**
	 * Gets the part of speech tag.
	 * 
	 * @return the part of speech tag
	 */
	public IType getPartOfSpeechTag() {
		return posTag;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((posTag == null) ? 0 : posTag.hashCode());
		result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
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
		PartOfSpeech other = (PartOfSpeech) obj;
		if (posTag == null) {
			if (other.posTag != null)
				return false;
		} else if (!posTag.equals(other.posTag))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		return true;
	}
}

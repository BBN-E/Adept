/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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


/**
 * The Class DiscourseUnit.
 */
public class DiscourseUnit extends Chunk {

	private static final long serialVersionUID = 1112000157213662861L;

	/** The sequence id. */
	private final long sequenceId;

	/** The discourse type. */
	private String discourseType;

	/** The uncertainty confidence. */
	private Float uncertaintyConfidence;

	/** The novelty confidence. */
	private Float noveltyConfidence;

	/**
	 * Instantiates a new discourse unit.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 * @param discourseType
	 *            the discourse type
	 */
	public DiscourseUnit(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String discourseType) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
                checkArgument(discourseType!=null && discourseType.trim().length()>0);
		this.discourseType = discourseType;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return the uncertainty confidence
	 */
	public Optional<Float> getUncertaintyConfidence() {
		return Optional.fromNullable(uncertaintyConfidence);
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
	public Optional<Float> getNoveltyConfidence() {
		return Optional.fromNullable(noveltyConfidence);
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
	 * Gets the discourse type.
	 * 
	 * @return the discourse type
	 */
	public String getDiscourseType() {
		return discourseType;
	}

	/**
	 * Sets the discourse type.
	 * 
	 * @param discourseType
	 *            the new discourse type
	 */
	public void setDiscourseType(String discourseType) {
                checkArgument(discourseType!=null && discourseType.trim().length()>0);
		this.discourseType = discourseType;
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
		result = prime * result + ((discourseType == null) ? 0 : discourseType.hashCode());
		result = prime * result + ((noveltyConfidence == null) ? 0 : noveltyConfidence.hashCode());
		result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
		result = prime * result + ((uncertaintyConfidence == null) ? 0 : uncertaintyConfidence.hashCode());
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
		DiscourseUnit other = (DiscourseUnit) obj;
		if (discourseType == null) {
			if (other.discourseType != null)
				return false;
		} else if (!discourseType.equals(other.discourseType))
			return false;
		if (noveltyConfidence == null) {
			if (other.noveltyConfidence != null)
				return false;
		} else if (!noveltyConfidence.equals(other.noveltyConfidence))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		if (uncertaintyConfidence == null) {
			if (other.uncertaintyConfidence != null)
				return false;
		} else if (!uncertaintyConfidence.equals(other.uncertaintyConfidence))
			return false;
		return true;
	}
}
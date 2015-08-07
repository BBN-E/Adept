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

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class DiscourseUnit.
 */
public class DiscourseUnit extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The discource type. */
	private String discourceType;

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
	 * @param discourceType
	 *            the discource type
	 */
	public DiscourseUnit(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String discourseType) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
                checkArgument(discourseType!=null && discourseType.trim().length()>0);
		this.discourceType = discourseType;
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
	 * Gets the discource type.
	 * 
	 * @return the discource type
	 */
	public String getDiscourceType() {
		return discourceType;
	}

	/**
	 * Sets the discource type.
	 * 
	 * @param discourceType
	 *            the new discource type
	 */
	public void setDiscourceType(String discourseType) {
                checkArgument(discourseType!=null && discourseType.trim().length()>0);
		this.discourceType = discourseType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

}
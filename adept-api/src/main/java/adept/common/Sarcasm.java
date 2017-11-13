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

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;

/**
 * The Class Sarcasm.
 */
public class Sarcasm extends Chunk implements Serializable {

	private static final long serialVersionUID = 8965001753590610528L;

	/** The sarcasm id. */
	private final long sarcasmId;

	/**
	 * The Enum Judgment.
	 */
	public enum Judgment {

		/** The positive. */
		POSITIVE,
		/** The negative. */
		NEGATIVE,
		/** The none. */
		NONE,
	}

	/** The judgment. */
	private final Judgment judgment;

	/** The confidence. */
	private float confidence;

	/**
	 * Instantiates a new sarcasm.
	 * 
	 * @param sarcasmId
	 *            the sarcasm id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param judgment
	 *            the judgment
	 */
	public Sarcasm(long sarcasmId, TokenOffset tokenOffset,
			TokenStream tokenStream, Judgment judgment) {
		super(tokenOffset, tokenStream);
		this.sarcasmId = sarcasmId;

                checkArgument(judgment!=null);
		this.judgment = judgment;
	}

	/**
	 * Gets the sarcasm id.
	 * 
	 * @return the sarcasm id
	 */
	public long getSarcasmId() {
		return sarcasmId;
	}

	/**
	 * Gets the judgment.
	 * 
	 * @return the judgment
	 */
	public Judgment getJudgment() {
		return judgment;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(confidence);
		result = prime * result + ((judgment == null) ? 0 : judgment.hashCode());
		result = prime * result + (int) (sarcasmId ^ (sarcasmId >>> 32));
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
		Sarcasm other = (Sarcasm) obj;
		if (Float.floatToIntBits(confidence) != Float.floatToIntBits(other.confidence))
			return false;
		if (judgment != other.judgment)
			return false;
		if (sarcasmId != other.sarcasmId)
			return false;
		return true;
	}

}
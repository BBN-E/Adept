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
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;

/**
 * Holds a value for a paraphrase of a string or a {@link Chunk}.
 */
public class Paraphrase extends HltContent implements Comparable<Paraphrase>, Serializable {

	private static final long serialVersionUID = 8851464791957991349L;

	/* The source phrase as a string.
	 * If sourcePhraseChunk is present,
	 * this should be expected to hold
	 * a null value.
	 */
	private final String sourcePhraseString;

	/** The source phrase, as a {@link Chunk}. */
	/* If this is present, sourcePhraseString
	 * should be expected to hold a null value.
	 */
	private final Optional<Chunk> sourcePhraseChunk;

	/** The paraphrase target value. */
	private final String value;

	/** The paraphrase constituent label */
	private final Optional<Type> constituentLabel;

	/** The paraphrase entailment relation */
	private final Optional<Type> entailmentRelation;

	/** The paraphrase confidence score. */
	private final float confidence;

	/**
	 * @param sourcePhraseString The source phrase, as a string.
	 * @param value The paraphrase target value.
	 * @param constituentLabel The paraphrase constituent label.
	 * @param confidence The paraphrase confidence score.
	 */
	public Paraphrase(String sourcePhraseString, String value, Optional<Type> constituentLabel, Optional<Type> entailmentRelation, float confidence) {
		checkNotNull(sourcePhraseString);
		checkArgument(!sourcePhraseString.trim().isEmpty());
		this.sourcePhraseString = sourcePhraseString;
		this.sourcePhraseChunk = Optional.absent();
		checkNotNull(value);
		checkArgument(!value.trim().isEmpty());
		this.value = value;
		checkNotNull(constituentLabel);
		if (constituentLabel.isPresent()) {
			checkArgument(!constituentLabel.get().getType().isEmpty());
		}
		this.constituentLabel = constituentLabel;
		checkNotNull(entailmentRelation);
		if (entailmentRelation.isPresent()) {
			checkArgument(!entailmentRelation.get().getType().isEmpty());
		}
		this.entailmentRelation = entailmentRelation;
		this.confidence = confidence;
	}

	/**
	 * @param sourcePhraseChunk The source phrase, as {@link Chunk}.
	 * @param value The paraphrase target value.
	 * @param constituentLabel The paraphrase constituent label.
	 * @param confidence The paraphrase confidence score.
	 */
	public Paraphrase(Chunk sourcePhraseChunk, String value, Optional<Type> constituentLabel, Optional<Type> entailmentRelation, float confidence) {
		this.sourcePhraseString = null;
		checkNotNull(sourcePhraseChunk);
		this.sourcePhraseChunk = Optional.of(sourcePhraseChunk);
		checkNotNull(value);
		checkArgument(!value.trim().isEmpty());
		this.value = value;
		checkNotNull(constituentLabel);
		if (constituentLabel.isPresent()) {
			checkArgument(!constituentLabel.get().getType().isEmpty());
		}
		this.constituentLabel = constituentLabel;
		checkNotNull(entailmentRelation);
		if (entailmentRelation.isPresent()) {
			checkArgument(!entailmentRelation.get().getType().isEmpty());
		}
		this.entailmentRelation = entailmentRelation;
		this.confidence = confidence;
	}

	/**
	 * Returns whether the source phrase is available as a {@link Chunk}.
	 * @see #getSourcePhraseAsChunk()
	 * @see #getSourcePhraseText()
	 */
	public boolean hasSourcePhraseAsChunk() {
		return sourcePhraseChunk.isPresent();
	}

	/** Get the source phrase's text value.
	 *  This does not depend on whether the
	 *  source phrase is a string or a {@link Chunk}.
	 */
	public String getSourcePhraseText() {
		if (sourcePhraseChunk.isPresent()) {
			return sourcePhraseChunk.get().getValue();
		} else {
			return sourcePhraseString;
		}
	}

	/** Get the source phrase as a {@link Chunk}; it may be absent.
	 * @see #hasSourcePhraseAsChunk()
	 */
	public Optional<Chunk> getSourcePhraseAsChunk() {
		return sourcePhraseChunk;
	}

	/*
	 * (non-Javadoc)
	 * @see adept.common.Item#getValue()
	 */
	/**
	 * @return The paraphrase target value.
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @return The constituent label; it may be absent.
	 */
	public Optional<Type> getConstituentLabel() {
		return constituentLabel;
	}

	/**
	 * @return The entailment relation; it may be absent.
	 */
	public Optional<Type> getEntailmentRelation() {
		return entailmentRelation;
	}

	/**
	 * @return The paraphrase confidence score.
	 */
	public float getConfidence() {
		return confidence;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/** Comparison is done on the basis of confidence score. */
	@Override
	public int compareTo(Paraphrase pp) {
	  return Float.compare(this.getConfidence(), pp.getConfidence());
	}
}
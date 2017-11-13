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

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;

/**
 * Represents the morphological information for a sentence.
 */
@Beta
public final class MorphSentence implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * The analyzed morph token sequences of the sentence.
	 */
	private final ImmutableList<MorphTokenSequence> sequences;

	private MorphSentence() {
		// private no args constructor for dozer
		sequences = null;
	}

	private MorphSentence(final ImmutableList<MorphTokenSequence> sequences) {
		this.sequences = checkNotNull(sequences);
	}

	/**
	 * Creates a new builder.
	 *
	 * @return the builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Gets the morphological token sequences for this sentence.
	 *
	 * @return the sequences
	 */
	public ImmutableList<MorphTokenSequence> morphTokenSequences() {
		return sequences;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final MorphSentence that = (MorphSentence) o;
		return Objects.equal(this.sequences, that.sequences);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(sequences);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("sequences", sequences)
				.toString();
	}

	/**
	 * Builds {@link MorphSentence} objects.
	 */
	public static final class Builder {

		private final List<MorphTokenSequence> sequencesBuilder;

		private Builder() {
			sequencesBuilder = Lists.newLinkedList();
		}

		/**
		 * Adds a morphological token sequence to this sentence's sequences.
		 *
		 * @param sequence the morph token sequence to add
		 * @return the builder
		 */
		public Builder addSequence(final MorphTokenSequence sequence) {
			sequencesBuilder.add(checkNotNull(sequence));
			return this;
		}

		/**
		 * Adds morphological token sequences from an iterable to this sentence's sequences.
		 *
		 * @param sequence an iterable of the morph token sequences to add
		 * @return the builder
		 */
		public Builder addAllSequences(final Iterable<MorphTokenSequence> sequence) {
			// Use ImmutableList to broker between Iterable and Collection
			sequencesBuilder.addAll(ImmutableList.copyOf(checkNotNull(sequence)));
			return this;
		}

		/**
		 * Creates a {@link MorphSentence}.
		 *
		 * @return the morphological sentence
		 */
		public MorphSentence build() {
			final ImmutableList<MorphTokenSequence> sequences = ImmutableList.copyOf(sequencesBuilder);
			checkArgument(!sequences.isEmpty(),
					"token sequences must be added before the sentence can be built");
			return new MorphSentence(sequences);
		}
	}
}
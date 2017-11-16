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
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import adept.metadata.SourceAlgorithm;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;

/**
 * Represents a sequence of morphological tokens.
 */
@Beta
public final class MorphTokenSequence implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The tokens in this sequence.
	 */
	private final ImmutableList<MorphToken> tokens;

	/**
	 * The confidence for this sequence. A reference type is used so that null may represent absence without the
	 * overhead of storing an {@link Optional}.
	 */
	private final Float confidence;

	/**
	 * The algorithm that generated this object.
	 */
	private final SourceAlgorithm sourceAlgorithm;

	private MorphTokenSequence(final ImmutableList<MorphToken> tokens, final Float confidence,
			final SourceAlgorithm sourceAlgorithm) {
		this.tokens = tokens;
		this.confidence = confidence;
		this.sourceAlgorithm = sourceAlgorithm;
	}

	/**
	 * Creates a new builder.
	 *
	 * @param sourceAlgorithm the algorithm used to generate this object
	 * @return the builder
	 */
	public static Builder builder(final SourceAlgorithm sourceAlgorithm) {
		return new Builder(sourceAlgorithm);
	}

	/**
	 * Returns the morphological tokens in this sequence.
	 *
	 * @return the morphological tokens
	 */
	public ImmutableList<MorphToken> tokens() {
		return tokens;
	}

	/**
	 * Returns the confidence for this sequence.
	 *
	 * @return the confidence
	 */
	public Optional<Float> confidence() {
		return Optional.fromNullable(confidence);
	}

	/**
	 * Returns the source algorithm that generated this sequence.
	 *
	 * @return the source algorithm
	 */
	public SourceAlgorithm sourceAlgorithm() {
		return sourceAlgorithm;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final MorphTokenSequence that = (MorphTokenSequence) o;
		return Objects.equal(this.tokens, that.tokens) &&
				Objects.equal(this.confidence, that.confidence) &&
				Objects.equal(this.sourceAlgorithm, that.sourceAlgorithm);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(tokens, confidence, sourceAlgorithm);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("tokens", tokens)
				.add("confidence", confidence)
				.add("sourceAlgorithm", sourceAlgorithm)
				.toString();
	}

	/**
	 * Builds {@link MorphTokenSequence} objects.
	 */
	public static final class Builder {

		private final List<MorphToken> tokenBuilder;
		// A reference type is used and is passed as-is to the built object, which uses null for absence.
		private Float confidence;
		private final SourceAlgorithm sourceAlgorithm;

		private Builder(final SourceAlgorithm sourceAlgorithm) {
			this.sourceAlgorithm = checkNotNull(sourceAlgorithm);
			this.tokenBuilder = Lists.newLinkedList();
		}

		private Builder() {
			// no args constructor for Dozer
			tokenBuilder = Lists.newArrayList();
			sourceAlgorithm = null;
		}

		/**
		 * Adds a token to this sequence.
		 *
		 * @param token the token to add
		 * @return the builder
		 */
		public Builder addToken(final MorphToken token) {
			tokenBuilder.add(checkNotNull(token));
			return this;
		}

		/**
		 * Adds tokens from an iterable to this sequence.
		 *
		 * @param tokens and iterable of the tokens to add
		 * @return the builder
		 */
		public Builder addAllTokens(final Iterable<MorphToken> tokens) {
			// Use ImmutableList to broker between Iterable and Collection
			tokenBuilder.addAll(ImmutableList.copyOf(checkNotNull(tokens)));
			return this;
		}

		/**
		 * Set the confidence for this sequence.
		 *
		 * @param confidence the confidence
		 * @return the builder
		 */
		public Builder setConfidence(final float confidence) {
			this.confidence = confidence;
			return this;
		}

		/**
		 * Creates a {@link MorphTokenSequence}.
		 *
		 * @return the morphological token sequence
		 */
		public MorphTokenSequence build() {
			final ImmutableList<MorphToken> builtTokens = ImmutableList.copyOf(tokenBuilder);
			checkArgument(!builtTokens.isEmpty(),
					"tokens must be added before the token sequence can be built");
			return new MorphTokenSequence(builtTokens, confidence, sourceAlgorithm);
		}
	}
}

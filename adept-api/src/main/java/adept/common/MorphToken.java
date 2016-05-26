/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.common;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a morphological token.
 */
@Beta
public final class MorphToken {

	/**
	 * The tokenstream that this token is based on.
	 */
	private final TokenStream tokenStream;
	/**
	 * The original text of the token.
	 */
	private final String text;
	/**
	 * The lemma for this token.
	 */
	private final String lemma;
	/**
	 * The confidence for this token.
	 */
	private final Float confidence;
	/**
	 * The roots present in this token.
	 */
	private final ImmutableList<String> roots;
	/**
	 * The morphs present in this token.
	 */
	private final ImmutableList<Morph> morphs;
	/**
	 * The features expressed by this token.
	 */
	private final ImmutableSet<MorphFeature> features;
	/**
	 * The offset of the primary {@link Token} that this token corresponds to.
	 */
	private final TokenOffset headTokenOffset;
	/**
	 * The offsets of all {@link Token}s that this token corresponds to.
	 */
	private final ImmutableList<TokenOffset> tokenOffsets;
	/**
	 * The part of speech of this token.
	 */
	private final String pos;
	/**
	 * Any extra information to be stored with this object.
	 */
	private final String notes;

	/**
	 * Creates a new instance. All arguments may be null except {@code text} and {@code tokenStream};
	 * when accessed, null fields will be returned as {@link Optional#absent()}. The constructor
	 * does not perform null checks but leaves them to the builder to support the thrift mappings.
	 */
	private MorphToken(final String text, final TokenStream tokenStream, final String lemma,
			final Float confidence, final String pos, final ImmutableList<String> roots,
			final ImmutableList<Morph> morphs, final ImmutableSet<MorphFeature> features,
			final TokenOffset headTokenOffset, final ImmutableList<TokenOffset> tokenOffsets,
			final String notes) {
		this.text = text;
		this.tokenStream = tokenStream;
		this.lemma = lemma;
		this.confidence = confidence;
		this.pos = pos;
		this.roots = roots;
		this.morphs = morphs;
		this.features = features;
		this.headTokenOffset = headTokenOffset;
		this.tokenOffsets = tokenOffsets;
		this.notes = notes;
	}

	/**
	 * Creates a new builder for the specified text and token stream.
	 *
	 * @param text the text for this morphological token
	 * @param tokenStream the token stream that this morphological token was generated from
	 * @return the builder
	 */
	public static Builder builder(final String text, final TokenStream tokenStream) {
		return new Builder(text, tokenStream);
	}

	/**
	 * Returns the token stream that this token is based on.
	 *
	 * @return the token stream
	 */
	public TokenStream tokenStream() {
		return tokenStream;
	}

	/**
	 * Returns the source text for this token.
	 *
	 * @return the text
	 */
	public String text() {
		return text;
	}

	/**
	 * Returns the lemma.
	 *
	 * @return the lemma
	 */
	public Optional<String> lemma() {
		return Optional.fromNullable(lemma);
	}

	/**
	 * Returns the confidence in the analysis of this token.
	 *
	 * @return the confidence
	 */
	public Optional<Float> confidence() {
		return Optional.fromNullable(confidence);
	}

	/**
	 * Returns the roots present in this token.
	 *
	 * @return the roots
	 */
	public Optional<ImmutableList<String>> roots() {
		return Optional.fromNullable(roots);
	}

	/** Returns the morphs present in this token.
	 *
	 * @return the morphs
	 */
	public Optional<ImmutableList<Morph>> morphs() {
		return Optional.fromNullable(morphs);
	}

	/**
	 * Returns the features expressed by this token.
	 *
	 * @return the features
	 */
	public Optional<ImmutableSet<MorphFeature>> features() {
		return Optional.fromNullable(features);
	}

	/**
	 * Returns the offset of the primary {@link Token} that this token corresponds to.
	 *
	 * @return the offset of the primary token
	 */
	public Optional<TokenOffset> headTokenOffset() {
		return Optional.fromNullable(headTokenOffset);
	}

	/**
	 * Returns the offsets of all {@link Token}s that this token corresponds to.
	 *
	 * @return the offsets of the tokens
	 */
	public Optional<ImmutableList<TokenOffset>> tokenOffsets() {
		return Optional.fromNullable(tokenOffsets);
	}

	/**
	 * Returns the part of speech of this token.
	 *
	 * @return the part of speech
	 */
	public Optional<String> pos() {
		return Optional.fromNullable(pos);
	}

	/**
	 * Returns notes stored in this token. It is up to the producer and consumer to agree on the
	 * interpretation of the contents of this field.
	 *
	 * @return the notes
	 */
	public Optional<String> notes() {
		return Optional.fromNullable(notes);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o){
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final MorphToken that = (MorphToken) o;
		return Objects.equal(this.tokenStream, that.tokenStream) &&
				Objects.equal(this.text, that.text) &&
				Objects.equal(this.lemma, that.lemma) &&
				Objects.equal(this.confidence, that.confidence) &&
				Objects.equal(this.roots, that.roots) &&
				Objects.equal(this.morphs, that.morphs) &&
				Objects.equal(this.features, that.features) &&
				Objects.equal(this.headTokenOffset, that.headTokenOffset) &&
				Objects.equal(this.tokenOffsets, that.tokenOffsets) &&
				Objects.equal(this.pos, that.pos) &&
				Objects.equal(this.notes, that.notes);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(tokenStream, text, lemma, confidence, roots, morphs,
				features, headTokenOffset, tokenOffsets, pos, notes);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("tokenStream", tokenStream)
				.add("text", text)
				.add("lemma", lemma)
				.add("confidence", confidence)
				.add("roots", roots)
				.add("morphs", morphs)
				.add("features", features)
				.add("headTokenOffset", headTokenOffset)
				.add("tokenOffsets", tokenOffsets)
				.add("pos", pos)
				.add("notes", notes)
				.toString();
	}

	/**
	 * Builds {@link MorphToken} instances.
	 */
	public static final class Builder {

		// These fields are mandatory
		private final String text;
		private final TokenStream tokenStream;
		// These are all optional and thus may be null. For container types, a distinction is
		// maintained between null (absent) and empty (known to be empty). A reference type is used for
		// confidence to allow it to be null.
		private String lemma;
		private Float confidence;
		private List<String> roots;
		private List<Morph> morphs;
		private Set<MorphFeature> features;
		private TokenOffset headTokenOffset;
		private List<TokenOffset> tokenOffsets;
		private String pos;
		private String notes;

		private Builder() {
			// no args constructor for dozer
			text = null;
			tokenStream = null;
		}

		private Builder(final String text, final TokenStream tokenStream) {
			this.text = checkNotNull(text);
			this.tokenStream = checkNotNull(tokenStream);
		}

		/**
		 * Sets the lemma.
		 *
		 * @param lemma the lemma
		 * @return the builder
		 */
		public Builder setLemma(final String lemma) {
			this.lemma = checkNotNull(lemma);
			return this;
		}

		/**
		 * Sets the confidence.
		 *
		 * @param confidence the confidence
		 * @return the builder
		 */
		public Builder setConfidence(final float confidence) {
			this.confidence = confidence;
			return this;
		}

		/**
		 * Sets the roots.
		 *
		 * @param roots the roots
		 * @return the builder
		 */
		public Builder setRoots(final Iterable<String> roots) {
			this.roots = ImmutableList.copyOf(checkNotNull(roots));
			return this;
		}

		/**
		 * Sets the morphs.
		 *
		 * @param morphs the morphs
		 * @return the builder
		 */
		public Builder setMorphs(final Iterable<Morph> morphs) {
			this.morphs = ImmutableList.copyOf(checkNotNull(morphs));
			return this;
		}

		/**
		 * Sets the features.
		 *
		 * @param features the features
		 * @return the builder
		 */
		public Builder setFeatures(final Iterable<MorphFeature> features) {
			this.features = ImmutableSet.copyOf(checkNotNull(features));
			return this;
		}

		/**
		 * Sets the offset of the head token. The offset must represent a single token.
		 *
		 * @param headTokenOffset the offset of the head token
		 * @return the builder
		 */
		public Builder setHeadToken(final TokenOffset headTokenOffset) {
			checkNotNull(headTokenOffset);
			checkArgument(headTokenOffset.getBegin() == headTokenOffset.getEnd(),
					"Head token offset must correspond to a single token");
			this.headTokenOffset = headTokenOffset;
			return this;
		}

		/**
		 * Sets the head token and the token offsets to the single provided token offset. This is
		 * a convenience method for the common case where a morphological token has a head token
		 * and it is the only token it is connected with. The offset must represent a single token.
		 *
		 * @param headTokenOffset the offset of the head token
		 * @return the builder
		 */
		public Builder setHeadTokenAndTokenOffsets(final TokenOffset headTokenOffset) {
			setHeadToken(headTokenOffset);
			setTokenOffsets(ImmutableList.of(headTokenOffset));
			return this;
		}

		/**
		 * Sets the token offsets.
		 *
		 * @param tokenOffsets the token offsets of the tokens related to this morphological token
		 * @return the builder
		 */
		public Builder setTokenOffsets(final Iterable<TokenOffset> tokenOffsets) {
			this.tokenOffsets = ImmutableList.copyOf(checkNotNull(tokenOffsets));
			return this;
		}

		/**
		 * Sets the part of speech.
		 *
		 * @param pos the part of speech
		 * @return the builder
		 */
		public Builder setPos(final String pos) {
			this.pos = checkNotNull(pos);
			return this;
		}

		/** Sets the notes.
		 *
		 * @param notes the notes
		 * @return the builder
		 */
		public Builder setNotes(String notes) {
			this.notes = checkNotNull(notes);
			return this;
		}

		/**
		 * Creates a new {@link MorphToken} instance.
		 *
		 * @return the morph token
		 */
		public MorphToken build() {
			// We use the awkward immutable*OrNull to make things easier for thrift conversion. Otherwise,
			// the original builder fields would have been Immutable* or null, which is what the
			// constructor is expecting.
			return new MorphToken(text, tokenStream, lemma, confidence, pos, immutableListOrNull(roots),
					immutableListOrNull(morphs), immutableSetOrNull(features), headTokenOffset,
					immutableListOrNull(tokenOffsets), notes);
		}
	}

	static <E> ImmutableList<E> immutableListOrNull(final List<E> list) {
		return list != null ? ImmutableList.copyOf(list) : null;
	}

	static <E> ImmutableSet<E> immutableSetOrNull(final Set<E> set) {
		return set != null ? ImmutableSet.copyOf(set) : null;
	}
}

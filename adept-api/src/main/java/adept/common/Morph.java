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

import static adept.common.MorphToken.immutableListOrNull;
import static adept.common.MorphToken.immutableSetOrNull;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a morph.
 */
@Beta
public final class Morph {

	/**
	 * The form of the morph, which may match the source text or be a more abstract form.
	 */
	private final String form;
	/**
	 * The type of the morph.
	 */
	private final MorphType morphType;
	/**
	 * Features expressed by this morph.
	 */
	private final ImmutableSet<MorphFeature> features;
	/**
	 * The character offsets representing the source text for this morph.
	 */
	private final ImmutableList<CharOffset> sourceOffsets;

	/**
	 * Creates a morph.
	 *
	 * @param form      the form
	 * @param morphType the type of morpheme
	 * @param features  the features expressed, which may be {@code null}.
	 * @param sourceOffsets the character offsets representing the source text for this morph, which
	 *                      may be {@code null}.
	 */
	private Morph(final String form, final MorphType morphType,
		  final ImmutableSet<MorphFeature> features, final ImmutableList<CharOffset> sourceOffsets) {
		this.form = form;
		this.morphType = morphType;
		this.features = features;
		this.sourceOffsets = sourceOffsets;
	}

	/**
	 * Creates a builder for a morph with the specified form and type.
	 *
	 * @param form the form
	 * @param morphType the morph type
	 * @return the builder
	 */
	public static Builder builder(final String form, final MorphType morphType) {
		return new Builder(form, morphType);
	}

	/**
	 * Returns the form of the morph.
	 *
	 * @return the form of the morph
	 */
	public String form() {
		return form;
	}
	/**
	 * Returns the type of the morph.
	 *
	 * @return the type of the morph
	 */
	public MorphType morphType() {
		return morphType;
	}

	/**
	 * Returns the features expressed by this morph.
	 *
	 * @return the features
	 */
	public Optional<ImmutableSet<MorphFeature>> features() {
		return Optional.fromNullable(features);
	}

	/**
	 * Returns the character offsets representing the source text for this morph.
	 *
	 * @return the offsets
	 */
	public Optional<ImmutableList<CharOffset>> sourceOffsets() {
		return Optional.fromNullable(sourceOffsets);
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Morph that = (Morph) o;
		return Objects.equal(this.form, that.form) &&
				Objects.equal(this.morphType, that.morphType) &&
				Objects.equal(this.features, that.features) &&
				Objects.equal(this.sourceOffsets, that.sourceOffsets);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(form, morphType, features, sourceOffsets);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("form", form)
				.add("morphType", morphType)
				.add("features", features)
				.add("sourceOffsets", sourceOffsets)
				.toString();
	}

	/**
	 * Builds {@link Morph} instances.
	 */
	public static final class Builder {

		private final String form;
		private final MorphType morphType;
		private Set<MorphFeature> features;
		private List<CharOffset> sourceOffsets;

		private Builder() {
			// no args constructor for dozer
			form = null;
			morphType = null;
		}

		private Builder(final String form, final MorphType morphType) {
			this.form = checkNotNull(form);
			this.morphType = checkNotNull(morphType);
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
		 * Sets the source offsets. Multiple source offsets are supported in the case of a morph that
		 * represents non-continuous characters in the source (e.g., a circumfix, a consonant template).
		 * If only a single source offset is desired, {@link #setSingleSourceOffset} is more
		 * convenient.
		 *
		 * @param offsets the source offsets
		 * @return the builder
		 */
		public Builder setSourceOffsets(final Iterable<CharOffset> offsets) {
			this.sourceOffsets = ImmutableList.copyOf(checkNotNull(offsets));
			return this;
		}

		/**
		 * Sets a source offset as the sole source offset for this morph. This is a convenience method
		 * for the common case where a morph token is associated with a single character offset.
		 *
		 * @param offset the source offset
		 * @return the builder
		 */
		public Builder setSingleSourceOffset(final CharOffset offset) {
			return setSourceOffsets(ImmutableList.of(offset));
		}

		/**
		 * Creates the {@link Morph} instance.
		 *
		 * @return the morph
		 */
		public Morph build() {
			// We use the awkward immutable*OrNull to make things easier for thrift conversion. Otherwise,
			// the original builder fields would have been Immutable* or null, which is what the
			// constructor is expecting.
			return new Morph(form, morphType, immutableSetOrNull(features), immutableListOrNull(sourceOffsets));
		}
	}
}

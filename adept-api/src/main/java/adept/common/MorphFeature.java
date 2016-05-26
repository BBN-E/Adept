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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a morphological feature.
 */
@Beta
public final class MorphFeature {

	/**
	 * The property expressed.
	 */
	private final String property;
	/**
	 * The value of that property.
	 */
	private final String value;

	private MorphFeature() {
		// no args constructor for Dozer
		property = null;
		value = null;
	}

	private MorphFeature(final String property, final String value) {
		this.property = property;
		this.value = value;
	}

	/**
	 * Creates a new morphological feature instance.
	 *
	 * @param property the property it encodes
	 * @param value the value of that property
	 * @return the morphological feature instance
	 */
	public static MorphFeature create(final String property, final String value) {
		return new MorphFeature(checkNotNull(property), checkNotNull(value));
	}

	/**
	 * Returns the property expressed.
	 *
	 * @return the property
	 */
	public String property() {
		return property;
	}

	/**
	 * Returns the value of the property.
	 *
	 * @return the value
	 */
	public String value() {
		return value;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final MorphFeature that = (MorphFeature) o;
		return Objects.equal(this.property, that.property) &&
				Objects.equal(this.value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(property, value);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("property", property)
				.add("value", value)
				.toString();
	}
}

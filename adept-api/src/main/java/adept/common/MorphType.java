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

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;

/**
 * Represents the types of morphs.
 */
@Beta
public class MorphType implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String type;

	private MorphType() {
		// private no args constructor for dozer mappings
		this.type = null;
	}

	private MorphType(final String type) {
		this.type = type;
	}

	/**
	 * Creates a new instance.
	 *
	 * @param type the type of the morpheme
	 * @return the instance
	 */
	public static MorphType create(final String type) {
		return new MorphType(checkNotNull(type));
	}

	/**
	 * Returns the morph type.
	 *
	 * @return the morph type
	 */
	public String type(){
		return type;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final MorphType that = (MorphType) o;
		return Objects.equal(this.type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(type);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("type", type)
				.toString();
	}
}

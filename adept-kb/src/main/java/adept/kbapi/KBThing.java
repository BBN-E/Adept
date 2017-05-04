package adept.kbapi;

/*-
 * #%L
 * adept-kb
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

import java.util.Set;

import adept.common.KBID;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * 
 * @author dkolas
 */
public abstract class KBThing extends KBPredicateArgument {

	private final String canonicalString;

	/**
	 * @param kbID
	 * @param provenances
	 */
	protected KBThing(KB kb, KBID kbID, Optional<Set<KBProvenance>> provenances, String canonicalString) {
		super(kb, kbID, provenances);
		this.canonicalString = canonicalString;
	}

	public String getCanonicalString() {
		return canonicalString;
	}

	/**
	 * Get an UpdateBuilder for this object which will provide allowed
	 * modifications.
	 * 
	 * @return
	 */
	public abstract UpdateBuilder<? extends UpdateBuilder<?, ?>, ? extends KBPredicateArgument> updateBuilder();

	@Override
	public boolean equals(Object o) {
		if (null == o || !(getClass() == o.getClass())) {
			return false;
		}
		KBThing that = (KBThing) o;
		return super.equals(that) && Objects.equal(that.canonicalString, this.canonicalString);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), canonicalString);
	}
}

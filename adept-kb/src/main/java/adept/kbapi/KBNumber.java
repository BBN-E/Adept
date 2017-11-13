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

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adept.common.KBID;
import adept.common.NumberPhrase;
import adept.common.NumericValue;

/**
 * This class represents numbers in the KB.
 *
 * Numbers are assumed to be canonical, so they do not have confidences.
 *
 * There should only be one copy of any given number in the KB at any time.
 *
 * @author dkolas
 */
public class KBNumber extends KBThing {

	/**
	 * The number value
	 */
	private final Number number;

	/**
	 * Internal constructor
	 *
	 * @param kbID
	 * @param provenances
	 * @param number
	 */
	private KBNumber(KB kb, KBID kbID, Optional<Set<KBProvenance>> provenances, Number number) {
		super(kb, kbID, provenances, number.toString());
		this.number = number;
	}

	/**
	 * Get the numeric value as a {@link Number}
	 *
	 * @return
	 */
	public Number getNumber() {
		return number;
	}

	/**
	 * Create a new insertion builder with a {@link Number} object. Provenances
	 * can then be added to the builder.
	 *
	 * @param number
	 * @return
	 */
	public static InsertionBuilder numberInsertionBuilder(Number number) {
		return new InsertionBuilder(number);
	}

	/**
	 * Create a new insertion builder with an existing NumericValue and list of
	 * number phrases. The numeric value will be used to determine the number
	 * value, and the numberPhrases will be used to add provenance.
	 *
	 * @param numericValue
	 * @param numberPhrases
	 * @return
	 */
	public static InsertionBuilder numberInsertionBuilder(NumericValue numericValue,
			List<NumberPhrase> numberPhrases) {
		InsertionBuilder builder = new InsertionBuilder(numericValue.asNumber());
		for (NumberPhrase numberPhrase : numberPhrases) {
			builder.addProvenance(KBTextProvenance.builder(numberPhrase, 1f));
		}
		return builder;
	}

	/**
	 * Class defines the InsertionBuilder for a KBNumber object.
	 *
	 * Only the addition of provenances is supported.
	 *
	 * @author dkolas
	 */
	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilder<InsertionBuilder, KBNumber> {

		/**
		 * Number value
		 */
		private Number number;

		public Number getNumber() {
			return number;
		}

		/**
		 * Private constructor, to be called by KBNumber.insertionBuilder
		 * methods
		 *
		 * @param number
		 */
		private InsertionBuilder(Number number) {
			this.number = number;
		}

		/**
		 * Insert method for saving the KBNumber in the KB.
		 *
		 * @param kb
		 * @return
		 * @throws adept.kbapi.KBUpdateException
		 *
		 */
		@Override
		public KBNumber insert(KB kb) throws KBUpdateException {
			kb.insertNumber(this);
			return build(kb, false);
		}

		protected KBNumber build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBNumber(kb, kbid, provenances, number);
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.InsertionBuilder#me()
		 */
		@Override
		protected InsertionBuilder me() {
			return this;
		}
	}

	/**
	 * Update builder for KBNumber objects. Provenances may be added or removed.
	 *
	 * @return
	 *
	 */
	@Override
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder(this);
	}

	/**
	 * Update builder for KBNumber objects. Provenances may be added or removed.
	 *
	 * @author dkolas
	 */
	public class UpdateBuilder extends KBPredicateArgument.UpdateBuilder<UpdateBuilder, KBNumber> {
		private KBNumber kbNumber = null;

		private UpdateBuilder(KBNumber kbNumber) {
			this.kbNumber = kbNumber;
		}

		@Override
		public KBNumber update(KB kb) throws KBUpdateException {
			Set<KBProvenance> oldProvenances = null;
			try{
				oldProvenances = getProvenances();
				checkProvenancesToRemove();
			}catch(KBQueryException e){
				throw new KBUpdateException("Could not load provenances for original object",e);
			}
			kb.updateKBPredicateArgumentProvenances(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : oldProvenances) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBNumber(kb, kbNumber.getKBID(), Optional.of(provenances), kbNumber.getNumber());
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.UpdateBuilder#me()
		 */
		@Override
		protected UpdateBuilder me() {
			return this;
		}

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBNumber)) {
			return false;
		}
		KBNumber that = (KBNumber) o;
		return super.equals(that) && Objects.equal(that.number, this.number);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), number);
	}
}
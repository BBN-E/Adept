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

package adept.kbapi;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

import java.util.Set;

import adept.common.KBID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class KBOpenIERelation represents a fully loaded, immutable OpenIE relation.
 * The {@code KBID} corresponds to the openIERelationID in the OpenIDRelations Postgres table.
 *
 * @author dakodes, msrivast
 */
public class KBOpenIERelation extends KBPredicateArgument{

  /**
   *	Textual predicate of this relation
   */
  	private final String predicate;

	/**
	 * The confidence that this relation is correct.
	 */
	private final float confidence;

	/**
	 * The KBID of the first argument.
	 */
	private final Optional<KBOpenIEArgument> arg1;

	/**
	 * The KBID of the second argument.
	 */
	private final Optional<KBOpenIEArgument> arg2;

	public String getPredicate() {
    		return predicate;
  	}

	/**
	 * @return The confidence that this relation is correct.bestProvenance().get();
	 */
	public float getConfidence() {
		return confidence;
	}

//	/**
//	 * @return The predicate provenance of this relation.
//	 */
//	public KBTextProvenance getPredicateProvenance() throws KBQueryException {
//		Optional<KBTextProvenance> predicateProvenancePerhaps = bestProvenance();
//		if (!predicateProvenancePerhaps.isPresent()) {
//			throw new KBQueryException("Provenance missing from KBOpenIERelation");
//		}
//		return predicateProvenancePerhaps.get();
//	}

	/**
	 * @return The first argument of this relation.
	 */
	public Optional<KBOpenIEArgument> getArg1() {
		return arg1;
	}

	/**
	 * @return The second argument of this relation.
	 */
	public Optional<KBOpenIEArgument> getArg2() {
		return arg2;
	}

	//////

	/**
	 * Private constructor, intended to be called only by builders.
	 * @param kb
	 * @param kbID
	 * @param arg1
	 * @param arg2
	 * @param confidence
	 * @param provenances
	 */
	private KBOpenIERelation(KB kb, KBID kbID, String predicate, Optional<KBOpenIEArgument> arg1,
	    Optional<KBOpenIEArgument> arg2, float confidence, Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, provenances);
	  	this.predicate = predicate;
		this.arg1 = checkNotNull(arg1);
		this.arg2 = checkNotNull(arg2);
		this.confidence = confidence;
	}

	/**
	 * Create a new builder, given the predicate. Arguments, confidence and a displayability score
	 * can be
	 * added later.
	 * @return
	 */
	public static InsertionBuilder insertionBuilder(String predicate) {
		return new InsertionBuilder(predicate);
	}

	/**
	 * Class defines the InsertionBuilder for a KBOpenIERelation object.
	 *
	 * Only the addition of provenances is supported.
	 *
	 * @author dakodes
	 */
	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilder<KBOpenIERelation.InsertionBuilder, KBOpenIERelation> {

	  	private final String predicate;
		private float confidence;
		private Optional<KBOpenIEArgument> arg1 = Optional.absent();
		private Optional<KBOpenIEArgument> arg2 = Optional.absent();

		public String getPredicate(){
		  return predicate;
		}

	  	public Optional<KBOpenIEArgument> getArg1() {
			return arg1;
		}

		public Optional<KBOpenIEArgument> getArg2() {
			return arg2;
		}

		public float getConfidence() {
			return confidence;
		}

		/**
		 * Private constructor, to be called by KBOpenIERelation.insertionBuilder.
		 * methods
		 */
		private InsertionBuilder(String predicate) {
			this.predicate = predicate;
		}

		/**
		 * @param arg1 The (optional) first argument of the KBOpenIERelation to be built.
		 */
		public InsertionBuilder addArg1(KBOpenIEArgument arg1) {
			this.arg1 = Optional.fromNullable(arg1);
			return me();
		}

		/**
		 * @param arg2 The (optional) second argument of the KBOpenIERelation to be built.
		 */
		public InsertionBuilder addArg2(KBOpenIEArgument arg2) {
			this.arg2 = Optional.fromNullable(arg2);
			return me();
		}

	  /**
	   * @param confidence The confidence value of the KBOpenIERelation to be
	   *                        built.
	   */
	  public InsertionBuilder setConfidence(float confidence) {
	    this.confidence = confidence;
	    return me();
	  }

		protected KBOpenIERelation build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBOpenIERelation(kb, kbid, predicate, arg1, arg2,
			    confidence, provenances);
		}

		/**
		 * Insert method for saving the KBOpenIERelation in the KB.
		 *
		 * @param kb
		 * @throws adept.kbapi.KBUpdateException
		 *
		 * @see adept.kbapi.KBPredicateArgument.InsertionBuilder#insert(adept.kbapi.KB)
		 */
		@Override
		public KBOpenIERelation insert(KB kb) throws KBUpdateException {
			kb.insertOpenIERelation(this);// TODO
			return build(kb, false);
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.InsertionBuilder#me()
		 */
		@Override
		protected KBOpenIERelation.InsertionBuilder me() {
			return this;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBOpenIERelation)) {
			return false;
		}
		KBOpenIERelation that = (KBOpenIERelation) o;
		return super.equals(that)
		    		&& Objects.equal(that.predicate,this.predicate)
				&& Objects.equal(that.arg1, this.arg1)
				&& Objects.equal(that.arg2, this.arg2)
				&& that.confidence == this.confidence;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), predicate, arg1, arg2,
		    confidence);
	}
}
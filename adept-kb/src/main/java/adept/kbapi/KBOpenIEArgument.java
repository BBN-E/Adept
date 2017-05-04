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

/**
 * The Class KBOpenIERelation represents a fully loaded, immutable OpenIE relation.
 * The {@code KBID} corresponds to the openIERelationID in the OpenIDRelations Postgres table.
 *
 * @author dakodes, msrivast
 */
public class KBOpenIEArgument extends KBPredicateArgument{

  	/**
   	*	Textual value for this argument
   	*/
  	private String value;

	/**
	 * The confidence value for this argument
	 */
	private final float confidence;

  	public String getValue() {
    		return value;
  	}

	public float getConfidence() {
		return confidence;
	}

	/**
	 * Private constructor, intended to be called only by builders.
	 * @param kb
	 * @param kbID
	 * @param value
	 * @param confidence
	 * @param provenances
	 */
	private KBOpenIEArgument(KB kb, KBID kbID, String value,
	    float confidence,
	    Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, provenances);
	  	this.value = value;
		this.confidence = confidence;
	}

	/**
	 * Create a new builder, given the value.
	 * @return
	 */
	public static InsertionBuilder insertionBuilder(String value) {
		return new InsertionBuilder(value);
	}

	/**
	 * Class defines the InsertionBuilder for a KBOpenIEArgument object.
	 *
	 * Only the addition of provenances is supported.
	 *
	 * @author msrivast
	 */
	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilder<KBOpenIEArgument.InsertionBuilder, KBOpenIEArgument> {

	  	private final String value;
		private float confidence;

	  	public String getValue(){
	   		return value;
	  	}


		public float getConfidence() {
			return confidence;
		}

		/**
		 * Private constructor, to be called by KBOpenIERelation.insertionBuilder.
		 * methods
		 */
		private InsertionBuilder(String value) {
			this.value = value;
		}

	  /**
	   * @param confidence The confidence value of the KBOpenIERelation to be
	   *                        built.
	   */
	  public InsertionBuilder setConfidence(float confidence) {
	    this.confidence = confidence;
	    return me();
	  }

		protected KBOpenIEArgument build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBOpenIEArgument(kb, kbid, value,
			    confidence, provenances);
		}

		/**
		 * Insert method for saving the KBOpenIERelation in the KB.
		 *
		 * @param kb
		 * @throws KBUpdateException
		 *
		 * @see KBPredicateArgument.InsertionBuilder#insert(KB)
		 */
		@Override
		public KBOpenIEArgument insert(KB kb) throws KBUpdateException {
			kb.insertOpenIEArgument(this);// TODO
			return build(kb, false);
		}

		/**
		 *
		 * @return
		 *
		 * @see KBPredicateArgument.InsertionBuilder#me()
		 */
		@Override
		protected KBOpenIEArgument.InsertionBuilder me() {
			return this;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBOpenIEArgument)) {
			return false;
		}
		KBOpenIEArgument that = (KBOpenIEArgument) o;
		return super.equals(that)
				&& Objects.equal(that.value, this.value)
				&& that.confidence == this.confidence;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), value,
		    confidence);
	}
}

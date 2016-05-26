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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import adept.common.KBID;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * Superclass for all KB types which can be the argument of any predicate.
 * Stores the KBID for the object, and provides nested superclasses for
 * subclasses' insertion and update builders.
 * 
 * All KBPredicateArguments are immutable.
 * 
 * @author dkolas
 */
public abstract class KBPredicateArgument {

	private final KBID kbID;

	/**
	 * Immutable set of provenances.
	 */
	private final ImmutableSet<KBProvenance> provenances;

	/**
	 * Internal constructor, to be called only by builders.
	 * 
	 * @param kbID
	 * @param provenances
	 */
	protected KBPredicateArgument(KBID kbID, Set<KBProvenance> provenances) {
		this.kbID = kbID;
		this.provenances = ImmutableSet.copyOf(Preconditions.checkNotNull(provenances));
	}

	/**
	 * Get the KBID of this object.
	 * 
	 * @return
	 */
	public KBID getKBID() {
		return kbID;
	}

	public Set<KBProvenance> getProvenances() {
		return provenances;
	}

	/**
	 * Default UpdateBuilder class, allows the addition and removal of
	 * provenances. Subclasses of {@link KBPredicateArgument} can have inner
	 * classes which inherit from this.
	 * 
	 * @author dkolas
	 */
	protected abstract class UpdateBuilder<BuilderType extends UpdateBuilder<BuilderType, PredicateArgumentType>, PredicateArgumentType extends KBPredicateArgument> {

		private Set<KBProvenance.InsertionBuilder> newProvenances;
		private Set<KBProvenance> provenancesToRemove;
        private Set<KBID> newExternalKBIDs;
        private Set<KBID> externalKBIDsToRemove;

		public Set<KBProvenance.InsertionBuilder> getNewProvenances() {
			return newProvenances;
		}

		public Set<KBProvenance> getProvenancesToRemove() {
			return provenancesToRemove;
		}
                
        public Set<KBID> getNewExternalKBIDs() {
            return newExternalKBIDs;
        }
                
        public Set<KBID> getExternalKBIDsToRemove() {
            return externalKBIDsToRemove;
        }

		/**
		 * Internal constructor, should be called from updateBuilder() methods
		 * on containing objects.
		 */
		protected UpdateBuilder() {
			newProvenances = new HashSet<KBProvenance.InsertionBuilder>();
			provenancesToRemove = new HashSet<KBProvenance>();
                        
            newExternalKBIDs = new HashSet<KBID>();
			externalKBIDsToRemove = new HashSet<KBID>();
		}

		/**
		 * Add a new provenance to this object. The new provenance builder will
		 * be saved to the KB when update is called.
		 * 
		 * @param provenance
		 * @return
		 */
		public BuilderType addProvenance(KBProvenance.InsertionBuilder provenance) {
			newProvenances.add(provenance);
			return me();
		}

		/**
		 * Remove a provenance from this object. The existing provenance will be
		 * removed from the KBPredicateArgument when update is called.
		 * 
		 * @param provenance
		 * @return
		 */
		public BuilderType removeProvenance(KBProvenance provenance) {
			Preconditions.checkArgument(provenances.contains(provenance),
					"This relation does not contain the given provenance.");
			provenancesToRemove.add(provenance);
			return me();
		}
                
         /**
		 * Add a new external KBID to this object. The new external KBID will
		 * be saved to the KB when update is called.
		 * 
		 * @param externalId
		 * @return
		 */
         public BuilderType addExternalKBID(KBID externalId) {
             newExternalKBIDs.add(externalId);
             return me();
         }
                
         /**
		 * Remove an external KBID from this object. The existing external KBID will be
		 * removed from the KBPredicateArgument when update is called.
		 * 
		 * @param externalId
		 * @return
		 */
         public BuilderType removeExternalKBID(KBID externalId) {
             externalKBIDsToRemove.add(externalId);
             return me();
         }

		public abstract KBID getKBID();

		public abstract KBPredicateArgument update(KB kb) throws KBUpdateException;

		protected abstract BuilderType me();
	}

	/**
	 * UpdateBuilder class which additionally allows the updating of the
	 * confidence value. Subclasses of {@link KBPredicateArgument} can have
	 * inner classes which inherit from this.
	 * 
	 * @author dkolas
	 */
	protected abstract class UpdateBuilderWithConfidence<BuilderType extends UpdateBuilderWithConfidence<BuilderType, PredicateArgumentType>, PredicateArgumentType extends KBPredicateArgument>
			extends UpdateBuilder<BuilderType, PredicateArgumentType> {

		private Float newConfidence;

		public Float getNewConfidence() {
			return newConfidence;
		}

		public BuilderType setConfidence(float newConfidence) {
			this.newConfidence = newConfidence;
			return me();
		}
	}

	/**
	 * Default InsertionBuilder class, which holds provenances that will be
	 * inserted when the object is inserted.
	 * 
	 * @author dkolas
	 */
	protected static abstract class InsertionBuilder<BuilderType extends InsertionBuilder<BuilderType, PredicateArgumentType>, PredicateArgumentType extends KBPredicateArgument>
			extends KBObjectBuilder {

		private Set<KBProvenance.InsertionBuilder> newProvenances;
		private Set<KBID> externalKBIds;

		public Set<KBProvenance.InsertionBuilder> getProvenances() {
			return newProvenances;
		}

		public Set<KBID> getExternalKBIds() {
			return externalKBIds;
		}

		protected abstract BuilderType me();

		/**
		 * Protected constructor
		 */
		protected InsertionBuilder() {
			newProvenances = new HashSet<KBProvenance.InsertionBuilder>();
			externalKBIds = new HashSet<KBID>();
		}

		/**
		 * Add a Provenance builder to this object. This provenance will be
		 * inserted into the KB when the object is inserted.
		 * 
		 * @param provenance
		 * @return
		 */
		public BuilderType addProvenance(KBProvenance.InsertionBuilder provenance) {
			newProvenances.add(provenance);
			return me();
		}

		/**
		 * Add a set of Provenance builders to this object. These provenances
		 * will be inserted into the KB when the object is inserted.
		 * 
		 * @param provenances
		 */
		public BuilderType addProvenances(
				Collection<? extends adept.kbapi.KBProvenance.InsertionBuilder> provenances) {
			newProvenances.addAll(provenances);
			return me();
		}

		public BuilderType addExternalKBId(KBID kbid) {
			externalKBIds.add(kbid);
			return me();
		}

		public BuilderType addExternalKBIds(Set<KBID> externalKbids) {
			externalKBIds.addAll(externalKbids);
			return me();
		}

		/**
		 * Insert this object into the given KB.
		 * 
		 * The returned object is immutable.
		 * 
		 * @param kb
		 * @return
		 */
		public abstract KBPredicateArgument insert(KB kb) throws KBUpdateException;
	}

	/**
	 * Extension to default InsertionBuilder for objects which include a
	 * confidence value.
	 * 
	 * @author dkolas
	 */
	protected static abstract class InsertionBuilderWithConfidence<BuilderType extends InsertionBuilderWithConfidence<BuilderType, PredicateArgumentType>, PredicateArgumentType extends KBPredicateArgument>
			extends InsertionBuilder<BuilderType, PredicateArgumentType> {

		private float confidence;

		public float getConfidence() {
			return confidence;
		}

		protected BuilderType setConfidence(float confidence) {
			this.confidence = confidence;
			return me();
		}

		protected InsertionBuilderWithConfidence(float confidence) {
			this.confidence = confidence;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBPredicateArgument)) {
			return false;
		}
		KBPredicateArgument that = (KBPredicateArgument) o;
		return that.kbID.equals(this.kbID) && Objects.equal(that.provenances, this.provenances);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(kbID, provenances);
	}
}

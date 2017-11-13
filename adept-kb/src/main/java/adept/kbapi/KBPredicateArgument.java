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

import adept.common.KBID;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

	protected final KBID kbID;
	private final KB kb;

	/**
	 * Immutable set of provenances.
	 */
	private ImmutableSet<KBProvenance> provenances = null;


	/**
	 * Internal constructor, to be called only by builders.
	 *
	 * @param kbID
	 * @param provenances
	 */
	protected KBPredicateArgument(KB kb, KBID kbID, Optional<Set<KBProvenance>> provenances) {
		this.kbID = kbID;
		if (provenances.isPresent()){
			this.provenances = ImmutableSet.copyOf(Preconditions.checkNotNull(provenances.get()));
		}
		this.kb = kb;
	}


	/**
	 * Get the KBID of this object.
	 *
	 * @return
	 */
	public KBID getKBID() {
		return kbID;
	}

	public Set<KBProvenance> getProvenances() throws KBQueryException{
		if (provenances == null){
			provenances = ImmutableSet.copyOf(kb.getProvenancesForObject(kbID));
		}
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
	  	private Set<KBProvenance.UpdateBuilder> provenancesToUpdate;
		private Set<KBProvenance> provenancesToRemove;
		private Set<KBID> newExternalKBIDs;
		private Set<KBID> externalKBIDsToRemove;

		public Set<KBProvenance.InsertionBuilder> getNewProvenances() {
			return newProvenances;
		}

	  	public Set<KBProvenance.UpdateBuilder> getProvenancesToUpdate(){
		  	return provenancesToUpdate;
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
		  	provenancesToUpdate = new HashSet<KBProvenance.UpdateBuilder>();
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
	   	* Link an existing provenance to this object. The updated state of the provenance
		 * will be saved to the KB when update is called. This method will be called when
		 * merging multiple KB objects, so that their provenances can be re-linked with the
		 * merged KB object.
	   	*
	   	* @param provenance
	   	* @return
	   	*/
	  	public BuilderType addProvenanceToUpdate(KBProvenance.UpdateBuilder provenance){
	    		provenancesToUpdate.add(provenance);
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

			provenancesToRemove.add(provenance);
			return me();
		}

		protected void checkProvenancesToRemove() throws KBQueryException{
			for (KBProvenance provenance : provenancesToRemove){
				Preconditions.checkArgument(getProvenances().contains(provenance),
						"This object does not contain the given provenance to remove: "+provenance);
			}
		}

		/**
		 * Add a new external KBID to this object. The new external KBID will be
		 * saved to the KB when update is called.
		 *
		 * @param externalId
		 * @return
		 */
		public BuilderType addExternalKBID(KBID externalId) {
			newExternalKBIDs.add(externalId);
			return me();
		}

		/**
		 * Remove an external KBID from this object. The existing external KBID
		 * will be removed from the KBPredicateArgument when update is called.
		 *
		 * @param externalId
		 * @return
		 */
		public BuilderType removeExternalKBID(KBID externalId) {
			externalKBIDsToRemove.add(externalId);
			return me();
		}

		public abstract PredicateArgumentType update(KB kb) throws KBUpdateException;

		protected abstract BuilderType me();

		public KBID getKBID() {
			return kbID;
		}
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

		protected Optional<Set<KBProvenance>> buildProvenances(boolean deferProvenances){
			if (deferProvenances){
				return Optional.<Set<KBProvenance>>absent();
			}else{
				Set<KBProvenance> provenances = null;
				provenances = new HashSet<KBProvenance>();
				for (KBProvenance.InsertionBuilder provenanceBuilder : getProvenances()) {
					provenances.add(provenanceBuilder.build());
				}
				return Optional.of(provenances);
			}
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
		if (null == o || !(getClass() == o.getClass())) {
			return false;
		}
		KBPredicateArgument that = (KBPredicateArgument) o;
		return that.kbID.equals(this.kbID);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(kbID);
	}
}
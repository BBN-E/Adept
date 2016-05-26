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

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Set;

import adept.common.KBID;
import adept.common.OntType;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * This class represents the link between a KBRelation and one of its arguments.
 * 
 * KBRelationArguments are dependent on their owning KBRelation, and cannot be
 * saved by themselves.
 * 
 * Once built, a KBRelationArgument is immutable.
 * 
 * 
 * @author dkolas
 */
public class KBRelationArgument extends KBPredicateArgument {

	/**
	 * The role of this argument.
	 */
	private final OntType role;

	/**
	 * The target of this argument.
	 */
	private final KBPredicateArgument target;

	/**
	 * The confidence of this argument
	 */
	private final float confidence;

	/**
	 * Gets the role for this relation argument
	 * 
	 * @return
	 */
	public OntType getRole() {
		return role;
	}

	/**
	 * Gets the target for this relation argument
	 * 
	 * @return
	 */
	public KBPredicateArgument getTarget() {
		return target;
	}

	/**
	 * Gets the confidence for this relation argument
	 * 
	 * @return
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Private constructor, intended to be called only by builders.
	 * 
	 * @param kbID
	 * @param role
	 * @param target
	 * @param provenances
	 * @param confidence
	 */
	private KBRelationArgument(KBID kbID, OntType role, KBPredicateArgument target,
			Set<KBProvenance> provenances, float confidence) {
		super(kbID, provenances);

		this.role = checkNotNull(role);
		this.target = checkNotNull(target);
		this.confidence = confidence;
	}

	/**
	 * Create a new builder, given a role, target, and confidence. Provenances
	 * may then be added.
	 * 
	 * @param role
	 * @param target
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder insertionBuilder(OntType role, KBPredicateArgument target,
			float confidence) {
		return new InsertionBuilder(role, target, confidence);
	}

	/**
	 * Create a new UpdateBuilder for updating this KBRelationArgument.
	 * Confidence and provenances are updatable.
	 * 
	 * @return {@link UpdateBuilder}
	 */
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder(this);
	}

	/**
	 * Class for creating a new KBRelationArgument. Only intended to be saved by
	 * other classes in this package, i.e, {@link KBRelation}.
	 * 
	 * Does not inherit from {@link KBPredicateArgument#InsertionBuilder}
	 * because this object cannot be saved on its own.
	 * 
	 */
	public static class InsertionBuilder extends KBObjectBuilder {

		private final OntType role;
		private KBPredicateArgument target;
		private Set<KBProvenance.InsertionBuilder> provenances;
		private final float confidence;

		public float getConfidence() {
			return confidence;
		}

		public OntType getRole() {
			return role;
		}

		public KBPredicateArgument getTarget() {
			return target;
		}

		public Set<KBProvenance.InsertionBuilder> getProvenances() {
			return provenances;
		}

		/**
		 * Internal constructor for InsertionBuilder. Should only be called by
		 * insertionBuilder methods.
		 * 
		 * @param role
		 * @param target
		 * @param confidence
		 */
		private InsertionBuilder(OntType role, KBPredicateArgument target, float confidence) {
			this.role = checkNotNull(role);
			this.target = checkNotNull(target);
			this.confidence = confidence;
			provenances = new HashSet<KBProvenance.InsertionBuilder>();
		}

		/**
		 * Add a provenance builder to this KBRelationArgument. The provenance
		 * will be saved when the KBRelationArgument is inserted, which is when
		 * the object that owns this KBrelationArgument is inserted.
		 * 
		 * @param provenance
		 * @return
		 */
		public InsertionBuilder addProvenance(KBProvenance.InsertionBuilder provenance) {
			provenances.add(provenance);
			return this;
		}

		/**
		 * Create the KBRelationArgument. Only intended to be called internally,
		 * when the owning object is being saved.
		 * 
		 * @param kbID
		 * @return
		 */
		protected KBRelationArgument build(KBID kbID) {
			Set<KBProvenance> builtProvenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getProvenances()) {
				builtProvenances.add(provenanceBuilder.build());
			}
			return new KBRelationArgument(kbID, role, target, builtProvenances, confidence);
		}

		/**
		 * Add a provenance builder to this KBRelationArgument. The provenance
		 * will be saved when the KBRelationArgument is inserted, which is when
		 * the object that owns this KBrelationArgument is inserted.
		 * 
		 * @param provenance
		 * @return
		 */
		public void addProvenances(Set<KBProvenance.InsertionBuilder> provenances) {
			this.provenances.addAll(provenances);
		}

		/**
		 * Used *Only* when adding an additional type to this entity as part of
		 * the process of inserting a relation.
		 * 
		 * @param kbEntity
		 */
		protected void setTarget(KBEntity kbEntity) {
			target = kbEntity;
		}
	}

	/**
	 * UpdateBuilder class for Relation Arguments. Confidence and provenances
	 * are updatable.
	 * 
	 * @author dkolas
	 */
	public class UpdateBuilder {
		private KBRelationArgument kbRelationArgument = null;
		private Set<KBProvenance.InsertionBuilder> newProvenances;
		private Set<KBProvenance> provenancesToRemove;

		private Float newConfidence;

		private UpdateBuilder(KBRelationArgument kbRelationArgument) {
			this.kbRelationArgument = kbRelationArgument;
			newProvenances = new HashSet<KBProvenance.InsertionBuilder>();
			provenancesToRemove = new HashSet<KBProvenance>();
		}

		public KBID getKBID() {
			return kbRelationArgument.getKBID();
		}

		protected KBRelationArgument getKBRelationArgument() {
			return kbRelationArgument;
		}

		public Set<KBProvenance.InsertionBuilder> getNewProvenances() {
			return newProvenances;
		}

		public Set<KBProvenance> getProvenancesToRemove() {
			return provenancesToRemove;
		}

		/**
		 * Add a new provenance to this object. The new provenance builder will
		 * be saved to the KB when update is called.
		 * 
		 * @param provenance
		 * @return
		 */
		public UpdateBuilder addProvenance(KBProvenance.InsertionBuilder provenance) {
			newProvenances.add(provenance);
			return this;
		}

		/**
		 * Remove a provenance from this object. The existing provenance will be
		 * removed from the KBPredicateArgument when update is called.
		 * 
		 * @param provenance
		 * @return
		 */
		public UpdateBuilder removeProvenance(KBProvenance provenance) {
			Preconditions.checkArgument(kbRelationArgument.getProvenances().contains(provenance),
					"This relation does not contain the given provenance.");
			provenancesToRemove.add(provenance);
			return this;
		}

		public Float getNewConfidence() {
			return newConfidence;
		}

		public UpdateBuilder setConfidence(float newConfidence) {
			this.newConfidence = newConfidence;
			return this;
		}

		/**
		 * Update the KBRelationArgument, applying the requested changes.
		 * Returns a new KBRelationArgument with the updates applied.
		 * 
		 * @param kb
		 * @return
		 * 
		 * @see adept.kbapi.model.KBPredicateArgument.UpdateBuilder#update(adept.kbapi.KB)
		 */
		protected KBRelationArgument update(KB kb) throws KBUpdateException {
			kb.updateRelationArgument(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : kbRelationArgument.getProvenances()) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBRelationArgument(kbRelationArgument.getKBID(), kbRelationArgument.role,
					kbRelationArgument.target, provenances,
					getNewConfidence() != null ? getNewConfidence() : kbRelationArgument.confidence);
		}

	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KBRelationArgument)) {
			return false;
		}
		KBRelationArgument that = (KBRelationArgument) o;
		return super.equals(that) && Objects.equal(that.role, this.role)
				&& Objects.equal(that.target, this.target) && that.confidence == this.confidence;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), role, target, confidence);
	}
}

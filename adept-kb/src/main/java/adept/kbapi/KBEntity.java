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

/*
 * 
 */
package adept.kbapi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.IType;
import adept.common.KBID;
import adept.common.OntType;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * The Class KBEntity represents an entity saved in the knowledge base. A
 * KBEntity may have multiple entity types, with associated confidences.
 * 
 */
public class KBEntity extends KBThing {
	private final float confidence;

	/**
	 * Types and confidences
	 */
	private final ImmutableMap<OntType, Float> types;

	/**
	 * Canonical mention confidence
	 */
	private final float canonicalMentionConfidence;

	/**
	 * Private constructor. To be called only by builders.
	 * 
	 * @param kbID
	 * @param types
	 * @param canonicalString
	 * @param provenances
	 */
	private KBEntity(KBID kbID, float confidence, Map<OntType, Float> types,
			String canonicalString, float canonicalMentionConfidence, Set<KBProvenance> provenances) {
		super(kbID, provenances, canonicalString);
		Preconditions.checkNotNull(types);
		this.confidence = confidence;
		this.types = ImmutableMap.copyOf(types);
		this.canonicalMentionConfidence = canonicalMentionConfidence;
	}

	public float getConfidence() {
		return confidence;
	}

	/**
	 * Get the types of this entity, and their confidences.
	 * 
	 * @return
	 */
	public ImmutableMap<OntType, Float> getTypes() {
		return types;
	}

	/**
	 * Gets the canonical mention confidence for this entity
	 * 
	 * @return
	 */
	public float getCanonicalMentionConfidence() {
		return canonicalMentionConfidence;
	}

	/**
	 * Get a new InsertionBuilder for this entity.
	 * 
	 * @param types
	 * @param canonicalString
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder entityInsertionBuilder(Map<OntType, Float> types,
			KBTextProvenance.InsertionBuilder canonicalMention, float confidence,
			float canonicalMentionConfidence) {
		return new InsertionBuilder(types, canonicalMention, confidence, canonicalMentionConfidence);
	}

	/**
	 * 
	 * Get a new InsertionBuilder based on an existing Entity. Types will be
	 * mapped via the KBOntologyMap.
	 * 
	 * @param entity
	 * @param entityMentions
	 * @param ontologyMap
	 * @return
	 */
	public static InsertionBuilder entityInsertionBuilder(Entity entity,
			List<EntityMention> entityMentions, KBOntologyMap ontologyMap) {
		Preconditions.checkNotNull(entity);
		Preconditions.checkNotNull(entityMentions);
		Preconditions.checkArgument(!entityMentions.isEmpty());
		Preconditions.checkNotNull(entity.getCanonicalMention());
		Preconditions.checkNotNull(ontologyMap);

		Map<OntType, Float> types = new HashMap<OntType, Float>();
		for (Map.Entry<IType, Double> type : entity.getAllTypes().entrySet()) {
			Optional<OntType> ontType = ontologyMap.getKBTypeForType(type.getKey());
			Preconditions.checkArgument(ontType.isPresent(),
					"Ontology map must have mapping for the Entity type.");
			types.put(ontType.get(), type.getValue().floatValue());
		}
		long entityId = entity.getEntityId();

		EntityMention canonicalMention = entity.getCanonicalMention();
		KBTextProvenance.InsertionBuilder canonicalMentionBuilder = KBTextProvenance.builder(
				canonicalMention, canonicalMention.getConfidence(entityId));
		InsertionBuilder insertionBuilder = new InsertionBuilder(types, canonicalMentionBuilder,
				(float) entity.getEntityConfidence(),
				(float) entity.getCanonicalMentionConfidence());

		for (EntityMention entityMention : entityMentions) {
			if (!entityMention.equals(canonicalMention)) {
				Preconditions
						.checkArgument(
								entityMention.getEntityIdDistribution().containsKey(
										entity.getEntityId()),
								"All EntityMentions must be associated with the Entity in their entity map");
				Optional<OntType> ontType = ontologyMap.getKBTypeForType(entityMention
						.getEntityType());
				Preconditions
						.checkArgument(ontType.isPresent(),
								"Ontology map must have mapping for the entity types of all entity mentions.");
				KBTextProvenance.InsertionBuilder textProvenance = KBTextProvenance.builder(
						entityMention, entityMention.getConfidence(entityId));
				insertionBuilder.addProvenance(textProvenance);
			}
		}
		return insertionBuilder;
	}

	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilderWithConfidence<InsertionBuilder, KBEntity> {

		private final Map<OntType, Float> types;
		private final KBTextProvenance.InsertionBuilder canonicalMention;
		private final float canonicalMentionConfidence;

		/**
		 * @param types
		 * @param canonicalMention
		 * @param confidence
		 */
		public InsertionBuilder(Map<OntType, Float> types,
				KBTextProvenance.InsertionBuilder canonicalMention, float confidence,
				float canonicalMentionConfidence) {
			super(confidence);
			Preconditions.checkNotNull(types);
			Preconditions.checkArgument(!types.isEmpty());
			Preconditions.checkNotNull(canonicalMention);
			Preconditions.checkNotNull(canonicalMention.getValue());
			this.types = types;
			this.canonicalMention = canonicalMention;
			this.canonicalMentionConfidence = canonicalMentionConfidence;
			addProvenance(canonicalMention);
		}

		public Map<OntType, Float> getTypes() {
			return types;
		}

		public KBTextProvenance.InsertionBuilder getCanonicalMention() {
			return canonicalMention;
		}

		public float getCanonicalMentionConfidence() {
			return canonicalMentionConfidence;
		}

		/**
		 * 
		 * @param kb
		 * @return
		 * 
		 * @see adept.kbapi.model.KBPredicateArgument.InsertionBuilder#insert(adept.kbapi.KB)
		 */
		@Override
		public KBEntity insert(KB kb) throws KBUpdateException {
			kb.insertEntity(this);
			return build();
		}

		protected KBEntity build() {
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			return new KBEntity(kbid, getConfidence(), types,
					this.getCanonicalMention().getValue(), canonicalMentionConfidence, provenances);
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
	 * Get an updateBuilder for this KBEntity.
	 * 
	 * Allowed updates are the canonical string, confidence, and provenances.
	 * 
	 * @return
	 * 
	 * @see adept.kbapi.model.KBThing#updateBuilder()
	 */
	@Override
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder();
	}

	/**
	 * Builder for updating the KBEntity.
	 * 
	 * @author dkolas
	 */
	public class UpdateBuilder extends
			KBPredicateArgument.UpdateBuilderWithConfidence<UpdateBuilder, KBEntity> {

		private KBTextProvenance.InsertionBuilder newCanonicalMention = null;
		private Float newCanonicalMentionConfidence = null;

		private Map<OntType, Float> updatedTypes;

		private UpdateBuilder() {
			updatedTypes = new HashMap<OntType, Float>(types);
		}

		public UpdateBuilder setNewCanonicalMention(
				KBTextProvenance.InsertionBuilder newCanonicalMention,
				float newCanonicalMentionConfidence) {
			this.newCanonicalMention = newCanonicalMention;
			this.newCanonicalMentionConfidence = newCanonicalMentionConfidence;
			return me();
		}

		public UpdateBuilder addNewType(OntType type, Float confidence) {
			Preconditions.checkArgument(!types.containsKey(type),
					"This entity already contains the given type");
			updatedTypes.put(type, confidence);
			return me();
		}

		public UpdateBuilder removeType(OntType type) {
			Preconditions.checkArgument(types.containsKey(type),
					"This entity does not contain the given type.");
			updatedTypes.remove(type);
			return me();
		}

		public UpdateBuilder alterTypeConfidence(OntType type, Float confidence) {
			Preconditions.checkArgument(types.containsKey(type),
					"This entity does not contain the given type.");
			updatedTypes.put(type, confidence);
			return me();
		}

		public KBTextProvenance.InsertionBuilder getNewCanonicalMention() {
			return newCanonicalMention;
		}

		public float getNewCanonicalMentionConfidence() {
			return newCanonicalMentionConfidence;
		}

		public Map<OntType, Float> getUpdatedTypes() {
			return updatedTypes;
		}

		@Override
		public KBEntity update(KB kb) throws KBUpdateException {
			Preconditions.checkArgument(!updatedTypes.isEmpty());
			kb.updateEntity(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : getProvenances()) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBEntity(getKBID(),
					this.getNewConfidence() != null ? this.getNewConfidence() : getConfidence(),
					updatedTypes, newCanonicalMention != null ? newCanonicalMention.getValue()
							: getCanonicalString(),
					newCanonicalMentionConfidence != null ? newCanonicalMentionConfidence
							: getCanonicalMentionConfidence(), provenances);
		}

		@Override
		public KBID getKBID() {
			return KBEntity.this.getKBID();
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
		if (!(o instanceof KBEntity)) {
			return false;
		}
		KBEntity that = (KBEntity) o;
		return super.equals(that) && that.confidence == this.confidence
				&& Objects.equal(that.types, this.types)
				&& that.canonicalMentionConfidence == this.canonicalMentionConfidence;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), confidence, types, canonicalMentionConfidence);
	}

}

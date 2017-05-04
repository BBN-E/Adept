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
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multiset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class KBEntity represents an entity saved in the knowledge base. A
 * KBEntity may have multiple entity types, with associated confidences.
 *
 */
public class KBEntity extends KBThing {
	private final float confidence;

  	private static Logger log = LoggerFactory.getLogger(KBEntity.class);

	private static final OntType FEMALE = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Female");
	private static final OntType MALE = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Male");

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
	private KBEntity(KB kb, KBID kbID, float confidence, Map<OntType, Float> types,
			String canonicalString, float canonicalMentionConfidence, Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, provenances, canonicalString);
		checkNotNull(types);
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
	 * @param canonicalMention
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder entityInsertionBuilder(Map<OntType, Float> types,
			KBTextProvenance.InsertionBuilder canonicalMention, float confidence,
			float canonicalMentionConfidence) {
		InsertionBuilder insertionBuilder = new InsertionBuilder(types, canonicalMention, confidence, canonicalMentionConfidence);
		insertionBuilder.addProvenance(canonicalMention);
		return insertionBuilder;
	}

	/**
	 * Protected InsertionBuilder method.
	 * Used when an Entity is loaded as part of a query so that mentions can be lazy loaded.
	 *
	 * @param types
	 * @param canonicalMentionID
	 * @param canonicalMentionValue
	 * @param confidence
	 * @param canonicalMentionConfidence
	 * @return
	 */
	protected static InsertionBuilder entityInsertionBuilder(Map<OntType, Float> types,
			KBID canonicalMentionID, String canonicalMentionValue, float confidence, float canonicalMentionConfidence){
		return new InsertionBuilder(types, canonicalMentionID, canonicalMentionValue, confidence, canonicalMentionConfidence);
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
		checkNotNull(entity);
		checkNotNull(entityMentions);
		Preconditions.checkArgument(!entityMentions.isEmpty());
		checkNotNull(entity.getCanonicalMention());
		checkNotNull(ontologyMap);

		Map<OntType, Float> types = new HashMap<OntType, Float>();
		for (Map.Entry<IType, Double> type : entity.getAllTypes().entrySet()) {
			Optional<OntType> ontType = ontologyMap.getKBTypeForType(type.getKey());
			Preconditions.checkArgument(ontType.isPresent(),
					"Ontology map must have mapping for the Entity type.");
			types.put(ontType.get(), type.getValue().floatValue());
		}

		Map<IType, Double> genders = entity.getGenderConfidences();
		for (Map.Entry<IType, Double> entry : genders.entrySet()) {
			if (entry.getKey().getType().equals("MALE")){
				types.put(MALE, entry.getValue().floatValue());
			} else if (entry.getKey().getType().equals("FEMALE")){
				types.put(FEMALE, entry.getValue().floatValue());
			} else {
				Preconditions.checkArgument(entry.getKey().getType().equals("MALE") || entry.getKey().getType().equals("FEMALE"), "Unknown gender type ["+entry.getKey().getType()+"] cannot be mapped.");
			}
		}
		long entityId = entity.getEntityId();

		EntityMention canonicalMention = entity.getCanonicalMention();
		KBTextProvenance.InsertionBuilder canonicalMentionBuilder = KBTextProvenance.builder(
				canonicalMention, canonicalMention.getConfidence(entityId));
		InsertionBuilder insertionBuilder = entityInsertionBuilder(types, canonicalMentionBuilder,
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
		private final KBID canonicalMentionID;
		private final String canonicalMentionValue;
		private final float canonicalMentionConfidence;

		private final Optional<KBProvenance.InsertionBuilder> canonicalMention;

		/**
		 * This builder is for query-time loading of the Entity.  The canonical mention
		 * is only loaded as an ID and a value string
		 * @param types
		 * @param canonicalMentionID
		 * @param canonicalMentionValue
		 * @param confidence
		 * @param canonicalMentionConfidence
		 */
		public InsertionBuilder(Map<OntType, Float> types,
				KBID canonicalMentionID, String canonicalMentionValue, float confidence,
				float canonicalMentionConfidence) {
			super(confidence);
			checkNotNull(types);
			Preconditions.checkArgument(!types.isEmpty());
			checkNotNull(canonicalMentionValue);
			checkNotNull(canonicalMentionID);
			this.types = types;
			this.canonicalMentionID = canonicalMentionID;
			this.canonicalMentionValue = canonicalMentionValue;
			this.canonicalMentionConfidence = canonicalMentionConfidence;
			this.canonicalMention = Optional.absent();
		}

		/**
		 * This builder is for insertion-time creation of the Entity.  The canonical mention
		 * is given as a builder
		 *
		 * @param types
		 * @param canonicalMention
		 * @param confidence
		 * @param canonicalMentionConfidence
		 */
		public InsertionBuilder(Map<OntType, Float> types,
				KBProvenance.InsertionBuilder canonicalMention, float confidence,
				float canonicalMentionConfidence) {
			super(confidence);
			checkNotNull(types);
			Preconditions.checkArgument(!types.isEmpty());
			this.types = types;
			this.canonicalMentionID = null;
			this.canonicalMentionValue = null;
			this.canonicalMentionConfidence = canonicalMentionConfidence;
			this.canonicalMention = Optional.of(canonicalMention);
		}



		public Map<OntType, Float> getTypes() {
			return types;
		}

		public float getCanonicalMentionConfidence() {
			return canonicalMentionConfidence;
		}

		public String getCanonicalMentionValue() {
			if (canonicalMention.isPresent()){
				return canonicalMention.get().getValue();
			}else{
				return canonicalMentionValue;
			}
		}

		public KBID getCanonicalMentionID(){
			if (canonicalMention.isPresent()){
				return canonicalMention.get().getKBID();
			}else{
				return canonicalMentionID;
			}
		}

		public Optional<KBProvenance.InsertionBuilder> getCanonicalMentionBuilder(){
			return canonicalMention;
		}

		/**
		 *
		 * @param kb
		 * @return
		 */
		@Override
		public KBEntity insert(KB kb) throws KBUpdateException {
			kb.insertEntity(this);
			return build(kb, false);
		}

		protected KBEntity build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBEntity(kb, kbid, getConfidence(), types,
					this.getCanonicalMentionValue(), canonicalMentionConfidence, provenances);
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
			Set<KBProvenance> oldProvenances = null;
			try{
				oldProvenances = getProvenances();
				checkProvenancesToRemove();
			}catch(KBQueryException e){
				throw new KBUpdateException("Could not load provenances for original object",e);
			}

			kb.updateEntity(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
		  	for(KBProvenance.UpdateBuilder provenanceBuilder : getProvenancesToUpdate()){
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : oldProvenances) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBEntity(kb, getKBID(),
					this.getNewConfidence() != null ? this.getNewConfidence() : getConfidence(),
					updatedTypes, newCanonicalMention != null ? newCanonicalMention.getValue()
							: getCanonicalString(),
					newCanonicalMentionConfidence != null ? newCanonicalMentionConfidence
							: getCanonicalMentionConfidence(), Optional.of(provenances));
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

  /**
   * <p>
   * API to merge multiple KBEntities into a single KBEntity. Merging of KBEntities may be
   * required, for example, when trying to remove duplicate entities (which point to the same
   * real-world object--in other words, have the same externalKBIds)
   * <p>
   *   Current implementation of this method gets the most confident canonicalString and a
   *   weighted average of entity-type confidences from the contributing KBEntities. In case of
   *   multiple canonicalStrings with maximum confidence, one such string is chosen arbitrarily.
   *   The entity-type confidences are weighted by the number of provenances of the contributing
   *   KBEntity. The confidence of the merged entity is also a weighted average weighted by
   *   number of provenances of the entities being merged.
   * <p>
   *   The provenances of all the contributing KBEntities are re-linked to the merged entity.
   *   Re-linking simply means updating the KBId field of the contributing provenances in the
   *   TextProvenance table with the KBID of the merged entity.
   * <p>
   *   In this implementation, the merged-entity is not a new KBEntity, but a KBEntity chosen
   *   arbitrarily from the input list of KBEntities to merge. Specifically, it's the first
   *   KBEntity in the list. To reflect the merging, the confidence, canonicalString,
   *   canonicalMentionConfidence,entity-types map of the merged KBEntity are updated with new
   *   values.
   * <p>
   *   Currently, when deleting duplicate KBEntities, any relations or events
   *   they might be part of are not updated. This should change in a later version of
   *   the api.
   * </p>
   *
   * @param kbEntitiesToMerge {@code List<KBEntity>} of KBEntities to merge
   * @param kb KB instance
   *
   * @return the merged KBEntity
   *
   */
  public static KBEntity mergeKBEntities(List<KBEntity> kbEntitiesToMerge, KB kb)
      throws KBUpdateException, KBQueryException {

    checkNotNull(kbEntitiesToMerge);
    checkArgument(!kbEntitiesToMerge.isEmpty(),"kbEntitiesToMerge list cannot be empty.");
    if(kbEntitiesToMerge.size()==1){
      return kbEntitiesToMerge.get(0);
    }
    KBEntity kbEntityToRetain = null;
    KBTextProvenance.InsertionBuilder canonicalMention=null;
    Map<Float, Multiset<String>> canonicalStringConfidences = new HashMap<>();
    Map<OntType, Float> entityTypeConfidences = new HashMap<OntType, Float>();
    double weightTotal = 0.0;
    float maxCanonicalStringConfidence = -1.0f;
    float averageConfidence = 0.0f;
    Set<KBProvenance> allProvenances = new HashSet<>();
    for (KBEntity kbEntity : kbEntitiesToMerge) {
      log.info("KBEntity to merge: {}",kbEntity.getKBID().getObjectID());
      if(kbEntityToRetain==null) {
	kbEntityToRetain = kbEntity;
      }
      //canonical string and confidence
      String canonicalString = kbEntity.getCanonicalString();
      float canonicalMentionConfidence = kbEntity.getCanonicalMentionConfidence();
      Multiset<String> canonicalStrings = canonicalStringConfidences.get(
	  canonicalMentionConfidence);
      if (canonicalStrings == null) {
	canonicalStrings = HashMultiset.create();
      }
      canonicalStrings.add(canonicalString);
      canonicalStringConfidences.put(canonicalMentionConfidence, canonicalStrings);
      if (maxCanonicalStringConfidence < canonicalMentionConfidence) {
	maxCanonicalStringConfidence = canonicalMentionConfidence;
      }
      //types
      Set<OntType> entityTypes = kbEntity.getTypes().keySet();
      float weight = kbEntity.getProvenances().size();
      weightTotal += weight;

      for (OntType entityType : entityTypes) {
	Float typeConfidence = entityTypeConfidences.get(entityType);
	if (typeConfidence == null) {
	  typeConfidence = 0.0f;
	}
	typeConfidence += (kbEntity.getTypes().get(entityType) * weight);
	entityTypeConfidences.put(entityType, typeConfidence);
      }
      //provenances
      allProvenances.addAll(kbEntity.getProvenances());
      //confidence
      averageConfidence += (kbEntity.getConfidence()*weight);
    }
    int maxBestCanonicalStringCount = 0;
    String bestCanonicalString = null;
    for (String canonicalString : canonicalStringConfidences.get(maxCanonicalStringConfidence)) {
      int count = canonicalStringConfidences.get(maxCanonicalStringConfidence).count
	  (canonicalString);
      if (count > maxBestCanonicalStringCount) {
	maxBestCanonicalStringCount = count;
	bestCanonicalString = canonicalString;
      }
    }
    //update the confidences in entityType map
    for (Map.Entry<OntType, Float> entry : entityTypeConfidences.entrySet()) {
      float confidence = entry.getValue();
      confidence /= weightTotal;
      entityTypeConfidences.put(entry.getKey(), confidence);
    }
    averageConfidence/=weightTotal;
    KBEntity.UpdateBuilder updateBuilder = kbEntityToRetain.updateBuilder();
    //update the types in KBEntity
    for(OntType entityType : kbEntityToRetain.getTypes().keySet()) {
      updateBuilder.alterTypeConfidence(entityType, entityTypeConfidences.get(entityType));
    }
    for(OntType entityType : entityTypeConfidences.keySet()) {
      if(!kbEntityToRetain.getTypes().containsKey(entityType)) {
	      updateBuilder.addNewType(entityType, entityTypeConfidences.get(entityType));
      }
    }
    //provenances
    for(KBProvenance provenance : allProvenances) {
      if(!kbEntityToRetain.getProvenances().contains(provenance)) {
	KBTextProvenance.UpdateBuilder provenanceUpdateBuilder =
	    ((KBTextProvenance)provenance).getUpdateBuilder();
	provenanceUpdateBuilder.setSourceEntityKBID(kbEntityToRetain.getKBID());
	log.info("Added provenance {}.... ",provenanceUpdateBuilder
	    .getKBID().getObjectID());
	log.info("...to update with KBId: {}",provenanceUpdateBuilder.getSourceEntityKBID()
	    .getObjectID());
	updateBuilder.addProvenanceToUpdate(provenanceUpdateBuilder);
      }
      if(canonicalMention==null&&bestCanonicalString.equals(((KBTextProvenance)
								 provenance).getValue())){
	canonicalMention=createInsertionBuilderForProvenance((KBTextProvenance)provenance);
      }
    }
    //canonicalmention
    updateBuilder.setNewCanonicalMention(canonicalMention, maxCanonicalStringConfidence);
    //update the confidence
    updateBuilder.setConfidence(averageConfidence);
    KBEntity mergedKBEntity =  updateBuilder.update(kb);
    log.info("Deleting duplicate KBEntities...");
    for(KBEntity kbEntity : kbEntitiesToMerge) {
      if(!kbEntity.getKBID().equals(mergedKBEntity.getKBID())) {
	log.info("Deleting KBEntity with KBID {}...",kbEntity.getKBID().getObjectID());
	kb.deleteDuplicateKBObject(kbEntity.getKBID());
      }
    }
    return mergedKBEntity;
  }

  private static KBTextProvenance.InsertionBuilder createInsertionBuilderForProvenance
      (KBTextProvenance
      provenance) {
    KBTextProvenance.InsertionBuilder insertionBuilder = KBTextProvenance.builder();
    insertionBuilder.setBeginOffset(provenance.getBeginOffset());
    insertionBuilder.setContributingSiteName(provenance.getContributingSiteName());
    insertionBuilder.setCorpusID(provenance.getCorpusID());
    insertionBuilder.setCorpusName(provenance.getCorpusName());
    insertionBuilder.setCorpusType(provenance.getCorpusType());
    insertionBuilder.setCorpusURI(provenance.getCorpusURI());
    insertionBuilder.setDocumentID(provenance.getDocumentID());
    insertionBuilder.setDocumentPublicationDate(provenance.getDocumentPublicationDate());
    insertionBuilder.setDocumentID(provenance.getDocumentID());
    insertionBuilder.setDocumentURI(provenance.getDocumentURI());
    insertionBuilder.setEndOffset(provenance.getEndOffset());
    insertionBuilder.setConfidence(provenance.getConfidence());
    insertionBuilder.setSourceAlgorithmName(provenance.getSourceAlgorithmName());
    insertionBuilder.setSourceLanguage(provenance.getSourceLanguage());
    insertionBuilder.setValue(provenance.getValue());
    return insertionBuilder;
  }


}

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
import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adept.common.Chunk;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.EntityMention;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;
import adept.common.RelationMention;
import adept.common.TimePhrase;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Class KBRelation represents a fully loaded, immutable relation.
 */
public class KBRelation extends KBPredicateArgument {

  private static Logger log = LoggerFactory.getLogger(KBRelation.class);
  /**
   * The relation type.
   */
  private final OntType relationType;
  /**
   * Confidence that this relation is correct.
   */
  private final float confidence;

  /**
   * Immutable set of relation arguments.
   */
  private final ImmutableSet<KBRelationArgument> arguments;

  /**
   * Instantiates a new kB relation; to be called only from KBAPI Builder
   * classes.
   *
   * @param kb           the kb instance
   * @param relationType the relation type
   * @param confidence   the confidence value
   */
  protected KBRelation(KB kb, KBID kbID, OntType relationType, float confidence,
      Set<KBRelationArgument> arguments, Optional<Set<KBProvenance>> provenances) {
    super(kb, kbID, provenances);

    checkArgument(relationType != null);
    this.relationType = relationType;
    this.confidence = confidence;
    this.arguments = ImmutableSet.copyOf(arguments);
  }

  /**
   * Create a new InsertionBuilder for insertion into the KB. This method
   * creates an InsertionBuilder with no arguments or provenances, which
   * should be added before insertion.
   */
  public static InsertionBuilder relationInsertionBuilder(OntType relationType, float confidence) {
    return new InsertionBuilder(relationType, confidence);
  }

  /**
   * Create a new InsertionBuilder for querying from the KB. This method
   * should only be called internally.
   *
   * @return the new builder.
   */
  protected static InsertionBuilder relationInsertionBuilder() {
    return new InsertionBuilder();
  }

  /**
   * Create a new InsertionBuilder for insertion into the KB. This method
   * prepopulates an InsertionBuilder with the content of a
   * {@link DocumentRelation}. All argument targets of the
   * {@link DocumentRelation} must have been inserted into the KB already, and
   * they must be mapped from an {@link Item} to a {@link KBPredicateArgument}
   * in the insertedArgumentMap. All provenances on both the
   * {@link DocumentRelation} and {@link DocumentRelationArgument}s will be
   * added to the builder.
   *
   * The ontologyMap will be used to convert Types and Roles from the
   * DocumentRelation into the ontology of the KB. An exception will be thrown
   * if the types cannot be mapped.
   *
   * @return the new builder. May be chained.
   */
  public static InsertionBuilder relationInsertionBuilder(DocumentRelation documentRelation,
      Map<Item, KBPredicateArgument> insertedArgumentMap, KBOntologyMap ontologyMap) {
    checkArgument(ontologyMap.getKBTypeForType(documentRelation.getRelationType()).isPresent(),
        "Ontology map must have entry for DocumentRelation type");
    checkArgument(
        !ontologyMap.relationConvertsToEvent(documentRelation.getRelationType()),
        "This relation type maps to an event.  Please use KBEvent.eventInsertionBuilder(DocumentRelation documentRelation...)");
    OntType relationType = ontologyMap.getKBTypeForType(documentRelation.getRelationType())
        .get();
    InsertionBuilder builder = new InsertionBuilder(relationType,
        documentRelation.getConfidence());
    relationInsertionBuilder(builder, documentRelation, insertedArgumentMap, ontologyMap);
    return builder;
  }

  protected static void relationInsertionBuilder(AbstractInsertionBuilder<?, ?> builder,
      DocumentRelation documentRelation, Map<Item, KBPredicateArgument> insertedArgumentMap,
      KBOntologyMap ontologyMap) {
    // Check preconditions
    for (DocumentRelationArgument argument : documentRelation.getArguments()) {
      Preconditions
          .checkArgument(
              argument.getFiller().asItem().isPresent(),
              "All argument targets of this DocumentRelation must be instances of Item. (Are you using an unsupported subclass of TemporalValue?)");
      Preconditions
          .checkNotNull(
              insertedArgumentMap.get(argument.getFiller().asItem().get()),
              "All arguments of this DocumentRelation must have already been inserted, and have their KBID in the insertedArgumentMap.");
      // Ensure that the ontology map contains a type for the argument
      checkArgument(
          ontologyMap.getKBRoleForType(documentRelation.getRelationType(),
              argument.getRole()).isPresent(),
          "Ontology map must have a role for role " + argument.getRole().getType()
              + " on relation type " + documentRelation.getRelationType().getType());
    }

    for (DocumentRelationArgument argument : documentRelation.getArguments()) {
      // Presence of this already verified
      OntType role = ontologyMap.getKBRoleForType(documentRelation.getRelationType(),
          argument.getRole()).get();
      Item argumentTarget = argument.getFiller().asItem().get();
      KBPredicateArgument targetKBObject = insertedArgumentMap.get(argumentTarget);
      Optional<OntType> additionalEntityType = ontologyMap.getAdditionalTypeForRoleTarget(
          documentRelation.getRelationType(), argument.getRole());
      if (additionalEntityType.isPresent()) {
        if (targetKBObject instanceof KBEntity) {
          KBEntity entityForAdditionalTypes = (KBEntity) targetKBObject;
          if (!entityForAdditionalTypes.getTypes()
              .containsKey(additionalEntityType.get())) {
            builder.addRequiredEntityUpdate(entityForAdditionalTypes.updateBuilder()
                .addNewType(additionalEntityType.get(),
                    documentRelation.getConfidence()));
          }
        } else {
          Preconditions
              .checkArgument(
                  false,
                  "OntologyMap specifies an additional type "
                      + additionalEntityType.get().getType()
                      + " for relation class "
                      + documentRelation.getRelationType()
                      + " and role "
                      + argument.getRole()
                      + ".  However, the type could not be assigned, because the target of that role is not a KBEntity.");
        }
      }

      KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
          .insertionBuilder(role, targetKBObject, argument.getConfidence());

      for (RelationMention.Filler mention : argument.getProvenances()) {
        Chunk chunk = null;
        String mentionType = null;
        if (mention.asEntityMention().isPresent()) {
          chunk = mention.asEntityMention().get();
          mentionType = ((EntityMention) chunk).getMentionType()
              .getType();
        } else if (mention.asNumberPhrase().isPresent()) {
          chunk = mention.asNumberPhrase().get();
        } else if (mention.asTimePhrase().isPresent()) {
          chunk = mention.asTimePhrase().get();
          mentionType = ((TimePhrase) chunk).getType();
        } else if (mention.asGenericChunk().isPresent()) {
          chunk = mention.asGenericChunk().get();
        }
        if (null != chunk) {
          argumentBuilder.addProvenance(KBTextProvenance.builder(chunk,
              mention.getConfidence()));
        }
      }

      builder.addArgument(argumentBuilder);
    }

    for (RelationMention mention : documentRelation.getProvenances()) {
      // add provenances
      if (mention.getJustification() != null) {
        Optional<OntType> mentionType = ontologyMap.getKBTypeForType(mention
            .getRelationType());
        checkArgument(mentionType.isPresent(),
            "Ontology map must have entry for all RelationMention types.");
        KBProvenance.InsertionBuilder provenanceBuilder = KBTextProvenance.builder(
            mention.getJustification(), mention.getConfidence
                ());
        builder.addProvenance(provenanceBuilder);
      }
    }
  }

  /**
   * <p>
   * API to merge multiple KBRelations into a single KBRelation. Merging of KBRelations may be
   * required, for example, when trying to remove duplicate relations (which point to the same
   * real-world relation--in other words, have the same relation-type and the same set of
   * real-world objects, in the same role, as their arguments)
   * <p>
   * The relation-type of all to-be-merged relations should be the same. The
   * confidence of the merged relation is a weighted average weighted by
   * number of provenances of the relations being merged.
   * <p>
   * The provenances of all the contributing relations are re-linked to the merged relation.
   * Re-linking simply means updating the KBId field of the contributing provenances in the
   * TextProvenance table with the KBID of the merged relation.
   * <p>
   * In this implementation, the merged-relation is not a new KBRelation, but a KBRelation chosen
   * arbitrarily from the input list of KBRelations to merge. Specifically, it's the first
   * KBRelation in the list.
   * * <p>
   * Currently, when merging KBRelations, we are not merging their arguments. Arguments of
   * relations are supposed to be de-duplicated before merging the relations. This should change
   * in a later version of the api.
   *
   * @param kbRelationsToMerge {@code List<>} of KBRelations to merge
   * @param kb                 KB instance
   * @return the merged KBRelation
   */
  public static KBRelation mergeKBRelations(List<KBRelation> kbRelationsToMerge, KB kb)
      throws KBUpdateException, KBQueryException {

    checkNotNull(kbRelationsToMerge);
    checkArgument(!kbRelationsToMerge.isEmpty(), "kbRelationsToMerge list cannot be empty.");
    if (kbRelationsToMerge.size() == 1) {
      return kbRelationsToMerge.get(0);
    }
    KBRelation kbRelationToRetain = null;
    double relationConfidenceTotalWeight = 0.0;
    float averageRelationConfidence = 0.0f;
    Set<KBProvenance> allRelationProvenances = new HashSet<>();
    Multimap<Pair<OntType, KBID>, KBProvenance> argumentProvenances = HashMultimap.create();
    Multimap<Pair<OntType, KBID>, Float> argumentConfidences = HashMultimap.create();

    Multimap<OntType, KBID> referenceRoleTargetKBIDMap = HashMultimap.create();

    for (KBRelation kbRelation : kbRelationsToMerge) {
      log.info("KBRelation to merge: {}", kbRelation.getKBID().getObjectID());
      if (kbRelation.getArguments() == null || kbRelation.getArguments().size() == 0) {
        kbRelation = kb.getRelationById(kbRelation.getKBID());
      }
      if (kbRelationToRetain == null) {
        kbRelationToRetain = kbRelation;
        fillReferenceMap(kbRelationToRetain, referenceRoleTargetKBIDMap);
      }
      float weight = kbRelation.getProvenances().size();
      relationConfidenceTotalWeight += weight;
      //provenances
      allRelationProvenances.addAll(kbRelation.getProvenances());
      //confidence
      averageRelationConfidence += (kbRelation.getConfidence() * weight);
      for (KBRelationArgument kbArg : kbRelation.getArguments()) {
        OntType role = kbArg.getRole();
        KBID targetKBID = kbArg.getTarget().getKBID();
        if (!referenceRoleTargetKBIDMap.get(role).contains(targetKBID)) {
          throw new KBUpdateException(
              "Cannot merge relations with different args; found KBID: " + targetKBID +
                  " for role: " + role.getType() + " for relation: " + kbRelation.getKBID());
        }
        argumentConfidences
            .put(new Pair(role, targetKBID), kbArg.getConfidence() * kbArg.getProvenances().size());
        argumentProvenances.putAll(new Pair(role, targetKBID), kbArg.getProvenances());
      }
    }
    averageRelationConfidence /= relationConfidenceTotalWeight;

    UpdateBuilder relationUpdateBuilder = createRelationUpdateBuilder(kbRelationToRetain,
        allRelationProvenances, averageRelationConfidence, argumentProvenances,
        argumentConfidences);

    KBRelation mergedKBRelation = relationUpdateBuilder.update(kb);
    log.info("Deleting duplicate KBRelations...");
    for (KBRelation kbRelation : kbRelationsToMerge) {
      if (!kbRelation.getKBID().equals(mergedKBRelation.getKBID())) {
        for (KBRelationArgument kbRelationArgument : kbRelation.getArguments()) {
          log.info("Deleting KBRelationArgument with KBID {}...",
              kbRelationArgument.getKBID().getObjectID());
          kb.deleteDuplicateKBObject(kbRelationArgument.getKBID());
        }
        log.info("Deleting KBRelation with KBID {}...", kbRelation.getKBID().getObjectID());
        kb.deleteDuplicateKBObject(kbRelation.getKBID());
      }
    }
    return mergedKBRelation;
  }

  private static void fillReferenceMap(KBRelation kbRelation,
      Multimap<OntType, KBID> referenceMap) {
    for (KBRelationArgument kbArg : kbRelation.getArguments()) {
      referenceMap.put(kbArg.getRole(), kbArg.getTarget().getKBID());
    }
  }

  private static UpdateBuilder createRelationUpdateBuilder(KBRelation kbRelationToRetain,
      Set<KBProvenance> allRelationProvenances, float updatedRelationConfidence,
      Multimap<Pair<OntType, KBID>, KBProvenance> argumentProvenances,
      Multimap<Pair<OntType, KBID>, Float>
          argumentConfidences) throws KBQueryException {
    //update confidence and provenances for relation
    AbstractUpdateBuilder<?, ?> relationUpdateBuilder = kbRelationToRetain.updateBuilder();
    //update relation confidence
    relationUpdateBuilder.setConfidence(updatedRelationConfidence);
    //updating relation provenances
    for (KBProvenance provenance : allRelationProvenances) {
      if (!kbRelationToRetain.getProvenances().contains(provenance)) {
        KBTextProvenance.UpdateBuilder provenanceUpdateBuilder =
            ((KBTextProvenance) provenance).getUpdateBuilder(kbRelationToRetain.getKBID());
        log.info("Added provenance {}.... ", provenanceUpdateBuilder
            .getKBID().getObjectID());
        log.info("...to update with relation KBId: {}",
            provenanceUpdateBuilder.getSourceEntityKBID()
                .getObjectID());
        relationUpdateBuilder.addProvenanceToReassign(provenanceUpdateBuilder);
      }
    }
    //updating relation args
    for (KBRelationArgument kbArg : kbRelationToRetain.getArguments()) {
      Pair<OntType, KBID> kbArgKey = new Pair(kbArg.getRole(), kbArg.getTarget().getKBID());
      KBRelationArgument.UpdateBuilder argUpdateBuilder = kbArg.updateBuilder();
      //updating arg provenances
      for (KBProvenance provenance : argumentProvenances.get(kbArgKey)) {
        if (!kbArg.getProvenances().contains(provenance)) {
          KBTextProvenance.UpdateBuilder provenanceUpdateBuilder =
              ((KBTextProvenance) provenance).getUpdateBuilder(kbArg.getKBID());
          log.info("Added provenance {} to arg (role:{},targetKBId:{})", provenanceUpdateBuilder
              .getKBID().getObjectID(), kbArgKey.getL(), kbArgKey.getR());
          log.info("...to update with Arg KBId: {}", provenanceUpdateBuilder.getSourceEntityKBID()
              .getObjectID());
          argUpdateBuilder.addProvenanceToReassign(provenanceUpdateBuilder);
        }
      }
      //updating arg-confidence
      float updatedConfidence =
          argumentConfidences.get(kbArgKey).stream().reduce((aFloat, aFloat2) -> (aFloat + aFloat2))
              .get();
      updatedConfidence /= (argumentProvenances.get(kbArgKey).size());
      argUpdateBuilder.setConfidence(updatedConfidence);
      relationUpdateBuilder.addUpdatedArgument(argUpdateBuilder);
    }
    return (UpdateBuilder) relationUpdateBuilder;
  }

  /**
   * Gets the relation type.
   *
   * @return the relation type
   */
  public OntType getType() {
    return relationType;
  }

  /**
   * Gets the confidence value.
   *
   * @return the confidence.
   */
  public float getConfidence() {
    return confidence;
  }

  /**
   * Get the arguments of this relation.
   */
  public ImmutableSet<KBRelationArgument> getArguments() {
    return arguments;
  }

  /**
   * Get the subset of arguments of this relation that are of the specified
   * role.
   */
  public ImmutableSet<KBRelationArgument> getArgumentsByRole(OntType role) {
    ImmutableSet.Builder<KBRelationArgument> result = ImmutableSet.builder();
    for (KBRelationArgument argument : getArguments()) {
      if (argument.getRole().equals(role)) {
        result.add(argument);
      }
    }
    return result.build();
  }

  /**
   * Create a new UpdateBuilder based on this object. Only confidences and
   * provenances can be updated after a {@link KBRelation} is saved.
   *
   * Note that {@link UpdateBuilder#update(KB)} returns a NEW KBrelation
   * object, and this one will no longer be valid.
   *
   * @return the new updateBuilder
   */
  public AbstractUpdateBuilder<?, ?> updateBuilder() {
    return new UpdateBuilder();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof KBRelation)) {
      return false;
    }
    KBRelation that = (KBRelation) o;
    return super.equals(that) && Objects.equal(that.relationType, this.relationType)
        && that.confidence == this.confidence
        && Objects.equal(that.arguments, this.arguments);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), relationType, confidence, arguments);
  }

  public final static class InsertionBuilder extends
      AbstractInsertionBuilder<InsertionBuilder, KBRelation> {

    protected InsertionBuilder(OntType relationType, float confidence) {
      super(relationType, confidence);
    }

    private InsertionBuilder() {
      super();
    }

    protected InsertionBuilder me() {
      return this;
    }

    public KBRelation insert(KB kb) throws KBUpdateException {
      for (KBEntity.UpdateBuilder entityTypeUpdate : requiredEntityUpdates) {
        updatedEntities.put(entityTypeUpdate.getKBID(), entityTypeUpdate.update(kb));
      }
      kb.insertRelation(this);
      return build(kb, false);
    }

    protected KBRelation build(KB kb, boolean deferProvenances) {
      Preconditions.checkNotNull(kbid);
      Preconditions.checkArgument(getConfidence() >= 0);

      Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
      Set<KBRelationArgument> arguments = new HashSet<KBRelationArgument>();
      for (KBRelationArgument.InsertionBuilder argumentBuilder : getArguments()) {
        if (updatedEntities.containsKey(argumentBuilder.getTarget().getKBID())) {
          argumentBuilder.setTarget(updatedEntities.get(argumentBuilder.getTarget()
              .getKBID()));
        }
        arguments.add(argumentBuilder.build(kb, argumentBuilder.kbid, deferProvenances));
      }

      return new KBRelation(kb, kbid, getType(), getConfidence(), arguments, provenances);
    }

  }

  /**
   * Class InsertionBuilder for assembling the data required to insert a new
   * {@link KBRelation} object.
   *
   * @author dkolas
   */
  protected static abstract class AbstractInsertionBuilder<BuilderType extends AbstractInsertionBuilder<BuilderType, RelationType>, RelationType extends KBRelation>
      extends KBPredicateArgument.InsertionBuilderWithConfidence<BuilderType, RelationType> {

    protected Set<KBEntity.UpdateBuilder> requiredEntityUpdates;
    protected HashMap<KBID, KBEntity> updatedEntities;
    private Set<KBRelationArgument.InsertionBuilder> arguments;
    private OntType type;

    /**
     * Create a new InsertionBuilder. This method intended to be called only
     * internally. Please use static insertionBuilder methods on
     * {@link KBRelation}
     */
    protected AbstractInsertionBuilder(OntType relationType, float confidence) {
      super(confidence);
      Preconditions.checkNotNull(relationType);
      this.type = relationType;
      this.arguments = new HashSet<KBRelationArgument.InsertionBuilder>();
      this.requiredEntityUpdates = new HashSet<KBEntity.UpdateBuilder>();
      updatedEntities = new HashMap<KBID, KBEntity>();
    }

    /**
     * Constructor for use from querying, where we need to set the
     * relationtype after the fact
     */
    protected AbstractInsertionBuilder() {
      super(-1);
      this.arguments = new HashSet<KBRelationArgument.InsertionBuilder>();
      updatedEntities = new HashMap<KBID, KBEntity>();
    }

    public Set<KBRelationArgument.InsertionBuilder> getArguments() {
      return arguments;
    }

    public OntType getType() {
      return type;
    }

    protected BuilderType setRelationType(OntType relationType) {
      this.type = relationType;
      return me();
    }

    public abstract RelationType insert(KB updateProcessor) throws KBUpdateException;

    public void addRequiredEntityUpdate(KBEntity.UpdateBuilder update) {
      requiredEntityUpdates.add(update);
    }

    /**
     * Add a builder for a KBRelationArgument. This argument will be
     * inserted when this KBRelation is inserted.
     */
    public BuilderType addArgument(KBRelationArgument.InsertionBuilder argumentBuilder) {
      Preconditions.checkNotNull(argumentBuilder);
      arguments.add(argumentBuilder);
      return me();
    }

    protected abstract RelationType build(KB kb, boolean deferProvenances);

  }

  public final class UpdateBuilder extends AbstractUpdateBuilder<UpdateBuilder, KBRelation> {

    protected UpdateBuilder() {
      super();
    }

    @Override
    protected UpdateBuilder me() {
      return this;
    }

    @Override
    public KBRelation update(KB kb) throws KBUpdateException {
      Set<KBProvenance> oldProvenances = null;
      try {
        oldProvenances = KBRelation.this.getProvenances();
        checkProvenancesToRemove();
      } catch (KBQueryException e) {
        throw new KBUpdateException("Could not load provenances for original object", e);
      }
      kb.updateRelation(this);
      Set<KBProvenance> updatedProvenances = new HashSet<KBProvenance>();
      for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
        updatedProvenances.add(provenanceBuilder.build());
      }
      for (KBProvenance.UpdateBuilder provenanceBuilder : getProvenancesToReassign()) {
        updatedProvenances.add(provenanceBuilder.build());
      }
      for (KBProvenance kbProvenance : oldProvenances) {
        if (!getProvenancesToRemove().contains(kbProvenance)) {
          updatedProvenances.add(kbProvenance);
        }
      }
      Set<KBRelationArgument> arguments = new HashSet<KBRelationArgument>();
      for (KBRelationArgument.UpdateBuilder updatedArgument : getUpdatedArguments()) {
        arguments.add(updatedArgument.update(kb));
      }
      for (KBRelationArgument.InsertionBuilder argumentBuilder : getNewArguments()) {
        arguments.add(argumentBuilder.build(kb, argumentBuilder.kbid, false));
      }
      for (KBRelationArgument oldArgument : KBRelation.this.arguments) {
        boolean isAlreadyAdded = false;
        for (KBRelationArgument currentArgument : arguments) {
          if (currentArgument.getKBID().equals(oldArgument.getKBID())) {
            isAlreadyAdded = true;
            break;
          }
        }
        if (!isAlreadyAdded) {
          arguments.add(oldArgument);
        }
      }
      return new KBRelation(kb, KBRelation.this.getKBID(), relationType,
          getNewConfidence() != null ? getNewConfidence()
                                     : KBRelation.this.getConfidence(), arguments,
          Optional.of(updatedProvenances));
    }

  }

  /**
   * @author dkolas
   */
  public abstract class AbstractUpdateBuilder<BuilderType extends AbstractUpdateBuilder<BuilderType, RelationType>, RelationType extends KBRelation>
      extends KBPredicateArgument.UpdateBuilderWithConfidence<BuilderType, RelationType> {

    private List<KBRelationArgument.UpdateBuilder> updatedArguments =
        new ArrayList<KBRelationArgument.UpdateBuilder>();
    private Set<KBRelationArgument.InsertionBuilder> newArguments =
        new HashSet<KBRelationArgument.InsertionBuilder>();

    protected AbstractUpdateBuilder() {
    }

    public BuilderType addNewArgument(KBRelationArgument.InsertionBuilder argumentBuilder) {
      Preconditions.checkNotNull(argumentBuilder);
      newArguments.add(argumentBuilder);
      return me();
    }

    public Set<KBRelationArgument.InsertionBuilder> getNewArguments() {
      return newArguments;
    }

    public OntType getType() {
      return KBRelation.this.getType();
    }

    protected KBRelation getKBRelation() {
      return KBRelation.this;
    }

    public KBID getKBID() {
      return kbID;
    }

    public BuilderType addUpdatedArgument(KBRelationArgument.UpdateBuilder updatedArgument) {
      Preconditions.checkArgument(KBRelation.this.arguments.contains(updatedArgument
          .getKBRelationArgument()));
      updatedArguments.add(updatedArgument);
      return me();
    }

    protected List<KBRelationArgument.UpdateBuilder> getUpdatedArguments() {
      return updatedArguments;
    }

    public int getUpdatedProvenanceCount() throws KBQueryException {
      int numberOfProvenances = getNewProvenances().size()
          + getProvenancesToReassign().size();
      for (KBProvenance kbProvenance : KBRelation.this.getProvenances()) {
        if (!getProvenancesToRemove().contains(kbProvenance)) {
          numberOfProvenances++;
        }
      }
      return numberOfProvenances;
    }


  }
}

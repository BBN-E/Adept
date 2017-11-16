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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adept.common.Chunk;
import adept.common.DocumentEvent;
import adept.common.DocumentEventArgument;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.EventMentionArgument;
import adept.common.IType;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.Pair;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class KBEvent represents Events that have been saved in the KB. This class
 * extends {@link KBRelation}, and allows creation of insertion builders from
 * {@link DocumentEvent}.
 *
 * @author dkolas
 */
public class KBEvent extends KBRelation {

  private static Logger log = LoggerFactory.getLogger(KBEvent.class);
  private final ImmutableMap<OntType, Float> realisTypes;

  /**
   * Private constructor. To be called only from builders.
   */
  private KBEvent(KB kb, KBID kbID, OntType eventType, float confidence,
      Set<KBRelationArgument> arguments, Optional<Set<KBProvenance>> provenances,
      Map<OntType, Float> realisTypes) {
    super(kb, kbID, eventType, confidence, arguments, provenances);
    this.realisTypes = ImmutableMap.copyOf(realisTypes);
  }

  /**
   * Create a new InsertionBuilder with the type and confidence of this event.
   */
  public static InsertionBuilder eventInsertionBuilder(OntType eventType, float confidence) {
    return new InsertionBuilder(eventType, confidence);
  }

  /**
   * Create a new InsertionBuilder from a {@link DocumentEvent}.
   *
   * Arguments of the {@link DocumentEvent} must have already been inserted
   * into the KB, and the mapping from the DocumentEvent argument targets to
   * the KBPredicateArguments must be provided in the insertedArgumentMap.
   *
   * Provenances in the {@link DocumentEvent} will be copied into the builder.
   * Note that if a provenance for the DocumentEvent contains both an
   * EventMention and an EventText, and the scores of those objects are not
   * the same, the score of the EventMention will be used as the
   * {@link KBTextProvenance} confidence.
   */
  public static InsertionBuilder eventInsertionBuilder(DocumentEvent documentEvent,
      Map<Item, KBPredicateArgument> insertedArgumentMap, KBOntologyMap ontologyMap) {
    // Check preconditions
    Preconditions.checkArgument(documentEvent.getScore().isPresent(),
        "DocumentEvent must have a score (to be converted to confidence)");
    for (DocumentEventArgument argument : documentEvent.getArguments()) {
      Preconditions
          .checkArgument(
              argument.getFiller().asItem().isPresent(),
              "All argument targets of this DocumentEvent must be instances of Item. (Are you using an unsupported subclass of TemporalValue?)");
      Preconditions
          .checkNotNull(
              insertedArgumentMap.get(argument.getFiller().asItem().get()),
              "All arguments of this DocumentEvent must have already been inserted, and have their KBID in the insertedArgumentMap.");
      // Ensure that the ontology map contains a type for the argument
      Preconditions.checkArgument(
          ontologyMap.getKBRoleForType(documentEvent.getEventType(), argument.getRole())
              .isPresent(), "Ontology map must have a role for role "
              + argument.getRole().getType() + " on event type "
              + documentEvent.getEventType().getType());
    }
    Preconditions.checkArgument(ontologyMap.getKBTypeForType(documentEvent.getEventType())
        .isPresent(), "Ontology map must have entry for DocumentEvent type");
    OntType relationType = ontologyMap.getKBTypeForType(documentEvent.getEventType()).get();
    InsertionBuilder builder = new InsertionBuilder(relationType, documentEvent.getScore()
        .get());

    for (DocumentEventArgument argument : documentEvent.getArguments()) {
      // Presence of this already verified
      OntType role = ontologyMap.getKBRoleForType(documentEvent.getEventType(),
          argument.getRole()).get();
      Item argumentTarget = argument.getFiller().asItem().get();
      Preconditions
          .checkArgument(argument.getScore().isPresent(),
              "All DocumentEventArguments must have a score (to be converted into confidence)");
      KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
          .insertionBuilder(role, insertedArgumentMap.get(argumentTarget), argument
              .getScore().get());

      for (DocumentEventArgument.Provenance provenance : argument.getProvenances()) {
        EventMentionArgument eventMentionArgument = provenance.getEventMentionArgument();
        Preconditions
            .checkArgument(
                eventMentionArgument.getScore().isPresent(),
                "All EventMentionArgument provenances must have an associated score to be converted into confidence.");
        argumentBuilder.addProvenance(KBTextProvenance.builder(
            eventMentionArgument.getFiller(),
            eventMentionArgument.getScore().get()));
      }

      builder.addArgument(argumentBuilder);
    }

    for (DocumentEvent.Provenance provenance : documentEvent.getProvenances()) {
      // add provenances

      Preconditions
          .checkArgument(
              provenance.getEventText().getScore().isPresent(),
              "All EventTexts and EventMentions used as provenances of this event must have an associated score (to be converted into confidence)");
      float score = provenance.getEventText().getScore().get();
      if (provenance.getEventMention().isPresent()) {
        Preconditions
            .checkArgument(
                provenance.getEventMention().get().getScore().isPresent(),
                "All EventTexts and EventMentions used as provenances of this event must have an associated score (to be converted into confidence)");
        score = provenance.getEventMention().get().getScore().get();
      }
      ImmutableSet<Chunk> chunks = provenance.getEventText().getProvenanceChunks();

      for (Chunk chunk : chunks) {
        KBProvenance.InsertionBuilder provenanceBuilder = KBTextProvenance.builder(chunk,
            score);
        builder.addProvenance(provenanceBuilder);
      }
    }

    for (IType attribute : documentEvent.getScoredUnaryAttributes().keySet()) {
      Optional<OntType> realisType = ontologyMap.getKBTypeForType(attribute);
      if (realisType.isPresent()) {
        builder.addRealisType(realisType.get(), documentEvent.getScoredUnaryAttributes()
            .get(attribute));
      }
    }
    return builder;
  }

  /**
   * Create a new InsertionBuilder for insertion into the KB. THIS METHOD IS
   * FOR DOCUMENT RELATIONS THAT NEED TO BE CONVERTED INTO AN EVENT. Use
   * {@link KBOntologyMap#relationConvertsToEvent(IType)} to know if you need to
   * use this method. This method prepopulates a KBEvent.InsertionBuilder with
   * the content of a {@link DocumentRelation}. All argument targets of the
   * {@link DocumentRelation} must have been inserted into the KB already, and
   * they must be mapped from an {@link Item} to a {@link KBPredicateArgument}
   * in the insertedArgumentMap. All provenances on both the
   * {@link DocumentRelation} and {@link DocumentRelationArgument}s will be
   * added to the builder.
   *
   * The ontologyMap will be used to convert Types and Roles from the
   * DocumentRelation into the ontology of the KB. An exception will be thrown
   * if the types cannot be mapped.
   */
  public static InsertionBuilder eventInsertionBuilder(DocumentRelation documentRelation,
      Map<Item, KBPredicateArgument> insertedArgumentMap, KBOntologyMap ontologyMap) {
    checkArgument(ontologyMap.getKBTypeForType(documentRelation.getRelationType()).isPresent(),
        "Ontology map must have entry for DocumentRelation type");
    checkArgument(
        ontologyMap.relationConvertsToEvent(documentRelation.getRelationType()),
        "This relation type does not map to an event.  Please use KBRelation.relationInsertionBuilder(DocumentRelation documentRelation...)");
    OntType relationType = ontologyMap.getKBTypeForType(documentRelation.getRelationType())
        .get();
    InsertionBuilder builder = new InsertionBuilder(relationType,
        documentRelation.getConfidence());
    relationInsertionBuilder(builder, documentRelation, insertedArgumentMap, ontologyMap);
    return builder;
  }

  protected static InsertionBuilder eventInsertionBuilder() {
    return new InsertionBuilder();
  }

  /**
   * <p>
   * API to merge multiple KBEvents into a single KBEvent. Merging of KBEvents may be
   * required, for example, when trying to remove duplicate events (which point to the same
   * real-world event--in other words, have the same event-type, realis-type and the same set of
   * real-world objects, in the same role, as their arguments)
   * <p>
   * The event-type of all to-be-merged events should be the same. The
   * confidence of the merged event is a weighted average weighted by
   * number of provenances of the events being merged.
   * <p>
   * The provenances of all the contributing events are re-linked to the merged event.
   * Re-linking simply means updating the KBId field of the contributing provenances in the
   * TextProvenance table with the KBID of the merged event.
   * <p>
   * In this implementation, the merged-event is not a new KBEvent, but a KBEvent chosen
   * arbitrarily from the input list of KBEvents to merge. Specifically, it's the first
   * KBEvent in the list.
   * <p>
   * Currently, when merging KBEvents, we are not merging their arguments. Arguments of
   * events are supposed to be de-duplicated before merging the events. This should change
   * in a later version of the api.
   *
   * @param kbEventsToMerge {@code List<KBEvent>} of KBEvents to merge
   * @param kb              KB instance
   * @return the merged KBRelation
   */
  public static KBEvent mergeKBEvents(List<KBEvent> kbEventsToMerge, KB kb)
      throws KBUpdateException, KBQueryException {

    checkNotNull(kbEventsToMerge);
    checkArgument(!kbEventsToMerge.isEmpty(), "kbEventsToMerge list cannot be empty.");
    if (kbEventsToMerge.size() == 1) {
      return kbEventsToMerge.get(0);
    }

    KBEvent kbEventToRetain = null;
    double eventConfidenceTotalWeight = 0.0;
    float averageEventConfidence = 0.0f;
    Set<KBProvenance> allEventProvenances = new HashSet<>();
    Multimap<Pair<OntType, KBID>, KBProvenance> argumentProvenances = HashMultimap.create();
    Multimap<Pair<OntType, KBID>, Float> argumentConfidences = HashMultimap.create();

    Multimap<OntType, KBID> referenceRoleTargetKBIDMap = HashMultimap.create();
    for (KBEvent kbEvent : kbEventsToMerge) {
      log.info("KBEvent to merge: {}", kbEvent.getKBID().getObjectID());
      if (kbEvent.getArguments() == null || kbEvent.getArguments().size() == 0) {
        kbEvent = kb.getEventById(kbEvent.getKBID());
      }
      if (kbEventToRetain == null) {
        kbEventToRetain = kbEvent;
        fillReferenceMap(kbEventToRetain, referenceRoleTargetKBIDMap);
      }
      float weight = kbEvent.getProvenances().size();
      eventConfidenceTotalWeight += weight;
      //provenances
      allEventProvenances.addAll(kbEvent.getProvenances());
      //confidence
      averageEventConfidence += (kbEvent.getConfidence() * weight);
      for (KBRelationArgument kbArg : kbEvent.getArguments()) {
        OntType role = kbArg.getRole();
        KBID targetKBID = kbArg.getTarget().getKBID();
        if (!referenceRoleTargetKBIDMap.get(role).contains(targetKBID)) {
          throw new KBUpdateException(
              "Cannot merge events with different args; found targetKBID: " + targetKBID +
                  " for role: " + role.getType() + " for event: " + kbEvent.getKBID());
        }
        argumentConfidences
            .put(new Pair(role, targetKBID), kbArg.getConfidence() * kbArg.getProvenances().size());
        argumentProvenances.putAll(new Pair(role, targetKBID), kbArg.getProvenances());
      }
    }
    averageEventConfidence /= eventConfidenceTotalWeight;
    UpdateBuilder eventUpdateBuilder = createEventUpdateBuilder(kbEventToRetain,
        allEventProvenances, averageEventConfidence, argumentProvenances, argumentConfidences);

    KBEvent mergedKBEvent = eventUpdateBuilder.update(kb);
    log.info("Deleting duplicate KBEvents...");
    for (KBEvent kbEvent : kbEventsToMerge) {
      if (!kbEvent.getKBID().equals(mergedKBEvent.getKBID())) {
        for (KBRelationArgument kbRelationArgument : kbEvent.getArguments()) {
          log.info("Deleting KBEventArgument with KBID {}...",
              kbRelationArgument.getKBID().getObjectID());
          kb.deleteDuplicateKBObject(kbRelationArgument.getKBID());
        }
        log.info("Deleting KBEvent with KBID {}...", kbEvent.getKBID().getObjectID());
        kb.deleteDuplicateKBObject(kbEvent.getKBID());
      }
    }
    return mergedKBEvent;
  }

  private static UpdateBuilder createEventUpdateBuilder(KBEvent kbEventToRetain,
      Set<KBProvenance> allEventProvenances, float updatedEventConfidence,
      Multimap<Pair<OntType, KBID>, KBProvenance> argumentProvenances,
      Multimap<Pair<OntType, KBID>, Float>
          argumentConfidences) throws KBQueryException {
    //update confidence and provenances for relation
    UpdateBuilder eventUpdateBuilder = kbEventToRetain.updateBuilder();
    //update relation confidence
    eventUpdateBuilder.setConfidence(updatedEventConfidence);
    //updating relation provenances
    for (KBProvenance provenance : allEventProvenances) {
      if (!kbEventToRetain.getProvenances().contains(provenance)) {
        KBTextProvenance.UpdateBuilder provenanceUpdateBuilder =
            ((KBTextProvenance) provenance).getUpdateBuilder(kbEventToRetain.getKBID());
        log.info("Added provenance {}.... ", provenanceUpdateBuilder
            .getKBID().getObjectID());
        log.info("...to update with relation KBId: {}",
            provenanceUpdateBuilder.getSourceEntityKBID()
                .getObjectID());
        eventUpdateBuilder.addProvenanceToReassign(provenanceUpdateBuilder);
      }
    }
    //updating relation args
    for (KBRelationArgument kbArg : kbEventToRetain.getArguments()) {
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
      eventUpdateBuilder.addUpdatedArgument(argUpdateBuilder);
    }
    return eventUpdateBuilder;
  }

  private static void fillReferenceMap(KBEvent kbEvent, Multimap<OntType, KBID> referenceMap) {
    for (KBRelationArgument kbArg : kbEvent.getArguments()) {
      referenceMap.put(kbArg.getRole(), kbArg.getTarget().getKBID());
    }
  }

  /**
   * Get an UpdateBuilder for this event. Confidence and provenances may be
   * updated.
   */
  public UpdateBuilder updateBuilder() {
    return new UpdateBuilder();
  }

  /**
   * @return the realisTypes
   */
  public ImmutableMap<OntType, Float> getRealisTypes() {
    return realisTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof KBEvent)) {
      return false;
    }
    KBEvent that = (KBEvent) o;
    return super.equals(that) && Objects.equal(that.realisTypes, this.realisTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), realisTypes);
  }

  /**
   * The InsertionBuilder class for Events.
   *
   * @author dkolas
   */
  public static class InsertionBuilder extends
      KBRelation.AbstractInsertionBuilder<InsertionBuilder, KBEvent> {

    private HashMap<OntType, Float> realisTypes;

    private InsertionBuilder() {
      realisTypes = new HashMap<OntType, Float>();
    }

    private InsertionBuilder(OntType relationType, float confidence) {
      super(relationType, confidence);
      realisTypes = new HashMap<OntType, Float>();
    }

    public InsertionBuilder addPlaceArgument(KBEntity place, float confidence,
        Set<KBProvenance.InsertionBuilder> provenances) {
      KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
          .insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "place"),
              place, confidence);
      argumentBuilder.addProvenances(provenances);
      addArgument(argumentBuilder);
      return this;
    }

    public InsertionBuilder addTimeArgument(KBDate time, float confidence,
        Set<KBProvenance.InsertionBuilder> provenances) {
      KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
          .insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "time"),
              time, confidence);
      argumentBuilder.addProvenances(provenances);
      addArgument(argumentBuilder);
      return this;
    }

    @Override
    public KBEvent insert(KB kb) throws KBUpdateException {
      for (KBEntity.UpdateBuilder entityTypeUpdate : requiredEntityUpdates) {
        updatedEntities.put(entityTypeUpdate.getKBID(), entityTypeUpdate.update(kb));
      }
      kb.insertEvent(this);
      return build(kb, false);
    }

    public InsertionBuilder addRealisType(OntType realisType, float confidence) {
      realisTypes.put(realisType, confidence);
      return this;
    }

    protected KBEvent build(KB kb, boolean deferProvenances) {
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
      return new KBEvent(kb, kbid, getType(), getConfidence(), arguments, provenances,
          realisTypes);
    }

    protected InsertionBuilder me() {
      return this;
    }

    /**
     * @return
     */
    public HashMap<OntType, Float> getRealisTypes() {
      return realisTypes;
    }
  }

  /**
   * The UpdateBuilder class for events.
   *
   * Confidence and provenances may be updated.
   *
   * @author dkolas
   */
  public class UpdateBuilder extends KBRelation.AbstractUpdateBuilder<UpdateBuilder, KBEvent> {

    private HashMap<OntType, Float> updatedRealisTypes;

    private UpdateBuilder() {
      super();
      updatedRealisTypes = new HashMap<OntType, Float>(realisTypes);
    }

    @Override
    public KBEvent update(KB kb) throws KBUpdateException {
      Set<KBProvenance> oldProvenances = null;
      try {
        oldProvenances = getProvenances();
        checkProvenancesToRemove();
      } catch (KBQueryException e) {
        throw new KBUpdateException("Could not load provenances for original object", e);
      }
      kb.updateEvent(this);
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
      for (KBRelationArgument oldArgument : getKBRelation().getArguments()) {
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
      return new KBEvent(kb, getKBRelation().getKBID(), getType(),
          getNewConfidence() != null ? getNewConfidence() : getKBRelation()
              .getConfidence(), arguments, Optional.of(updatedProvenances), updatedRealisTypes);
    }

    protected UpdateBuilder me() {
      return this;
    }

    public UpdateBuilder addNewRealisType(OntType type, Float confidence) {
      Preconditions.checkArgument(!realisTypes.containsKey(type),
          "This entity already contains the given type");
      updatedRealisTypes.put(type, confidence);
      return me();
    }

    public UpdateBuilder removeRealisType(OntType type) {
      Preconditions.checkArgument(realisTypes.containsKey(type),
          "This entity does not contain the given type.");
      updatedRealisTypes.remove(type);
      return me();
    }

    public UpdateBuilder alterRealisTypeConfidence(OntType type, Float confidence) {
      Preconditions.checkArgument(realisTypes.containsKey(type),
          "This entity does not contain the given type.");
      updatedRealisTypes.put(type, confidence);
      return me();
    }

    public HashMap<OntType, Float> getUpdatedRealisTypes() {
      return updatedRealisTypes;
    }

  }
}

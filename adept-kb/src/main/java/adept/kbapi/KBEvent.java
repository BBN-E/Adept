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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.HashSet;
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

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Class KBEvent represents Events that have been saved in the KB. This class
 * extends {@link KBRelation}, and allows creation of insertion builders from
 * {@link DocumentEvent}.
 * 
 * @author dkolas
 */
public class KBEvent extends KBRelation {

	private final ImmutableMap<OntType, Float> realisTypes;

	/**
	 * Private constructor. To be called only from builders.
	 * 
	 * @param kbID
	 * @param eventType
	 * @param confidence
	 * @param arguments
	 * @param provenances
	 */
	private KBEvent(KBID kbID, OntType eventType, float confidence,
			Set<KBRelationArgument> arguments, Set<KBProvenance> provenances,
			Map<OntType, Float> realisTypes) {
		super(kbID, eventType, confidence, arguments, provenances);
		this.realisTypes = ImmutableMap.copyOf(realisTypes);
	}

	/**
	 * Create a new InsertionBuilder with the type and confidence of this event.
	 * 
	 * @param eventType
	 * @param confidence
	 * @return
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
	 * 
	 * @param documentEvent
	 * @param insertedArgumentMap
	 * @param ontologyMap
	 * @return
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
						eventMentionArgument.getFiller(), eventMentionArgument.getScore().get()));
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
	 * {@link KBOntologyMap.relationConvertsToEvent()} to know if you need to
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
	 * 
	 * @param documentRelation
	 * @param insertedArgumentMap
	 * @param ontologyMap
	 * @return
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
	 * Get an UpdateBuilder for this event. Confidence and provenances may be
	 * updated.
	 * 
	 * @return
	 * 
	 * @see adept.kbapi.model.KBRelation#updateBuilder()
	 */
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder();
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
			return build();
		}

		public InsertionBuilder addRealisType(OntType realisType, float confidence) {
			realisTypes.put(realisType, confidence);
			return this;
		}

		protected KBEvent build() {
			Preconditions.checkNotNull(kbid);
			Preconditions.checkArgument(getConfidence() >= 0);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			Set<KBRelationArgument> arguments = new HashSet<KBRelationArgument>();
			for (KBRelationArgument.InsertionBuilder argumentBuilder : getArguments()) {
				if (updatedEntities.containsKey(argumentBuilder.getTarget().getKBID())) {
					argumentBuilder.setTarget(updatedEntities.get(argumentBuilder.getTarget()
							.getKBID()));
				}
				arguments.add(argumentBuilder.build(argumentBuilder.kbid));
			}
			return new KBEvent(kbid, getType(), getConfidence(), arguments, provenances,
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
			kb.updateEvent(this);
			Set<KBProvenance> updatedProvenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				updatedProvenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : getProvenances()) {
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
			return new KBEvent(getKBRelation().getKBID(), getType(),
					getNewConfidence() != null ? getNewConfidence() : getKBRelation()
							.getConfidence(), arguments, updatedProvenances, updatedRealisTypes);
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
}

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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adept.common.Chunk;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Item;
import adept.common.KBID;
import adept.common.OntType;
import adept.common.RelationMention;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * The Class KBRelation represents a fully loaded, immutable relation.
 * 
 */
public class KBRelation extends KBPredicateArgument {

	/** The relation type. */
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
	 * @param kbUri
	 *            the kb uri
	 * @param relationType
	 *            the relation type
	 * @param sourceKB
	 *            the source KB
	 */
	protected KBRelation(KBID kbID, OntType relationType, float confidence,
			Set<KBRelationArgument> arguments, Set<KBProvenance> provenances) {
		super(kbID, provenances);

		checkArgument(relationType != null);
		this.relationType = relationType;
		this.confidence = confidence;
		this.arguments = ImmutableSet.copyOf(arguments);
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
	 * 
	 * @return
	 */
	public ImmutableSet<KBRelationArgument> getArguments() {
		return arguments;
	}

	/**
	 * 
	 * Get the subset of arguments of this relation that are of the specified
	 * role.
	 * 
	 * @param role
	 * @return
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
	 * Create a new InsertionBuilder for insertion into the KB. This method
	 * creates an InsertionBuilder with no arguments or provenances, which
	 * should be added before insertion.
	 * 
	 * @param relationType
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder relationInsertionBuilder(OntType relationType, float confidence) {
		return new InsertionBuilder(relationType, confidence);
	}

	/**
	 * Create a new InsertionBuilder for querying from the KB. This method
	 * should only be called internally.
	 * 
	 * @param type
	 * @param confidence
	 * @return
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
	 * @param documentRelation
	 * @param insertedArgumentMap
	 * @param ontologyMap
	 * @return
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
				if (mention.asEntityMention().isPresent()) {
					chunk = mention.asEntityMention().get();
				} else if (mention.asNumberPhrase().isPresent()) {
					chunk = mention.asNumberPhrase().get();
				} else if (mention.asTimePhrase().isPresent()) {
					chunk = mention.asTimePhrase().get();
				} else if (mention.asGenericChunk().isPresent()) {
                    chunk = mention.asGenericChunk().get();
                }
				argumentBuilder.addProvenance(KBTextProvenance.builder(chunk,
						mention.getConfidence()));
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
						mention.getJustification(), mention.getConfidence());
				builder.addProvenance(provenanceBuilder);
			}
		}
	}

	/**
	 * Create a new UpdateBuilder based on this object. Only confidences and
	 * provenances can be updated after a {@link KBRelation} is saved.
	 * 
	 * Note that {@link UpdateBuilder#update(KB)} returns a NEW KBrelation
	 * object, and this one will no longer be valid.
	 * 
	 * @return
	 * 
	 * @see adept.kbapi.model.KBPredicateArgument#updateBuilder()
	 */
	public AbstractUpdateBuilder<?, ?> updateBuilder() {
		return new UpdateBuilder();
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
			return build();
		}

		protected KBRelation build() {
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

			return new KBRelation(kbid, getType(), getConfidence(), arguments, provenances);
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

		private Set<KBRelationArgument.InsertionBuilder> arguments;

		protected Set<KBEntity.UpdateBuilder> requiredEntityUpdates;
		protected HashMap<KBID, KBEntity> updatedEntities;

		private OntType type;

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

		/**
		 * Create a new InsertionBuilder. This method intended to be called only
		 * internally. Please use static insertionBuilder methods on
		 * {@link KBRelation}
		 * 
		 * @param relationType
		 * @param confidence
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

		protected void addRequiredEntityUpdate(KBEntity.UpdateBuilder update) {
			requiredEntityUpdates.add(update);
		}

		/**
		 * Add a builder for a KBRelationArgument. This argument will be
		 * inserted when this KBRelation is inserted.
		 * 
		 * @param argumentBuilder
		 * @return
		 */
		public BuilderType addArgument(KBRelationArgument.InsertionBuilder argumentBuilder) {
			Preconditions.checkNotNull(argumentBuilder);
			arguments.add(argumentBuilder);
			return me();
		}

		protected abstract RelationType build();

	}

	public final class UpdateBuilder extends AbstractUpdateBuilder<UpdateBuilder, KBRelation> {

		/**
		 * @param kbRelation
		 */
		protected UpdateBuilder() {
			super();
		}

		@Override
		protected UpdateBuilder me() {
			return this;
		}

	}

	/**
	 * 
	 * @author dkolas
	 */
	protected abstract class AbstractUpdateBuilder<BuilderType extends AbstractUpdateBuilder<BuilderType, RelationType>, RelationType extends KBRelation>
			extends KBPredicateArgument.UpdateBuilderWithConfidence<BuilderType, RelationType> {
		private List<KBRelationArgument.UpdateBuilder> updatedArguments = new ArrayList<KBRelationArgument.UpdateBuilder>();
		private Set<KBRelationArgument.InsertionBuilder> newArguments = new HashSet<KBRelationArgument.InsertionBuilder>();

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

		protected AbstractUpdateBuilder() {
		}

		protected KBRelation getKBRelation() {
			return KBRelation.this;
		}

		@Override
		public KBID getKBID() {
			return KBRelation.this.getKBID();
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

		public int getUpdatedProvenanceCount() {
			int numberOfProvenances = getNewProvenances().size();
			for (KBProvenance kbProvenance : KBRelation.this.getProvenances()) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					numberOfProvenances++;
				}
			}
			return numberOfProvenances;
		}

		@Override
		public KBRelation update(KB kb) throws KBUpdateException {
			kb.updateRelation(this);
			Set<KBProvenance> updatedProvenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				updatedProvenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : KBRelation.this.getProvenances()) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					updatedProvenances.add(kbProvenance);
				}
			}
			Set<KBRelationArgument> arguments = new HashSet<KBRelationArgument>();
			for (KBRelationArgument.UpdateBuilder updatedArgument : updatedArguments) {
				arguments.add(updatedArgument.update(kb));
			}
			for (KBRelationArgument.InsertionBuilder argumentBuilder : newArguments) {
				arguments.add(argumentBuilder.build(argumentBuilder.kbid));
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
			return new KBRelation(KBRelation.this.getKBID(), relationType,
					getNewConfidence() != null ? getNewConfidence()
							: KBRelation.this.getConfidence(), arguments, updatedProvenances);
		}

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
}

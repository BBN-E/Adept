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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.Map;
import java.util.Set;

import adept.common.Chunk;
import adept.common.DocumentBelief;
import adept.common.DocumentMentalStateArgument;
import adept.common.EntityMention;
import adept.common.Item;
import adept.common.KBID;
import adept.common.MentalStateMention;
import adept.common.OntType;
import adept.common.RelationMention;
import adept.common.TimePhrase;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The class KBBelief represents a Belief relation within the KB. This class
 * extends KBRelation with the ability to easily add the arguments of a Belief
 * and build a {@link KBBelief} from a {@link DocumentBelief}.
 *
 */
public class KBBelief extends KBMentalState {
	public static final OntType beliefType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX,
			"Belief");

	/**
	 * Private constructor to be called only from builders.
	 *
	 * @param kbID
	 * @param confidence
	 * @param arguments
	 * @param provenances
	 */
	protected KBBelief(KB kb, KBID kbID, float confidence, Set<KBRelationArgument> arguments,
			Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, beliefType, confidence, arguments, provenances);
	}

	/**
	 * Get a new insertion builder, populated only with the belief's confidence.
	 *
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder beliefInsertionBuilder(float confidence) {
		return new InsertionBuilder(confidence);
	}

	/**
	 * Get a new insertion builder, copying information from the
	 * {@link DocumentBelief}.
	 *
	 * All arguments of the belief must have already been inserted into the KB
	 * and be present in the insertedArgumentMap, which maps Document level
	 * argument targets to KB level argument targets.
	 *
	 * Provenances will be copied from the {@link DocumentBelief} into the
	 * builder.
	 *
	 * @param documentBelief
	 * @param insertedArgumentMap
	 * @param ontologyMap
	 * @return
	 */
	public static InsertionBuilder beliefInsertionBuilder(DocumentBelief documentBelief,
			Map<Item, KBPredicateArgument> insertedArgumentMap, KBOntologyMap ontologyMap) {
		// Check preconditions
		for (DocumentMentalStateArgument argument : documentBelief.getArguments()) {
			Preconditions
					.checkArgument(
							argument.getFiller().asItem().isPresent(),
							"All argument targets of this DocumentRelation must be instances of Item. (Are you using an unsupported subclass of TemporalValue?)");
			Preconditions
					.checkNotNull(
							insertedArgumentMap.get(argument.getFiller().asItem().get()),
							"All arguments of this DocumentRelation must have already been inserted, and have their KBID in the insertedArgumentMap.");
		}
		KBBelief.InsertionBuilder builder = new KBBelief.InsertionBuilder(
				documentBelief.getConfidence());

		for (DocumentMentalStateArgument argument : documentBelief.getArguments()) {
			if (argument.getFiller().asStrength().isPresent()) {
				checkArgument(argument.getFiller().asStrength().get().asNumber().intValue() >= -2
						&& argument.getFiller().asStrength().get().asNumber().intValue() <= 2);
			}

			OntType role = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, argument.getRole()
					.getType());
			Item argumentTarget = argument.getFiller().asItem().get();
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(role, insertedArgumentMap.get(argumentTarget),
							argument.getConfidence());

			for (MentalStateMention.Filler mention : argument.getProvenances()) {
				Chunk chunk = null;
				String mentionType = null;
				if (mention.asEntityMention().isPresent()) {
					chunk = mention.asEntityMention().get();
					mentionType = ((EntityMention)chunk).getMentionType()
							.getType();
				} else if (mention.asStrength().isPresent()) {
					chunk = mention.asStrength().get();
				} else if (mention.asRelationMention().isPresent()) {
					chunk = mention.asRelationMention().get().getJustification();
					Optional<OntType> relMentionType = ontologyMap.getKBTypeForType((
							(RelationMention)mention.asRelationMention
							().get()).getRelationType());
					checkArgument(relMentionType.isPresent(),
							"Ontology map must have entry for all RelationMention types.");
					mentionType = relMentionType.get().getType();
				} else if (mention.asRelationArgument().isPresent()) {
					RelationMention.Filler relationArg = mention.asRelationArgument().get();
					if (relationArg.asEntityMention().isPresent()) {
						chunk = relationArg.asEntityMention().get();
						mentionType = ((EntityMention)chunk)
								.getMentionType().getType();
					} else if (relationArg.asNumberPhrase().isPresent()) {
						chunk = relationArg.asNumberPhrase().get();
					} else if (relationArg.asTimePhrase().isPresent()) {
						chunk = relationArg.asTimePhrase().get();
						mentionType = ((TimePhrase)chunk).getType();
					}
				}
				if (null != chunk)
					argumentBuilder.addProvenance(KBTextProvenance.builder(chunk,
						mention.getConfidence()));
			}

			builder.addArgument(argumentBuilder);
		}

		for (MentalStateMention mention : documentBelief.getProvenances()) {
			// add provenances
			if (mention.getJustification() != null) {
				KBProvenance.InsertionBuilder provenanceBuilder = KBTextProvenance.builder(
						mention.getJustification(), mention.getConfidence
								());
				builder.addProvenance(provenanceBuilder);
			}
		}
		return builder;
	}

	/**
	 * Class for inserting a KBBelief into the KB.
	 *
	 * @author dkolas
	 */
	public static class InsertionBuilder extends KBMentalState.InsertionBuilder<KBBelief> {
		protected InsertionBuilder(float confidence) {
			super(beliefType, confidence);
		}

		@Override
		protected KBBelief buildMentalState(KB kb, KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Optional<Set<KBProvenance>> provenances) {
			return new KBBelief(kb, kbid, confidence, arguments, provenances);
		}

		@Override
		protected KBMentalState.InsertionBuilder<KBBelief> me() {
			return this;
		}

	}

	/**
	 * Get an updateBuilder for this KBBelief. Provenances and the confidence
	 * may be updated.
	 *
	 * @return the new UpdateBuilder. May be chained.
	 *
	 */
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder();
	}

	/**
	 * Class for updating the provenances or confidence of this belief.
	 *
	 * @author dkolas
	 */
	public class UpdateBuilder extends KBMentalState.UpdateBuilder<KBBelief> {
		protected UpdateBuilder() {
			super();
		}

		/**
		 *
		 * @return
		 *
		 * @see adept.kbapi.KBPredicateArgument.UpdateBuilder#me()
		 */
		@Override
		protected adept.kbapi.KBMentalState.UpdateBuilder<KBBelief> me() {
			return this;
		}

		/**
		 * @param kb
		 * @param kbid
		 * @param confidence
		 * @param arguments
		 * @param provenances
		 * @return
		 *
		 * @see adept.kbapi.KBMentalState.UpdateBuilder#buildMentalState(KB, KBID, float, Set, Set)
		 */
		@Override
		protected KBBelief buildMentalState(KB kb, KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Set<KBProvenance> provenances) {
			return new KBBelief(kb, kbid, confidence, arguments, Optional.of(provenances));
		}
	}
}

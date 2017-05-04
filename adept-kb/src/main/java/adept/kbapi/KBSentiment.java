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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Set;

import adept.common.Chunk;
import adept.common.DocumentMentalStateArgument;
import adept.common.DocumentSentiment;
import adept.common.Item;
import adept.common.KBID;
import adept.common.MentalStateMention;
import adept.common.OntType;
import adept.common.RelationMention;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * The Class KBSentiment represents a pointer to a Knowledge Base Entity
 * uniquely identified by KB URI and source KB type.
 */
public class KBSentiment extends KBMentalState {
	public static final OntType sentimentType = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX,
			"Sentiment");

	/**
	 * Private constructor to be called only from builders.
	 * 
	 * @param kbID
	 * @param confidence
	 * @param arguments
	 * @param provenances
	 */
	protected KBSentiment(KB kb, KBID kbID, float confidence, Set<KBRelationArgument> arguments,
			Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, sentimentType, confidence, arguments, provenances);
	}

	/**
	 * Get a new insertion builder, populated only with the sentiment's
	 * confidence.
	 * 
	 * @param confidence
	 * @return
	 */
	public static InsertionBuilder sentimentInsertionBuilder(float confidence) {
		return new InsertionBuilder(confidence);
	}

	/**
	 * Get a new insertion builder, copying information from the
	 * {@link DocumentSentiment}.
	 * 
	 * All arguments of the sentiment must have already been inserted into the
	 * KB and be present in the insertedArgumentMap, which maps Document level
	 * argument targets to KB level argument targets.
	 * 
	 * Provenances will be copied from the {@link DocumentSentiment} into the
	 * builder.
	 * 
	 * @param documentSentiment
	 * @param insertedArgumentMap
	 * @param ontologyMap
	 * @return
	 */
	public static InsertionBuilder sentimentInsertionBuilder(DocumentSentiment documentSentiment,
			Map<Item, KBPredicateArgument> insertedArgumentMap, KBOntologyMap ontologyMap) {
		// Check preconditions
		for (DocumentMentalStateArgument argument : documentSentiment.getArguments()) {
			Preconditions
					.checkArgument(
							argument.getFiller().asItem().isPresent(),
							"All argument targets of this DocumentRelation must be instances of Item. (Are you using an unsupported subclass of TemporalValue?)");
			Preconditions
					.checkNotNull(
							insertedArgumentMap.get(argument.getFiller().asItem().get()),
							"All arguments of this DocumentRelation must have already been inserted, and have their KBID in the insertedArgumentMap.");
		}
		KBSentiment.InsertionBuilder builder = new KBSentiment.InsertionBuilder(
				documentSentiment.getConfidence());

		for (DocumentMentalStateArgument argument : documentSentiment.getArguments()) {
			if (argument.getFiller().asStrength().isPresent()) {
				checkArgument(argument.getFiller().asStrength().get().asNumber().intValue() >= -3
						&& argument.getFiller().asStrength().get().asNumber().intValue() <= 3);
			}

			OntType role = new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, argument.getRole()
					.getType());
			Item argumentTarget = argument.getFiller().asItem().get();
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(role, insertedArgumentMap.get(argumentTarget),
							argument.getConfidence());

			for (MentalStateMention.Filler mention : argument.getProvenances()) {
				Chunk chunk = null;
				if (mention.asEntityMention().isPresent()) {
					chunk = mention.asEntityMention().get();
				} else if (mention.asStrength().isPresent()) {
					chunk = mention.asStrength().get();
				} else if (mention.asRelationMention().isPresent()) {
					chunk = mention.asRelationMention().get().getJustification();
				} else if (mention.asRelationArgument().isPresent()) {
					RelationMention.Filler relationArg = mention.asRelationArgument().get();
					if (relationArg.asEntityMention().isPresent()) {
						chunk = relationArg.asEntityMention().get();
					} else if (relationArg.asNumberPhrase().isPresent()) {
						chunk = relationArg.asNumberPhrase().get();
					} else if (relationArg.asTimePhrase().isPresent()) {
						chunk = relationArg.asTimePhrase().get();
					}
				}
				if (null != chunk) 
					argumentBuilder.addProvenance(KBTextProvenance.builder(chunk,
						mention.getConfidence()));
			}

			builder.addArgument(argumentBuilder);
		}

		for (MentalStateMention mention : documentSentiment.getProvenances()) {
			// add provenances
			if (mention.getJustification() != null) {
				KBProvenance.InsertionBuilder provenanceBuilder = KBTextProvenance.builder(
						mention.getJustification(), mention.getConfidence());
				builder.addProvenance(provenanceBuilder);
			}
		}
		return builder;
	}

	/**
	 * Class for inserting a KBSentiment into the KB.
	 * 
	 * @author dkolas
	 */
	public static class InsertionBuilder extends KBMentalState.InsertionBuilder<KBSentiment> {
		protected InsertionBuilder(float confidence) {
			super(sentimentType, confidence);
		}

		@Override
		protected KBSentiment buildMentalState(KB kb, KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Optional<Set<KBProvenance>> provenances) {
			return new KBSentiment(kb, kbid, confidence, arguments, provenances);
		}

		@Override
		protected adept.kbapi.KBMentalState.InsertionBuilder<KBSentiment> me() {
			return this;
		}

	}

	/**
	 * Get an updateBuilder for this KBSentiment. Provenances and the confidence
	 * may be updated.
	 * 
	 * @return
	 * 
	 */
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder();
	}

	/**
	 * Class for updating the provenances or confidence of this sentiment.
	 * 
	 * @author dkolas
	 */
	public class UpdateBuilder extends KBMentalState.UpdateBuilder<KBSentiment> {
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
		protected adept.kbapi.KBMentalState.UpdateBuilder<KBSentiment> me() {
			return this;
		}

		/**
		 * 
		 * @param kb
		 * @param kbid
		 * @param confidence
		 * @param arguments
		 * @param provenances
		 * @return
		 * 
		 */
		@Override
		protected KBSentiment buildMentalState(KB kb, KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Set<KBProvenance> provenances) {
			return new KBSentiment(kb, kbid, confidence, arguments, Optional.of(provenances));
		}
	}

}

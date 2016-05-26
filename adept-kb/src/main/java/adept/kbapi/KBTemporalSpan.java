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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adept.common.KBID;
import adept.common.OntType;
import adept.common.TemporalSpan;
import adept.common.TemporalValue;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * 
 * @author dkolas
 */
public class KBTemporalSpan extends KBRelation {
	public static final OntType temporalSpanType = new OntType(
			KBOntologyModel.ONTOLOGY_BASE_PREFIX, "TemporalSpan");

	private KBTemporalSpan(KBID kbID, float confidence, Set<KBRelationArgument> arguments,
			Set<KBProvenance> provenances) {
		super(kbID, temporalSpanType, confidence, arguments, provenances);
	}

	public static InsertionBuilder temporalSpanInsertionBuilder(float confidence) {
		return new InsertionBuilder(confidence);
	}

	public static InsertionBuilder temporalSpanInsertionBuilder(TemporalSpan temporalSpan,
			float confidence, Map<TemporalValue, KBDate> insertedArgumentMap,
			Optional<Float> beginDateConfidence, Optional<Float> endDateConfidence) {
		Preconditions.checkNotNull(temporalSpan);
		Preconditions.checkNotNull(insertedArgumentMap);
		Preconditions.checkArgument(!insertedArgumentMap.isEmpty());

		Optional<TemporalValue> beginDate = temporalSpan.getBeginDate();
		Optional<TemporalValue> endDate = temporalSpan.getEndDate();

		Preconditions.checkArgument(!beginDate.isPresent()
				|| insertedArgumentMap.containsKey(beginDate.get())
				|| !beginDateConfidence.isPresent());
		Preconditions
				.checkArgument(!endDate.isPresent()
						|| insertedArgumentMap.containsKey(endDate.get())
						|| !endDateConfidence.isPresent());
		Preconditions.checkArgument(beginDate.isPresent() || endDate.isPresent());

		KBTemporalSpan.InsertionBuilder insertionBuilder = new InsertionBuilder(confidence);
		if (beginDate.isPresent()) {
			insertionBuilder.addBeginDateArgument(insertedArgumentMap.get(beginDate.get()),
					beginDateConfidence.get(), new HashSet<KBProvenance.InsertionBuilder>());
		}
		if (endDate.isPresent()) {
			insertionBuilder.addEndDateArgument(insertedArgumentMap.get(endDate.get()),
					endDateConfidence.get(), new HashSet<KBProvenance.InsertionBuilder>());
		}

		return insertionBuilder;
	}

	public static class InsertionBuilder extends
			KBRelation.AbstractInsertionBuilder<InsertionBuilder, KBTemporalSpan> {

		protected InsertionBuilder(float confidence) {
			super(temporalSpanType, confidence);
		}

		protected InsertionBuilder me() {
			return this;
		}

		public InsertionBuilder addBeginDateArgument(KBDate beginDate, float confidence,
				Set<KBProvenance.InsertionBuilder> provenances) {
			createBeginDateArgument(beginDate, confidence, provenances);
			return me();
		}

		/**
		 * @param beginDate
		 * @param confidence
		 * @param provenances
		 */
		protected KBRelationArgument.InsertionBuilder createBeginDateArgument(KBDate beginDate,
				float confidence, Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder beginDateArgument = KBRelationArgument
					.insertionBuilder(
							new OntType(KBOntologyModel.ONTOLOGY_BASE_PREFIX, "beginDate"),
							beginDate, confidence);
			beginDateArgument.setKBID(beginDate.getKBID());
			beginDateArgument.addProvenances(provenances);
			addArgument(beginDateArgument);
			return beginDateArgument;
		}

		public InsertionBuilder addEndDateArgument(KBDate endDate, float confidence,
				Set<KBProvenance.InsertionBuilder> provenances) {
			createEndDateArgument(endDate, confidence, provenances);
			return me();
		}

		/**
		 * @param endDate
		 * @param confidence
		 * @param provenances
		 */
		protected KBRelationArgument.InsertionBuilder createEndDateArgument(KBDate endDate,
				float confidence, Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder endDateArgument = KBRelationArgument
					.insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_BASE_PREFIX, "endDate"),
							endDate, confidence);
			endDateArgument.setKBID(endDate.getKBID());
			endDateArgument.addProvenances(provenances);
			addArgument(endDateArgument);
			return endDateArgument;
		}

		@Override
		public KBTemporalSpan insert(KB kb) throws KBUpdateException {
			kb.insertRelation(this);
			return build();
		}

		protected KBTemporalSpan build() {
			Preconditions.checkNotNull(kbid);
			Preconditions.checkArgument(getConfidence() >= 0);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			Set<KBRelationArgument> arguments = new HashSet<KBRelationArgument>();
			for (KBRelationArgument.InsertionBuilder argumentBuilder : getArguments()) {
				arguments.add(argumentBuilder.build(argumentBuilder.kbid));
			}

			return new KBTemporalSpan(kbid, getConfidence(), arguments, provenances);
		}

		/**
		 * @param build
		 * @param i
		 * @param hashSet
		 * @param beginDateStatementId
		 */
		protected void addBeginDateArgument(KBDate build, int i, HashSet<InsertionBuilder> hashSet,
				String beginDateStatementId) {
		}
	}

	/**
	 * Get an updateBuilder for this temporal span. Only provenances are
	 * updatable.
	 * 
	 * @return
	 * 
	 * @see adept.kbapi.KBRelation#updateBuilder()
	 */
	public KBTemporalSpan.UpdateBuilder updateBuilder() {
		return new KBTemporalSpan.UpdateBuilder();
	}

	public class UpdateBuilder extends
			KBRelation.AbstractUpdateBuilder<UpdateBuilder, KBTemporalSpan> {

		private UpdateBuilder() {
			super();
		}

		@Override
		public KBTemporalSpan update(KB kb) throws KBUpdateException {
			kb.updateKBPredicateArgumentProvenances(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : getKBRelation().getProvenances()) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBTemporalSpan(getKBRelation().getKBID(), getConfidence(), getArguments(),
					provenances);
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
}

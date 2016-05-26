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
import java.util.Set;

import adept.common.KBID;
import adept.common.OntType;

import com.google.common.base.Preconditions;

/**
 * 
 * @author dkolas
 */
public abstract class KBMentalState extends KBRelation {

	protected KBMentalState(KBID kbID, OntType eventType, float confidence,
			Set<KBRelationArgument> arguments, Set<KBProvenance> provenances) {
		super(kbID, eventType, confidence, arguments, provenances);
	}

	public abstract static class InsertionBuilder<T extends KBMentalState> extends
			KBRelation.AbstractInsertionBuilder<InsertionBuilder<T>, KBMentalState> {

		protected InsertionBuilder(OntType relationType, float confidence) {
			super(relationType, confidence);
		}

		public InsertionBuilder<T> addSourceArgument(KBEntity source, float confidence,
				Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "source"),
							source, confidence);
			argumentBuilder.addProvenances(provenances);
			addArgument(argumentBuilder);
			return this;
		}

		public InsertionBuilder<T> addStrengthArgument(KBNumber strength, float confidence,
				Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(
							new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "strength"),
							strength, confidence);
			argumentBuilder.addProvenances(provenances);
			addArgument(argumentBuilder);
			return this;
		}

		public InsertionBuilder<T> addTargetArgument(KBPredicateArgument target, float confidence,
				Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "target"),
							target, confidence);
			argumentBuilder.addProvenances(provenances);
			addArgument(argumentBuilder);
			return this;
		}

		public InsertionBuilder<T> addValidTemporalSpanArgument(KBTemporalSpan span,
				float confidence, Set<KBProvenance.InsertionBuilder> provenances) {
			KBRelationArgument.InsertionBuilder argumentBuilder = KBRelationArgument
					.insertionBuilder(new OntType(KBOntologyModel.ONTOLOGY_BASE_PREFIX,
							"validTemporalSpan"), span, confidence);
			argumentBuilder.addProvenances(provenances);
			addArgument(argumentBuilder);
			return this;
		}

		@Override
		public T insert(KB kb) throws KBUpdateException {
			kb.insertRelation(this);
			return build();
		}

		protected T build() {
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

			return buildMentalState(kbid, getConfidence(), arguments, provenances);
		}

		protected abstract T buildMentalState(KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Set<KBProvenance> provenances);
	}

	public abstract class UpdateBuilder<T extends KBMentalState> extends
			KBRelation.AbstractUpdateBuilder<UpdateBuilder<T>, T> {
		public UpdateBuilder() {
			super();
		}

		@Override
		public T update(KB kb) throws KBUpdateException {
			kb.updateRelation(this);
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
			return buildMentalState(getKBRelation().getKBID(),
					getNewConfidence() != null ? getNewConfidence() : getKBRelation()
							.getConfidence(), arguments, updatedProvenances);
		}

		protected abstract T buildMentalState(KBID kbid, float confidence,
				Set<KBRelationArgument> arguments, Set<KBProvenance> provenances);
	}

}

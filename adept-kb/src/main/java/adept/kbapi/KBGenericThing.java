/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adept.common.Chunk;
import adept.common.GenericThing;
import adept.common.KBID;
import adept.common.OntType;
import edu.stanford.nlp.util.Sets;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class for representing KB things which are mainly represented by a class and
 * a string reference, i.e, Crimes, Sentences, Weapons, etc.
 *
 * @author dkolas
 */
public class KBGenericThing extends KBThing {
	/**
	 * The ontology type
	 */
	private final OntType type;

	//Both provenanceCount and documentCount are Optional, only for
	// backwards compatibility
	private final Optional<Integer> provenanceCount;

	private final Optional<Integer> documentCount;

	/**
	 * Private constructor to be called by the builders
	 *
	 * @param kbID
	 * @param type
	 * @param canonicalString
	 * @param provenances
	 */
	private KBGenericThing(KB kb, KBID kbID, OntType type, String canonicalString,
			Optional<Set<KBProvenance>> provenances, Optional<Integer>
			provenanceCount, Optional<Integer> documentCount) {
		super(kb, kbID, provenances, canonicalString);
		checkNotNull(type);
		this.type = type;
		checkArgument(!provenanceCount.isPresent()||provenanceCount
				.get()>=0);
		this.provenanceCount = provenanceCount;
		checkArgument(!documentCount.isPresent()||documentCount
				.get()>=0);
		this.documentCount = documentCount;
	}

	/**
	 * Get the ontology type.
	 *
	 * @return
	 */
	public OntType getType() {
		return type;
	}

	public Optional<Integer> getProvenanceCount(){
		return provenanceCount;
	}

	public Optional<Integer> getDocumentCount(){
		return documentCount;
	}

	/**
	 * Get an insertionBuilder for a given {@link OntType} and string
	 * value. Deprecated; use
	 *
	 * @param type
	 * @param canonicalString
	 * @return
	 *
	 */
	public static InsertionBuilder genericThingInsertionBuilder(OntType type, String canonicalString) {
		return new InsertionBuilder(type, canonicalString);
	}

	/**
	 * Get an insertionBuilder for a given {@link GenericThing}. Chunks passed
	 * in will be used as provenances.
	 *
	 * @param genericThing
	 * @param provenances
	 * @param ontologyMap
	 * @return
	 */
	public static InsertionBuilder genericThingInsertionBuilder(GenericThing genericThing,
			List<Chunk> provenances, KBOntologyMap ontologyMap) {
		checkNotNull(genericThing);
		checkNotNull(provenances);
		checkNotNull(ontologyMap);

		Optional<OntType> ontType = ontologyMap.getKBTypeForType(genericThing.getType());

        // Because of duplicate sentence keys in the RichERE map, we have to hard code an exception here.
        if (ontType.isPresent() && ontType.get().getType().equals("Sentencing") && genericThing.getType().getType().equalsIgnoreCase("sentence")) {
            ontType = Optional.fromNullable(new OntType(KBOntologyModel.ONTOLOGY_CORE_PREFIX, "Sentence"));
        }

		Preconditions.checkArgument(ontType.isPresent(),
				"Ontology map must contain a value for the type of the GenericThing: "
						+ genericThing.getType().getType());

		InsertionBuilder result = new InsertionBuilder(ontType.get(), genericThing.getValue());
		for (Chunk chunk : provenances) {
			result.addProvenance(KBTextProvenance.builder(chunk, 1f));
		}
		return result;
	}

	/**
	 * InsertionBuilder class for inserting a {@link KBGenericThing}
	 *
	 * @author dkolas
	 */
	public static class InsertionBuilder extends
			KBPredicateArgument.InsertionBuilder<InsertionBuilder, KBGenericThing> {

		private final OntType type;
		private final String canonicalString;

		/**
		 * @param type
		 * @param canonicalString
		 */
		private InsertionBuilder(OntType type, String canonicalString) {
			checkNotNull(type);
			checkNotNull(canonicalString);
			this.type = type;
			this.canonicalString = canonicalString;
		}

		public Set<String> getDocumentIDsFromProvenances(){
			return KBTextProvenance
					.InsertionBuilder
					.getDocumentIDsFromProvenanceBuilders
							(getProvenances());
		}

		/**
		 * Save this KBGenericThing into the KB.
		 *
		 * @param kb
		 * @return
		 * @throws KBUpdateException
		 *
		 */
		@Override
		public KBGenericThing insert(KB kb) throws KBUpdateException {
			kb.insertGenericThing(this);
			Set<String> documentIDs =
					getDocumentIDsFromProvenances();
			return build(kb, false,Optional.of(getProvenances()
					.size()),Optional.of(documentIDs.size()));
		}

		protected KBGenericThing build(KB kb, boolean deferProvenances,
				Optional<Integer> provenanceCount,
				Optional<Integer> documentCount) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBGenericThing(kb, kbid, type,
					canonicalString, provenances,
					provenanceCount,documentCount);
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

		/**
		 * @return
		 */
		public OntType getType() {
			return type;
		}

		/**
		 * @return
		 */
		public String getCanonicalString() {
			return canonicalString;
		}
	}

	/**
	 * Return an UpdateBuilder for this generic thing Only provenances may be
	 * updated.
	 *
	 * @return
	 *
	 */
	@Override
	public UpdateBuilder updateBuilder() {
		return new UpdateBuilder(this);
	}

	public class UpdateBuilder extends
			KBPredicateArgument.UpdateBuilder<UpdateBuilder, KBGenericThing> {
		private KBGenericThing kbGenericThing = null;

		public UpdateBuilder(KBGenericThing kbGenericThing) {
			this.kbGenericThing = kbGenericThing;
		}

		public int getUpdatedProvenanceCount() throws KBQueryException{
			return getProvenances().size() + getNewProvenances().size() - getProvenancesToRemove().size();
		}

		public int getUpdatedDocumentCount() throws KBQueryException{
			Set<KBProvenance> provenancesToRetain =
					Sets.diff(getProvenances(),
							getProvenancesToRemove());
			Set<String> updatedDocIDs = KBTextProvenance
					.getDocumentIDsFromProvenances
							(provenancesToRetain);
			updatedDocIDs.addAll(KBTextProvenance
					.InsertionBuilder
					.getDocumentIDsFromProvenanceBuilders
							(getNewProvenances()));
			return updatedDocIDs.size();

		}

		@Override
		public KBGenericThing update(KB kb) throws KBUpdateException {
			Set<KBProvenance> oldProvenances = null;
			try{
				oldProvenances = getProvenances();
				checkProvenancesToRemove();
			}catch(KBQueryException e){
				throw new KBUpdateException("Could not load provenances for original object",e);
			}
			kb.updateKBPredicateArgumentProvenances(this);
			kb.updateGenericThingProvenanceAndDocumentCounts(this);
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : oldProvenances) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			Set<String> documentIds = KBTextProvenance
					.getDocumentIDsFromProvenances
							(provenances);

			return new KBGenericThing(kb, getKBID(), getType(),
					this.kbGenericThing.getCanonicalString(), Optional.of
					(provenances),
					Optional.of(provenances
							.size()),Optional.of(documentIds.size
					()));
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
		if (!(o instanceof KBGenericThing)) {
			return false;
		}
		KBGenericThing that = (KBGenericThing) o;
		return super.equals(that) && Objects.equal(that.type, this.type);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(super.hashCode(), type);
	}
}
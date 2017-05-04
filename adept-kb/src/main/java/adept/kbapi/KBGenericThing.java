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


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adept.common.Chunk;
import adept.common.GenericThing;
import adept.common.KBID;
import adept.common.OntType;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

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

	/**
	 * Private constructor to be called by the builders
	 * 
	 * @param kbID
	 * @param type
	 * @param canonicalString
	 * @param provenances
	 */
	private KBGenericThing(KB kb, KBID kbID, OntType type, String canonicalString,
			Optional<Set<KBProvenance>> provenances) {
		super(kb, kbID, provenances, canonicalString);
		Preconditions.checkNotNull(type);
		this.type = type;
	}

	/**
	 * Get the ontology type.
	 * 
	 * @return
	 */
	public OntType getType() {
		return type;
	}

	/**
	 * Get an insertionBuilder for a given {@link OntType} and string value.
	 * 
	 * @param type
	 * @param canonicalString
	 * @return
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
		Preconditions.checkNotNull(genericThing);
		Preconditions.checkNotNull(provenances);
		Preconditions.checkNotNull(ontologyMap);

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
			Preconditions.checkNotNull(type);
			Preconditions.checkNotNull(canonicalString);
			this.type = type;
			this.canonicalString = canonicalString;
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
			return build(kb, false);
		}

		protected KBGenericThing build(KB kb, boolean deferProvenances) {
			Optional<Set<KBProvenance>> provenances = buildProvenances(deferProvenances);
			return new KBGenericThing(kb, kbid, type, canonicalString, provenances);
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
			Set<KBProvenance> provenances = new HashSet<KBProvenance>();
			for (KBProvenance.InsertionBuilder provenanceBuilder : getNewProvenances()) {
				provenances.add(provenanceBuilder.build());
			}
			for (KBProvenance kbProvenance : oldProvenances) {
				if (!getProvenancesToRemove().contains(kbProvenance)) {
					provenances.add(kbProvenance);
				}
			}
			return new KBGenericThing(kb, getKBID(), getType(), getCanonicalString(), Optional.of(provenances));
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

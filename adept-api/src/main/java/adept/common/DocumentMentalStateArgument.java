package adept.common;

/*-
 * #%L
 * adept-api
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
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;
import java.util.Map;

public class DocumentMentalStateArgument extends HltContent implements HasScoredUnaryAttributes, Serializable {

	private static final long serialVersionUID = -6012702623129668755L;
	private final IType role;
    private final DocumentMentalStateArgument.Filler filler;
    private final ImmutableMap<IType, Float> attributes;
    private ImmutableSet<? extends MentalStateMention.Filler> provenances;
    private final float confidence;
    
    private DocumentMentalStateArgument(IType role, DocumentMentalStateArgument.Filler filler,
    		ImmutableSet<? extends MentalStateMention.Filler> provenances, ImmutableMap<IType, Float> attributes,float confidence) {
        this.role = checkNotNull(role);

        this.filler = checkNotNull(filler);

        this.provenances = ImmutableSet.copyOf(provenances);

        this.attributes = ImmutableMap.copyOf(attributes);

        this.confidence = confidence;
    }
        
    public float getConfidence() {
        return confidence;
    }
    
    /**
     *
     * @return {@code Will never be null}.
     */
    public IType getRole() {
        return role;
    }
    
    @Override
    public Map<IType, Float> getScoredUnaryAttributes() {
        return attributes;
    }
    
    /**
     * The entity or value filling this argument.
     * @return Will never be {@code null}.
     */
    public DocumentMentalStateArgument.Filler getFiller() {
        return filler;
    }

    /**
     * @return May be empty. May not contain {@code null}. Provenances may
     * have different relation types than this argument.
     */
    public ImmutableSet<? extends MentalStateMention.Filler> getProvenances() {
        return provenances;
    }
    
    /**
     * Creates a builder to create a relation argument of the specified type.
     * @param role May not be {@code null}.
     * @param entity May not be {@code null}.
     * @param confidence the confidence for this new mention
     * @return the new Builder. Useful for chaining.
     */
    public static Builder builder(IType role, Filler entity, float confidence) {
        return new Builder(role, entity, confidence);
    }
    
    public static final class Builder {
        private final IType role;
        private final Filler filler;        
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private ImmutableSet.Builder<MentalStateMention.Filler> provenances = ImmutableSet.builder();
        private float confidence = 1.0f;
        
        private Builder(IType role, Filler entity, float confidence) {
            this.role = checkNotNull(role);
            this.filler = checkNotNull(entity);
            this.confidence = confidence;
        }

        public Builder setConfidence(float confidence) {
            this.confidence = confidence;
            return this;
        }

        public Builder setAttributes(Map<? extends IType, Float> attributes) {
        	checkArgument(attributes!=null);
        	for (final Map.Entry<? extends IType, Float> entry : attributes.entrySet()) {
           	checkArgument(entry.getKey()!=null);
            checkArgument(entry.getValue()!=null);
          }  
          this.attributes.putAll(attributes);
          return this;
        }

        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;
        }

        public Builder addProvenances(Iterable<MentalStateMention.Filler> provenances) {
            checkArgument(provenances!=null);
            for (final MentalStateMention.Filler arg : provenances) {
                checkArgument(arg!=null);
            }          	
            this.provenances.addAll(provenances);
            return this;        
        }

        public Builder addProvenance(MentalStateMention.Filler provenance) {
            checkArgument(provenance!=null);
            this.provenances.add(provenance);
            return this;        
        }

        public DocumentMentalStateArgument build() {
            return new DocumentMentalStateArgument(role, filler,
            		provenances.build(), attributes.build(), confidence);
        }
    }

    /**
     * A filler for a role in a document-level mental state.  Such role fillers may be of a limited
     * number of different types, currently {@link Entity}, {@link NumericValue}, and
     * {@link TemporalValue}. This class represents that uncertainty. The available
     * types may expand in the future.  Please see {@link DocumentMentalState} for a full
     * description of available relation classes.
     *
     * This class is locally immutable.
     */
    public static final class Filler implements Serializable {
		private static final long serialVersionUID = 7228313063217427861L;
		private final Optional<Entity> entity;
        private final Optional<NumericValue> strength;
    	private final Optional<DocumentRelation> documentRelation;        
        private final Optional<DocumentRelationArgument> documentRelationArgument;
        private final Optional<DocumentEvent> documentEvent;        
        private final Optional<DocumentEventArgument> documentEventArgument;
        private final Optional<TemporalSpan> temporalSpan;
    	        
        private Filler(Entity entityMention, NumericValue strength, DocumentRelation relationMention, 
                DocumentRelationArgument relationArgument, TemporalSpan temporalSpanArgument, DocumentEvent documentEvent, DocumentEventArgument documentEventArgument) {	
            this.entity = Optional.fromNullable(entityMention);
            this.strength = Optional.fromNullable(strength);
            this.documentRelation = Optional.fromNullable(relationMention);
            this.documentRelationArgument = Optional.fromNullable(relationArgument);
            this.temporalSpan = Optional.fromNullable(temporalSpanArgument);
            this.documentEvent = Optional.fromNullable(documentEvent);
            this.documentEventArgument = Optional.fromNullable(documentEventArgument);
        }

        public Optional<Entity> asEntity() {
            return entity;
        }
        
        public Optional<TemporalSpan> asTemporalSpan() {
            return temporalSpan;
        }
        
        public Optional<NumericValue> asStrength() {
            return strength;
        }

        public Optional<DocumentRelation> asDocumentRelation() {
            return documentRelation;
        }
        
        public Optional<DocumentRelationArgument> asDocumentRelationArgument() {
            return documentRelationArgument;
        }
        
        public Optional<DocumentEvent> asDocumentEvent() {
            return documentEvent;
        }
        
        public Optional<DocumentEventArgument> asDocumentEventArgument() {
            return documentEventArgument;
        }
        
        

        public static Filler createSourceArgument(Entity entity) {
            return new Filler(checkNotNull(entity), null, null, null, null, null, null);
        }
        
        public static Filler createStrengthArgument(NumericValue strength) {
            return new Filler(null, strength, null, null, null, null, null);
        }
        
        public static Filler createTargetArgument(Entity entity) {
            return new Filler(checkNotNull(entity), null, null, null, null, null, null);
        }
        
        public static Filler createTargetArgument(DocumentRelation documentRelation) {
            return new Filler(null, null, checkNotNull(documentRelation), null, null, null, null);
        }
        
        public static Filler createTargetArgument(DocumentRelation documentRelation, DocumentRelationArgument documentRelationArgument) {
            return new Filler(null, null, checkNotNull(documentRelation), checkNotNull(documentRelationArgument), null, null, null);
        }
        
        public static Filler createValidTemporalSpanArgument(TemporalSpan temporalSpan){
        	return new Filler(null, null, null, null, checkNotNull(temporalSpan), null, null);
        }
        
        public static Filler createTargetArgument(DocumentEvent documentEvent) {
            return new Filler(null, null, null, null, null, documentEvent, null);
        }
        
        public static Filler createTargetArgument(DocumentEvent documentEvent, DocumentEventArgument documentEventArgument) {
            return new Filler(null, null, null, null, null, checkNotNull(documentEvent), checkNotNull(documentEventArgument));
        }
        
        /**
         * @return this Filler as an Item.
         */
        public Optional<Item> asItem() {
            if (asEntity().isPresent()) {
                return Optional.<Item>of(asEntity().get());
            } else if (asTemporalSpan().isPresent()) {
                return Optional.<Item>of(asTemporalSpan().get());
            } else if (asDocumentEventArgument().isPresent()) {
                return Optional.<Item>of(asDocumentEventArgument().get());
            } else if (asDocumentEvent().isPresent()) {
                return Optional.<Item>of(asDocumentEvent().get());
            } else if (asDocumentRelationArgument().isPresent()) {
                return Optional.<Item>of(asDocumentRelationArgument().get());
            } else if (asDocumentRelation().isPresent()) {
                return Optional.<Item>of(asDocumentRelation().get());
            } else if (asStrength().isPresent()) {
                return Optional.<Item>of(asStrength().get());
            } else {
                return Optional.absent();
            }
        }
    }    
}

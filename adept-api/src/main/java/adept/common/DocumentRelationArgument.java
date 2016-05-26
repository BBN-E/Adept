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

package adept.common;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;

public class DocumentRelationArgument extends HltContent implements HasScoredUnaryAttributes {
    private final IType role;
    private final DocumentRelationArgument.Filler filler;
    private final ImmutableMap<IType, Float> attributes;
    private ImmutableSet<RelationMention.Filler> provenances;
    private final float confidence;
    
    private DocumentRelationArgument(IType role, DocumentRelationArgument.Filler filler,
    		ImmutableSet<RelationMention.Filler> provenances, ImmutableMap<IType, Float> attributes,float confidence) {
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
    public DocumentRelationArgument.Filler getFiller() {
        return filler;
    }

    /**
     * @return May be empty. May not contain {@code null}. Provenances may
     * have different relation types than this argument.
     */
    public ImmutableSet<RelationMention.Filler> getProvenances() {
        return provenances;
    }
    
    /**
     * Creates a builder to create a relation argument of the specified type.
     * @param role May not be {@code null}.
     * @param entity May not be {@code null}.
     * @param confidence
     */
    public static Builder builder(IType role, Filler entity, float confidence) {
        return new Builder(role, entity, confidence);
    }
    
    public static final class Builder {
        private final IType role;
        private final Filler filler;        
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private ImmutableSet.Builder<RelationMention.Filler> provenances = ImmutableSet.builder();
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
            for (final IType arg : attributes.keySet()) {
            	checkArgument(arg!=null);
                checkArgument(attributes.get(arg)!=null);
            }  
            this.attributes.putAll(attributes);
            return this;
        }

        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;
        }

        public Builder addProvenances(Iterable<RelationMention.Filler> provenances) {
        	checkArgument(provenances!=null);
            for (final RelationMention.Filler arg : provenances) {
            	checkArgument(arg!=null);
            }          	
        	this.provenances.addAll(provenances);
            return this;        }

        public Builder addProvenance(RelationMention.Filler provenance) {
        	checkArgument(provenance!=null);
        	this.provenances.add(provenance);
            return this;        
        }

        public DocumentRelationArgument build() {
            return new DocumentRelationArgument(role, filler,
            		provenances.build(), attributes.build(), confidence);
        }
    }

    /**
     * A filler for a role in a document-level relation.  Such role fillers may be of a limited
     * number of different types, currently {@link Entity}, {@link NumericValue}, and
     * {@link TemporalValue}. This class represents that uncertainty. The available
     * types may expand in the future.  Please see {@link DocumentRelation} for a full
     * description of available relation classes.
     *
     * This class is locally immutable.
     */
    public static final class Filler {
    	private final Optional<Entity> entity;
    	private final Optional<TemporalValue> temporalValue;
        private final Optional<NumericValue> numericValue;
        private final Optional<GenericThing> genericThing;
    	
        private Filler(Entity entity, TemporalValue temporalValue, NumericValue numericValue, GenericThing genericThing) {
            this.entity = Optional.fromNullable(entity);
            this.temporalValue = Optional.fromNullable(temporalValue);
            this.numericValue = Optional.fromNullable(numericValue);
            this.genericThing = Optional.fromNullable(genericThing);
        }

        public Optional<Entity> asEntity() {
            return entity;
        }

        public Optional<TemporalValue> asTemporalValue() {
        	return temporalValue;
        }
        
        public Optional<NumericValue> asNumericValue() {
        	return numericValue;
        }
        
        public Optional<GenericThing> asGenericThing() {
        	return genericThing;
        }

        /**
         * @param e May not be null.
         */
        public static Filler fromEntity(Entity e) {
        	checkArgument(e!=null);
            return new Filler(e,null,null,null);
        }

        /**
         * @param temporalValue May not be null.
         */
        public static Filler fromTemporalValue(TemporalValue temporalValue) {
        	checkArgument(temporalValue!=null);
            return new Filler(null,temporalValue,null,null);
        }
        
        /**
         * @param numericValue May not be null.
         */
        public static Filler fromNumericValue(NumericValue numericValue) {
            checkArgument(numericValue!=null);
            return new Filler(null, null, numericValue, null);
        }
        
        /**
         * @param genericThing May not be null.
         */
        public static Filler fromGenericThing(GenericThing genericThing) {
            checkArgument(genericThing!=null);
            return new Filler(null, null, null, genericThing);
        }
        
        /**
         * @return
         */
        public Optional<Item> asItem() {
            if (asEntity().isPresent()) {
                return Optional.<Item>of(asEntity().get());
            } else if (asNumericValue().isPresent()) {
                return Optional.<Item>of(asNumericValue().get());
            } else if (asTemporalValue().isPresent() && asTemporalValue().get() instanceof Item) {
                return Optional.<Item>of((Item) asTemporalValue().get());
            } else if (asGenericThing().isPresent()) {
                return Optional.<Item>of((Item) asGenericThing().get());
            } else {
                return Optional.absent();
            }
        }
    }
}
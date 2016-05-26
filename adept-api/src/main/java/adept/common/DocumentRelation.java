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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;


/**
 * <p>Represents a relation at the level of participating document-level entities,
 * rather than strings of text. The arguments of this class must necessarily be references
 * to document-level entities or temporal values, i.e. instances of class 
 * {@link adept.common.Entity} or {@link adept.common.TemporalValue}. In future releases,
 * arguments will also be allowed to reference a numerical value.</p>
 * 
 * <p>Objects of this class are associated with provenances 
 * that are instances of {@link adept.common.RelationMention}. </p>
 * 
 * <p>This is a new class introduced in the API release version 2.0, and is the
 * primary class to be used in inserting relations into the Adept KB.</p>
 */

public final class DocumentRelation extends HltContent implements HasScoredUnaryAttributes {
    private final IType relationType;
    private final ImmutableSet<RelationMention> provenances;
    private final ImmutableSet<DocumentRelationArgument> arguments;
    private final ImmutableMap<IType, Float> attributes;
    private final float confidence;
    
    private DocumentRelation(IType relationType, ImmutableSet<RelationMention> provenances, ImmutableSet<DocumentRelationArgument> arguments, 
    		ImmutableMap<IType, Float> attributes, float confidence) {
        this.relationType = checkNotNull(relationType);
        
        this.provenances = ImmutableSet.copyOf(provenances);
        for (final RelationMention arg : provenances) {
            checkArgument(relationType.equals(arg.getRelationType()),
                    "A document relation's provenance's relation type must match its own, "
                    +"but got argument of type %s for relation of type %s",
                    arg.getRelationType(), relationType);
        }        

        this.arguments = ImmutableSet.copyOf(arguments);

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.confidence = confidence;
    }

    /**
     * @return May not be null.
     */
    public IType getRelationType() {
        return relationType;
    }

    public float getConfidence() {
        return confidence;
    }

    @Override
    public ImmutableMap<IType, Float> getScoredUnaryAttributes() {
        return attributes;
    }

    /**
     * @return May be empty, no element may be {@code null}. All arguments
     * will have the same relation type as this.
     */
    public ImmutableSet<DocumentRelationArgument> getArguments() {
        return arguments;
    }
    
    /**
     * 
     */
    public DocumentRelationArgument getArgumentById(long id) {
    	for(DocumentRelationArgument argument : getArguments())
    	{
    		if(argument.getFiller().asEntity().isPresent())
    		{
    			if(argument.getFiller().asEntity().get().getEntityId() == id)
    			{
    				return argument;
    			}
    		}
    		//TODO: Add support for argument being 
    		// date mentions.
    	}
    	return null;
    }

    /**
     * @return May be empty, may not contain {@code null}, but may differ
     * in relation type from this.
     */
    public ImmutableSet<RelationMention> getProvenances() {
        return provenances;
    }

    /**
     * @param relationType May not be {@code null}.
     * @return
     */
    public static Builder builder(IType relationType) {
        return new Builder(relationType);
    }

    public static final class Builder {
        private final IType relationType;
        private ImmutableSet.Builder<RelationMention> provenances = ImmutableSet.builder();
        private ImmutableSet.Builder<DocumentRelationArgument> arguments = ImmutableSet.builder();
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private float confidence = 1.0f;
    	
        private Builder(IType relationType) {
            this.relationType = checkNotNull(relationType);
        }

        /**
         * Sets the confidence of the relation being built. If not called, the confidence will be
         * 1.
         */
        public Builder setConfidence(float confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * @param arguments May not contain {@code null}, but may be empty. All must have the
         *                  same relation type as this, or an {@link java.lang.IllegalArgumentException}
         *                  will be thrown.
         * @return
         */
        public Builder addArguments(Iterable<DocumentRelationArgument> arguments) {
        	checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * @param argument May not be {@code null}. Must have the same relation type as this or
         *                 an {@link java.lang.IllegalArgumentException} will be thrown.
         * @return
         */
        public Builder addArgument(DocumentRelationArgument argument) {
        	checkArgument(argument!=null);
        	this.arguments.add(argument);
            return this;
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in relation type from this.
         * @return
         */
        public Builder addProvenances(Iterable<RelationMention> provenances) {
        	checkArgument(provenances!=null);
        	this.provenances.addAll(provenances);
            return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in relation type from this.
         * @return
         */
        public Builder addProvenance(RelationMention provenance) {
        	checkArgument(provenances!=null);
            this.provenances.add(provenance);
            return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in relation type from this.
         * @return
         */
        public Builder addProvenanceFromArgumentTuple(ArgumentTuple provenance) {
        	Optional<RelationMention> relationMention = RelationMention.fromArgumentTuple(provenance);
        	if(relationMention.isPresent())
             this.addProvenance(relationMention.get());
        	return this;
        }

        /**
         * @param attributes May be empty but may not contain {@code null}.
         */
        public Builder setAttributes(Map<? extends IType, Float> attributes) {
        	checkArgument(attributes!=null);
            for (final IType arg : attributes.keySet()) {
            	checkArgument(arg!=null);
                checkArgument(attributes.get(arg)!=null);
            }  
            this.attributes.putAll(attributes);
            return this;
        }

        /**
         * @param attribute may not be {@code null}.
         */
        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;
        }

        public DocumentRelation build() {
            return new DocumentRelation(relationType, provenances.build(), arguments.build(),
                    attributes.build(), confidence);
        }
    }
        }

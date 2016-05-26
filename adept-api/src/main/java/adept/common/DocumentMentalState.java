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

// TODO: add description

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableSet;

/**
 * Internal abstract class representing the commonality between
 * DocumentSentiment and DocumentBelief.
 *  
 */
public abstract class DocumentMentalState<T extends MentalStateMention> extends HltContent { 
    private final ImmutableSet<T> provenances;
    private final ImmutableSet<DocumentMentalStateArgument> arguments;
    private final float confidence;    
    private final MentalStateType type;
    
    protected DocumentMentalState(ImmutableSet<T> provenances, ImmutableSet<DocumentMentalStateArgument> arguments, 
            float confidence, MentalStateType type) {
        this.provenances = ImmutableSet.copyOf(provenances);
        this.arguments = ImmutableSet.copyOf(arguments);
        this.type = type;

        // no null check because it's optional
        this.confidence = confidence;
    }
    
    public ImmutableSet<T> getProvenances() {
        return provenances;
    }
    
    public float getConfidence() {
        return confidence;
    }
        
    /**
     * @return May be empty, no element may be {@code null}. All arguments
     * will have the same relation type as this.
     */
    public ImmutableSet<DocumentMentalStateArgument> getArguments() {
        return arguments;
    }
    
    public static abstract class Builder<T extends MentalStateMention> {
        protected ImmutableSet.Builder<T> provenances = ImmutableSet.builder();
        protected ImmutableSet.Builder<DocumentMentalStateArgument> arguments = ImmutableSet.builder();
        protected float confidence = 1.0f;
    	
        protected Builder() { }        

        /**
         * Sets the confidence of the sentiment being built. If not called, the confidence will be
         * 1.
         */
        public Builder<T> setConfidence(float confidence) {
            this.confidence = confidence;
            return this;
        }
        
        /**
         * @param arguments May not contain {@code null}, but may be empty. All must have the
         *                  same sentiment type as this, or an {@link java.lang.IllegalArgumentException}
         *                  will be thrown.
         * @return
         */
        public Builder<T> addArguments(Iterable<DocumentMentalStateArgument> arguments) {
        	checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * @param argument May not be {@code null}. Must have the same sentiment type as this or
         *                 an {@link java.lang.IllegalArgumentException} will be thrown.
         * @return
         */
        public Builder<T> addArgument(DocumentMentalStateArgument argument) {
        	checkArgument(argument!=null);
        	this.arguments.add(argument);
            return this;
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in sentiment type from this.
         * @return
         */
        public Builder<T> addProvenances(Iterable<T> provenances) {
        	checkArgument(provenances!=null);
        	this.provenances.addAll(provenances);
            return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in sentiment type from this.
         * @return
         */
        public Builder<T> addProvenance(T provenance) {
            checkArgument(provenances!=null);
            this.provenances.add(provenance);
            return this;
        }

        public abstract DocumentMentalState<T> build();
    }    
}

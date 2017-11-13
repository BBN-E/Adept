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

// TODO: add description

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;

/**
 * Internal abstract class representing the commonality between
 * DocumentSentiment and DocumentBelief.
 *  
 */
public abstract class DocumentMentalState<T extends MentalStateMention> extends HltContent implements Serializable { 
	private static final long serialVersionUID = -3837738192120721955L;
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
    
    public static abstract class Builder<BuilderType extends Builder<BuilderType, MentionType>, MentionType extends MentalStateMention> {
        protected ImmutableSet.Builder<MentionType> provenances = ImmutableSet.builder();
        protected ImmutableSet.Builder<DocumentMentalStateArgument> arguments = ImmutableSet.builder();
        protected float confidence = 1.0f;
    	
        protected Builder() { }        

        /**
         * Sets the confidence of the sentiment being built. If not called, the confidence will be
         * 1.
         * 
         * @param confidence the confidence level of this relationship
         * 
         * @return this BuilderType instance. Useful for chaining
         */
        public BuilderType setConfidence(float confidence) {
            this.confidence = confidence;
            return me();
        }
        
        /**
         * @param arguments May not contain {@code null}, but may be empty. All must have the
         *                  same sentiment type as this, or an {@link java.lang.IllegalArgumentException}
         *                  will be thrown.
         * @return this BuilderType instance. Useful for chaining
         */
        public BuilderType addArguments(Iterable<DocumentMentalStateArgument> arguments) {
        	checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return me();
        }

        /**
         * @param argument May not be {@code null}. Must have the same sentiment type as this or
         *                 an {@link java.lang.IllegalArgumentException} will be thrown.
         * @return this BuilderType instance. Useful for chaining
         */
        public BuilderType addArgument(DocumentMentalStateArgument argument) {
        	checkArgument(argument!=null);
        	this.arguments.add(argument);
            return me();
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in sentiment type from this.
         * @return this BuilderType instance. Useful for chaining
         */
        public BuilderType addProvenances(Iterable<MentionType> provenances) {
        	checkArgument(provenances!=null);
        	this.provenances.addAll(provenances);
            return me();
        }

        /**
         * @param provenance may not be {@code null}. May differ in sentiment type from this.
         * @return this BuilderType instance. Useful for chaining
         */
        public BuilderType addProvenance(MentionType provenance) {
            checkArgument(provenances!=null);
            this.provenances.add(provenance);
            return me();
        }

        public abstract DocumentMentalState<MentionType> build();
        
        protected abstract BuilderType me();
    }    
}
/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;

/**
 * Represents a set of event occurrence.  This can be used, for example, to represent
 * event coreference which doesn't bother about arguments.  THe types of the contained
 * events are allowed to be heterogeneous.
 *
 *  <p>For high-level documentation of all event classes, see {@link adept.common.DocumentEvent}.
 <p>This class is locally immutable.</p>

 */
public final class EventTextSet extends HltContent implements HasAttributes {
    private final ImmutableSet<EventText> provenances;
    private final ImmutableMap<IType, Float> attributes;
    private final Float score;
    
    private EventTextSet(ImmutableSet<EventText> provenances, ImmutableMap<IType, Float> attributes, Float score) {
        this.provenances = ImmutableSet.copyOf(provenances);

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.score = score;      	
    }

    /**
     * @return Will not be empty or contain {@code null}, but may be of
     * heterogeneous event types.
     */
    public ImmutableSet<EventText> getProvenances() {
    	return provenances;
    }

    @Override
    public ImmutableMap<IType, Float> getAttributes() {
        return attributes;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    /**
     * @param provenances May not be empty or contain {@code null}. If any provenance's
     *    {@link EventText#getEventType()} does not
     * @return
     */
    public static Builder builder(Iterable<EventText> provenances) {
    	checkArgument(provenances!=null);
        for (final EventText arg : provenances) {
        	checkArgument(arg!=null);
        	// types may vary
        }      	
        return new Builder(provenances);
    }

    public static final class Builder {
        private ImmutableSet.Builder<EventText> provenances = ImmutableSet.builder();
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();    	
        private Float score = null;
        
        private Builder(Iterable<EventText> provenances) {
        	// checked in caller
            this.provenances.addAll(provenances);        
        }

        /**	
         * Sets the score of the {@code EventProvenanceSet} being built. If not called, the score
         * will be {@link com.google.common.base.Optional#absent()}.
         */
        public Builder setScore(float score) {
            this.score = score;
            return this;
        }

        /**
         * @param attribute may be empty but may not contain {@code null}.
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
         * @param attribute May not be {@code null}.
         * @return
         */
        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;        
        }

        public EventTextSet build() {
            return new EventTextSet( provenances.build(), attributes.build(), score);
        }
    }
}
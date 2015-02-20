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

import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents an event where the arguments are particular
 * chunks of text which may or may not be further resolved.
 * Typically this will also be associated with text supporting
 * the event, but this is not required.
 *
 *  <p>For high-level documentation of all event classes, see {@link adept.common.DocumentEvent}.
 <p>This class is locally immutable.</p>

 */
public final class EventMention extends HltContent implements HasAttributes {
    private final IType eventType;
    private final EventText provenance;
    private final ImmutableSet<EventMentionArgument> arguments;
    private final ImmutableMap<IType, Float> attributes;
    private final Float score;

    private EventMention(IType eventType, EventText provenance,
                         Iterable<EventMentionArgument> arguments,
                         Map<IType, Float> attributes, Float score)
    {
        this.eventType = checkNotNull(eventType);

        // no null check because it's optional
        this.provenance = provenance;
        if (provenance != null) {
            checkArgument(eventType.equals(provenance.getEventType()),
                    "An event mention's provenance's event type must match its own, "
                            + "but got provenance of type %s for event of type %s",
                    provenance.getEventType(), eventType);
        }

        this.arguments = ImmutableSet.copyOf(arguments);
        for (final EventMentionArgument arg : arguments) {
            checkArgument(eventType.equals(arg.getEventType()),
                    "An event mention's arguments' event types must match its own, "
                    +"but got argument of type %s for event of type %s",
                    arg.getEventType(), eventType);
        }

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.score = score;
    }

    /**
     * @return Will never be {@code null}/
     */
    public IType getEventType() {
       return eventType;
    }

    /**
     * Evidence that the event occurred, is available. If present, will always
     * have the same event type as this.
     */
    public Optional<EventText> getProvenance() {
       return Optional.fromNullable(provenance);
    }

    /**
     * The arguments of the event.
     * @return May be empty, but will not be or contain {@code null}. All must have the same
     * event type as this.
     */
    public ImmutableSet<EventMentionArgument> getArguments() {
        return arguments;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    @Override
    public ImmutableMap<IType, Float> getAttributes() {
        return attributes;
    }

    /**
     * Returns an object which can be used to create an {@code EventMention}.
     * @param eventType May not be {@code null}.
     */
    public static Builder builder(IType eventType) {
    	checkArgument(eventType!=null);
        return new Builder(eventType);
    }

    public static final class Builder {
        private final IType eventType;
        private EventText provenance = null;
        private ImmutableSet.Builder<EventMentionArgument> arguments = ImmutableSet.builder();
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private Float score = null;
        
        private Builder(IType eventType) {
            this.eventType = eventType;
        }

        /**
         * Specified the score of the {@code EventMention} to be built. If not specified,
         * will be {@link com.google.common.base.Optional#absent()}.
         */
        public Builder setScore(float score) {
            this.score = score;
            return this;
        }

        /**
         * @param arguments May not be or contain {@code null}. May be empty. All
         *                  must have the same event type as this.
         * @return
         */
        public Builder addArguments(Iterable<EventMentionArgument> arguments) {
        	checkArgument(arguments!=null);
            for (final EventMentionArgument arg : arguments) {
            	checkArgument(arg!=null);
            	// More informative check above
            	//checkArgument(arg.getEventType()==eventType);
            }
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * @param argument May not be null. Must have the same event type as
         *                 this.
         */
        public Builder addArgument(EventMentionArgument argument) {
        	checkArgument(argument!=null);
        	// More informative check above
        	//checkArgument(argument.getEventType()==eventType);
            this.arguments.add(argument);
            return this;
        }

        /**
         * Specified the provenance of the {@code EventMention} to be built. If not specified,
         * will be {@link com.google.common.base.Optional#absent()}. If the event type of
         * {@code provenance} does not match the event type of the builder, an
         * {@link java.lang.IllegalArgumentException} will be thrown.
         * @param provenance May not be null.
         */
        public Builder setProvenance(EventText provenance) {
        	checkArgument(provenance!=null);
        	// More informative check above
        	//checkArgument(provenance.getEventType()==eventType);
        	this.provenance = provenance;
            return this;
        }

        /**
         * Adds the specified scored attributes.  Beware that attempting to set
         * the same attribute twice, even to the same value, will raise an
         * {@link java.lang.IllegalArgumentException} when {@link #build()}
         * is called.
         * @param attributes
         * @return
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
         * Adds the specified scored attribute.  Beware that attempting to set
         * the same attribute twice, even to the same value, will raise an
         * {@link java.lang.IllegalArgumentException} when {@link #build()}
         * is called.
         */
        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;
        }

        public EventMention build() {
            return new EventMention(eventType, provenance, arguments.build(),
                    attributes.build(), score);
        }
    }
}
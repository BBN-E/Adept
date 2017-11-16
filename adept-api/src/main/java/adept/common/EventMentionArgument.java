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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents that a particular span of text plays a role in an event.
 *
 *  <p>For high-level documentation of all event classes, see {@link adept.common.DocumentEvent}.
 <p>This class is locally immutable.</p>

 */
public final class EventMentionArgument extends HltContent implements HasScoredUnaryAttributes, Serializable {
	private static final long serialVersionUID = -4250809784071643735L;
	private final IType eventType;
    private final IType role;
    private final Chunk filler;
    private final ImmutableMap<IType, Float> attributes;
    private Float score;

    private EventMentionArgument(IType eventType, IType role, Chunk filler, ImmutableMap<IType, Float> attributes, Float score) {
        this.eventType = checkNotNull(eventType);

        this.role = checkNotNull(role);

        this.filler = checkNotNull(filler);

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.score = score;
    }

    public IType getEventType() {
        return eventType;
    }

    public IType getRole() {
        return role;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    public void updateScore(Float newScore){
	this.score = newScore;
    }

    public Chunk getFiller() {
        return filler;
    }

    @Override
    public ImmutableMap<IType, Float> getScoredUnaryAttributes() {
        return attributes;
    }

    /**
     * Obtains a builder which can build an {@code EventMentionArgument}
     * of the specified type with the specified filler.
     * @param eventType may not be {@code null}
     * @param role may not be {@code null}.
     * @param filler may not be {@code null}.
     */
    public static Builder builder(IType eventType, IType role, Chunk filler) {
        return new Builder(eventType, role, filler);
    }

    public static final class Builder {
        private final IType eventType;
        private final IType role;
        private final Chunk filler;
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private Float score = null;

        private Builder(IType eventType, IType role, Chunk filler) {
            this.eventType = checkNotNull(eventType);
			this.role = checkNotNull(role);
			this.filler = checkNotNull(filler);
        }

        /**
         * Sets the score of the argument. If not set, defaults to {@link com.google.common.base.Optional#absent()}.
         * @param score
         * @return
         */
        public Builder setScore(float score) {
            this.score = score;
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

        public EventMentionArgument build() {
            return new EventMentionArgument(eventType, role, filler,
                    attributes.build(), score);
        }

    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final EventMentionArgument that = (EventMentionArgument) o;

        if (!eventType.equals(that.eventType)) {
            return false;
        }
        if (!role.equals(that.role)) {
            return false;
        }
        if (!filler.equals(that.filler)) {
            return false;
        }
        if (!attributes.equals(that.attributes)) {
            return false;
        }
        return !(score != null ? !score.equals(that.score) : that.score != null);

    }

    @Override
    public int hashCode() {
        int result = eventType.hashCode();
        result = 31 * result + role.hashCode();
        result = 31 * result + filler.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}

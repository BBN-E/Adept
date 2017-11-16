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
import adept.common.EventMentionArgument.Builder;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.io.Serializable;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents the argument of an event.  Unlike {@link adept.common.EventMentionArgument},
 this represents the participation of an <b>entity</b> in an event rather than an extent of
 text, though supporting provenances may be made optionally available.

 <p>For high-level documentation of all event classes, see {@link adept.common.DocumentEvent}.
 <p>This class is locally immutable.</p>

 */
public class DocumentEventArgument extends HltContent implements HasScoredUnaryAttributes, Serializable {
	private static final long serialVersionUID = -456665354017823245L;
	private final IType eventType;
    private final IType role;
    private final Filler filler;
    private final ImmutableMap<IType, Float> attributes;
    private ImmutableSet<Provenance> provenances;
    private final Float score;

    private DocumentEventArgument(IType eventType, IType role, Filler filler,
    		ImmutableSet<Provenance> provenances, ImmutableMap<IType, Float> attributes,Float score) {
        this.eventType = checkNotNull(eventType);

        this.role = checkNotNull(role);

        this.filler = checkNotNull(filler);

        // provenances may vary in event type
        this.provenances = ImmutableSet.copyOf(provenances);

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.score = score;
    }

    /**
     * @return Will never be {@code null}.
     */
    public IType getEventType() {
        return eventType;
    }

    /**
     *
     * @return Will never be {@code null}.
     */
    public IType getRole() {
        return role;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    @Override
    public ImmutableMap<IType, Float> getScoredUnaryAttributes() {
        return attributes;
    }

    /**
     * The entity or value filling this argument.
     * @return Will never be {@code null}.
     */
    public Filler getFiller() {
        return filler;
    }

    /**
     * @return May be empty. May not contain {@code null}. Provenances may
     * have different event types than this argument.
     */
    public ImmutableSet<Provenance> getProvenances() {
        return provenances;
    }

    /**
     * Creates a builder to create an event argument of the specified type.
     * @param eventType May not be {@code null}.
     * @param role May not be {@code null}.
     * @param entity May not be {@code null}.
     * @return the new Builder instance. Can be chained.
     */
    public static Builder builder(IType eventType, IType role, Filler entity) {
        return new Builder(eventType, role, entity);
    }

    public static final class Builder {
        private final IType eventType;
        private final IType role;
        private final Filler filler;
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private ImmutableSet.Builder<Provenance> provenances = ImmutableSet.builder();
        private Float score = null;

        private Builder(IType eventType, IType role, Filler entity) {
            this.eventType = checkNotNull(eventType);
			this.role = checkNotNull(role);
			this.filler = checkNotNull(entity);
        }

        /**
         * Sets the score of the event argument being built. If not called,
         * score will be {@link com.google.common.base.Optional#absent()}
         * 
         * @param score the value to set
         * @return this Builder. Useful for chaining.
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

        public Builder addProvenances(Iterable<? extends Provenance> provenances) {
        	checkArgument(provenances!=null);
            for (final Provenance arg : provenances) {
            	checkArgument(arg!=null);
            }
        	this.provenances.addAll(provenances);
            return this;        }

        public Builder addProvenance(Provenance provenance) {
        	checkArgument(provenance!=null);
        	this.provenances.add(provenance);
            return this;
        }

        public DocumentEventArgument build() {
            return new DocumentEventArgument(eventType, role, filler,
            		provenances.build(), attributes.build(), score);
        }
    }

    /**
     * Represents the source of an event argument. It could come from an {@link EventMention},
     * or it could have been predicted in isolation. This class reflects that uncertainty.
     *
     * This class is locally immutable.
     */
    public static final class Provenance implements Serializable {
		private static final long serialVersionUID = 3321871896100169601L;
		private final Optional<EventMention>  eventMention;
    	private final EventMentionArgument  eventMentionArgument;

        private Provenance(EventMention eventMention, EventMentionArgument arg) {
        	checkArgument(arg!=null);
        	this.eventMention = Optional.fromNullable(eventMention);
        	this.eventMentionArgument = arg;
        }

        public static Provenance fromStandaloneEventMentionArgument(EventMentionArgument arg) {
        	checkArgument(arg!=null);
        	EventMention eventMention = null;
        	return new Provenance( eventMention, arg);
        }

        /**
         * Creates a {@code Provenance} from an argument attached to an event mention. If {@code arg}
         * is not an argument of {@code em}, an {@link java.lang.IllegalArgumentException} will be thrown.
         * @param em May not be null.
         * @param arg Must be an argument of {@code em} (and therefore cannot be {@code null}).
         * @return the new Provenance
         */
        public static Provenance fromArgumentOfEventMention(EventMention em, EventMentionArgument arg) {
        	checkArgument(em!=null);
        	checkArgument(arg!=null);
        	checkArgument( em.getArguments().contains(arg));
        	return new Provenance( em, arg);
        }

        /**
         * Gets the event mention this argument is connected to, if available.  If present, will contain
         * {@link #getEventMentionArgument()}.
         * @return if present, the event mention.
         */
        public Optional<EventMention> getEventMention() {
            return eventMention;
        }

        /**
         * @return Will never be {@code null}.
         */
        public EventMentionArgument getEventMentionArgument() {
            return eventMentionArgument;
        }
    }

    /**
     * A filler for a role in a document-level event.  Such role fillers may be of a limited
     * number of different types, currently {@link Entity} and
     * {@link TemporalValue}. This class represents that uncertainty. The available
     * types may expand in the future.  Please see {@link DocumentEvent} for a full
     * description of available event classes.
     *
     * This class is locally immutable.
     */
    public static final class Filler implements Serializable {
		private static final long serialVersionUID = -2180016602285535326L;
		private final Optional<Entity> entity;
    	private final Optional<TemporalValue> temporalValue;
    	private final Optional<GenericThing> genericThing;

        private Filler(Entity entity, TemporalValue temporalValue, GenericThing genericThing) {
            this.entity = Optional.fromNullable(entity);
            this.temporalValue = Optional.fromNullable(temporalValue);
            this.genericThing = Optional.fromNullable(genericThing);
        }

        public Optional<Entity> asEntity() {
            return entity;
        }

        public Optional<TemporalValue> asTemporalValue() {
        	return temporalValue;
        }

        public Optional<GenericThing> asGenericThing(){
        	return genericThing;
        }

        /**
         * @param e May not be null.
         * @return this Filler. Useful for chaining.
         */
        public static Filler fromEntity(Entity e) {
        	checkArgument(e!=null);
            return new Filler(e,null, null);
        }

        /**
         * @param temporalValue May not be null.
         * @return this Filler. Useful for chaining.
         */
        public static Filler fromTemporalValue(TemporalValue temporalValue) {
        	checkArgument(temporalValue!=null);
            return new Filler(null,temporalValue, null);
        }

        public static Filler fromGenericThing(GenericThing genericThing){
        	checkArgument(genericThing != null);
        	return new Filler(null, null, genericThing);
        }

        /**
         * @return the filler as an {@code Optional<Item>}
         */
        public Optional<Item> asItem() {
            if (asEntity().isPresent()) {
                return Optional.<Item>of(asEntity().get());
            } else if (asTemporalValue().isPresent() && asTemporalValue().get() instanceof Item) {
                return Optional.<Item>of((Item) asTemporalValue().get());
            } else if (asGenericThing().isPresent()){
            	return Optional.<Item>of(asGenericThing().get());
            } else {
                return Optional.absent();
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

            final Filler filler = (Filler) o;

            if (!entity.equals(filler.entity)) {
                return false;
            }
            if (!temporalValue.equals(filler.temporalValue)) {
                return false;
            }
            return genericThing.equals(filler.genericThing);

        }

        @Override
        public int hashCode() {
            int result = entity.hashCode();
            result = 31 * result + temporalValue.hashCode();
            result = 31 * result + genericThing.hashCode();
            return result;
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

        final DocumentEventArgument that = (DocumentEventArgument) o;

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
        if (!provenances.equals(that.provenances)) {
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
        result = 31 * result + provenances.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}

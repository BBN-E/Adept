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
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;

import java.util.Map;

/**
 * Represents an event at the level of participating document-level entities,
 * rather than strings of text.
 * <p>
 * What follows is a description of the structure of all event-related classes in the ADEPT API
 * from two perspective: first, in terms of the API's internal logic and the distinctions made in
 * its data structures, and, second, in terms of how DEFT event algorithm output ought to be
 * mapped to these data structures.
 * <p>
 * <strong>API Data Structure Rationale</strong>
 * <p>
 * There are two main axes of
 * variation central to the API.  The first is the representation of event participants, whether
 * as spans of text (<b>textual events</b>) or as entities/resolved values (<b>document-level events</b>).
 * The second is that we may wish to express the mere occurrence of some event, the arguments
 * involved in an event, or both together.
 * <p>
 * <strong>Representing Textual Events</strong>
 * <ul>
 * <li>
 * Representing the occurrence of <b>textual events</b>, (e.g. a particular span
 * of text in sentence three indicates that a {@code Life.Marry} event occurred) is handled by
 * {@link EventText}.  The API is agnostic as to the number and sizes of the spans of
 * text justifying the event's occurrence. It could range from single trigger words
 * to multiple non-contiguous sentences.
 * <li>
 * Representing the arguments of <b>textual events</b> (e.g. a particular span
 * of text is the {@code Victim} of a {@code Conflict.Attack} event) is handled by
 * {@link adept.common.EventMentionArgument}. The text of the argument can be a
 * {@link adept.common.EntityMention}, a {@link adept.common.TimePhrase}, or
 * any other kind of {@link adept.common.Chunk}.
 * <li>
 * Grouping together the arguments of the same textual expression of an event is
 * handled by {@link adept.common.EventMention}.  This may <b>optionally</b> also link
 * the arguments to an {@link EventText}.
 * </ul>
 * <p>
 * <strong>Intermediate levels</strong>
 * <p>
 * If you wish to represent a grouping of textual events without regard for their arguments,
 * use an {@link EventTextSet}.  These may optionally be converted to
 * {@link adept.common.DocumentEvent}s (see below) without arguments, but beware that by doing so
 * you are abusing the semantics of the classes somewhat (since you do not wish to assert
 * that the {@code DocumentEvent}s are in fact argumentless) and may get invalid results
 * from some downstream algorithms.
 * <p>
 * <strong>Representing Entity-level Events</strong>
 * <ul>
 * <li>
 * {@link adept.common.DocumentEventArgument.Filler} represents something which fills an
 * argument role of a document-level event.  Currently this is either an {@link adept.common.Entity}
 * or a {@link adept.common.TemporalValue}.
 * <li>
 * To represent the participation of an {@link adept.common.DocumentEventArgument.Filler} in an event, use
 * {@link DocumentEventArgument}.  An {@code DocumentEventArgument} may have optionally have
 * {@link DocumentEventArgument.Provenance}s linking it to supporting
 * {@link adept.common.EventMentionArgument}(s).
 * <li>
 * To represent an event at the document-level, independent of any single
 * textual realization, use {@link DocumentEvent}, which gathers together {@link DocumentEventArgument}s.
 * An {@code Event} may optionally have {@link DocumentEvent.Provenance}s linking it to
 * supporting {@link adept.common.EventMention}s and {@link EventText}s.   DocumentEvent is an event-level parallel to an
 * entity. Therefore, no {@link adept.common.DocumentEvent}s in the same document should be in
 * an identity-coreference relationship with each other.  Document-level events are appropriate for
 * eventual storage in a knowledge-base.
 * </ul>
 * <p>
 * There is another older event class, {@link adept.common.Event}, which is roughly
 * equivalent to {@link adept.common.EventMention}.  However, use of the newer classes
 * described here is strongly encouraged because they provide several advantages, most
 * importantly the possibility of reification, allowing event coreference and event-event
 * relations to be expressed.  An {@link adept.common.Event} may be converted to an
 * approximating {@link adept.common.EventMention} using the {@link Event#asEventMention()} method.
 * <p>
 * Algorithms differ greatly in exactly what objects they score, so most event classes have an optional
 * score field.  Interpretation of this field is algorithm-specific. Most of the classes also implement
 * {@link adept.common.HasScoredUnaryAttributes}, allowing algorithms to mark e.g. realis at varying levels.
 * Interpretation of attributes is algorithm-specific.
 * <p>
 * All event-related classes are locally immutable, which is to say that once created, you cannot alter
 * their event-specific information. However, the fields associated with their {@link adept.common.HltContent}
 * super-class are mutable, as are some of their contained types such as {@link adept.common.Chunk}.
 * <p>
 * Most of these classes are constructed using the fluent builder pattern. Please see
 * {@code com.bbn.EventTest} for many examples.
 * <p>
 * <strong>Mapping from DEFT Event Algorithms to this API</strong>
 * <p>
 *     <ul>
 *         <li><strong>ACE and ERE event mentions: </strong> The triggers should be
 *         {@link adept.common.EventText}s, the arguments should be {@link adept.common.EventMentionArgument}s,
 *         and the should be joined together in {@link adept.common.EventMention}s. Modality,
 *         genericity, etc. should be represented by scored attributes on {@link adept.common.EventMention}.
 *         <li><strong>ACE and ERE events</strong> should be represented by {@link adept.common.DocumentEvent}s.
 *         <li><strong>NIST's 2014 Event Mention Detection Evaluation event nuggets</strong> should
 *         be {@link adept.common.EventText}s with realis indicated using scored attributes.
 *         <li><b>TAC KBP 2014 Event Arguments</b> should be represented as {@link adept.common.DocumentEventArgument}s
 *         where the entity corresponds to the canonical argument string and justifications and basefillers
 *         are indicated by {@link adept.common.DocumentEventArgument.Provenance}s wrapping
 *         {@link adept.common.EventMentionArgument}s.
 *     </ul>
 * <p>
 * <strong>Concerning Event Types</strong>
 * <p>
 *     The types of events may come from either a closed ontology or an open one (e.g.
 *     predicates drawn from natural language).  Either is acceptable to the API. In the
 *     latter case, you can construct new types using {@link Type#Type(String)}.
 * <p>
 * <strong>Concerning Reification of Arguments a System Cannot Coref</strong>
 * <p>
 *     Many systems may be unable to coreference certain argument types (e.g. crimes).
 *     In such cases, it is recommend that the algorithm create singleton entities for these
 *     when creating {@link adept.common.DocumentEvent}s.
 */
public final class DocumentEvent extends HltContent implements HasScoredUnaryAttributes, Serializable {
	private static final long serialVersionUID = 9124568202462195235L;
	private final IType eventType;
    private final ImmutableSet<Provenance> provenances;
    private final ImmutableSet<DocumentEventArgument> arguments;
    private final ImmutableMap<IType, Float> attributes;
    private final Float score;

    private DocumentEvent(IType eventType, ImmutableSet<Provenance> provenances, ImmutableSet<DocumentEventArgument> arguments,
    		ImmutableMap<IType, Float> attributes, Float score) {
        this.eventType = checkNotNull(eventType);

        // provenances may vary in event type
        this.provenances = ImmutableSet.copyOf(provenances);
//        for (final Provenance arg : provenances) {
//            checkArgument(eventType.equals(arg.getEventText().getEventType()),
//                    "A document event's provenance's event type must match its own, "
//                    +"but got argument of type %s for event of type %s",
//                    arg.getEventText().getEventType(), eventType);
//        }

        this.arguments = ImmutableSet.copyOf(arguments);
        for (final DocumentEventArgument arg : arguments) {
            checkArgument(eventType.equals(arg.getEventType()),
                    "A document event's arguments' event types must match its own, "
                    +"but got argument of type %s [%s] for event of type %s [%s]",
                    arg.getEventType(), arg.getEventType().getType(), eventType, eventType.getType());
        }

        this.attributes = ImmutableMap.copyOf(attributes);

        // no null check because it's optional
        this.score = score;
    }

    /**
     * @return May not be null.
     */
    public IType getEventType() {
        return eventType;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    @Override
    public ImmutableMap<IType, Float> getScoredUnaryAttributes() {
        return attributes;
    }

    /**
     * @return May be empty, no element may be {@code null}. All arguments
     * will have the same event type as this.
     */
    public ImmutableSet<DocumentEventArgument> getArguments() {
        return arguments;
    }

    /**
     * @return May be empty, may not contain {@code null}, but may differ
     * in event type from this.
     */
    public ImmutableSet<Provenance> getProvenances() {
        return provenances;
    }

    /**
     * @param eventType May not be {@code null}.
     * @return this Builder instance. Useful for chaining
     */
    public static Builder builder(IType eventType) {
        return new Builder(eventType);
    }

    public static final class Builder {
        private final IType eventType;
        private ImmutableSet.Builder<Provenance> provenances = ImmutableSet.builder();
        private ImmutableSet.Builder<DocumentEventArgument> arguments = ImmutableSet.builder();
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private Float score = null;

        private Builder(IType eventType) {
            this.eventType = checkNotNull(eventType);
        }

        /**
         * Sets the score of the event being built. If not called, the score will be
         * {@link com.google.common.base.Optional#absent()}.
         * 
         * @param score the score to set
         * @return this Builder instance. Useful for chaining
         */
        public Builder setScore(float score) {
            this.score = score;
            return this;
        }

        /**
         * @param arguments May not contain {@code null}, but may be empty. All must have the
         *                  same event type as this, or an {@link java.lang.IllegalArgumentException}
         *                  will be thrown.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addArguments(Iterable<DocumentEventArgument> arguments) {
        	checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * @param argument May not be {@code null}. Must have the same event type as this or
         *                 an {@link java.lang.IllegalArgumentException} will be thrown.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addArgument(DocumentEventArgument argument) {
        	checkArgument(argument!=null);
        	this.arguments.add(argument);
            return this;
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenances(Iterable<Provenance> provenances) {
        	checkArgument(provenances!=null);
        	this.provenances.addAll(provenances);
            return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenance(Provenance provenance) {
        	checkArgument(provenances!=null);
            this.provenances.add(provenance);
            return this;
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenancesFromEventMentions(Iterable<EventMention> provenances) {
        	checkArgument(provenances!=null);
            for (final EventMention arg : provenances) {
            	checkArgument(arg!=null);
            	this.addProvenance(Provenance.fromEventMention(arg));
            }
        	return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenanceFromEventMention(EventMention provenance) {
            this.addProvenance(Provenance.fromEventMention(provenance));
        	return this;
        }

        /**
         * @param provenances may be empty, but may not contain {@code null}. Provenances may
         *                    differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenancesFromEventOccurences(Iterable<EventText> provenances) {
        	checkArgument(provenances!=null);
            for (final EventText arg : provenances) {
            	checkArgument(arg!=null);
            	this.addProvenance(Provenance.fromEventTextAlone(arg));
            }
        	return this;
        }

        /**
         * @param provenance may not be {@code null}. May differ in event type from this.
         * @return this Builder instance. Useful for chaining
         */
        public Builder addProvenanceFromEventOccurences(EventText provenance) {
            this.addProvenance(Provenance.fromEventTextAlone(provenance));
        	return this;
        }

        /**
         * @param attributes May be empty but may not contain {@code null}.
         * @return this Builder instance. Useful for chaining
         */
        public Builder setAttributes(Map<? extends IType, Float> attributes) {
        	checkArgument(attributes!=null);
        	for (Map.Entry<? extends IType, Float> entry : attributes.entrySet()) {
          	checkArgument(entry.getKey()!=null);
            checkArgument(entry.getValue()!=null);
          }  
          this.attributes.putAll(attributes);
          return this;
        }

        /**
         * @param attribute may not be {@code null}.
         * @param score the score to assign to this attribute
         * @return this Builder instance. Useful for chaining
         */
        public Builder addAttribute(IType attribute, float score) {
        	checkArgument(attribute!=null);
            this.attributes.put(attribute, score);
            return this;
        }

        public DocumentEvent build() {
            return new DocumentEvent(eventType, provenances.build(), arguments.build(),
                    attributes.build(), score);
        }
    }

    /**
     * Represents evidence for the existence of an event. This could be 'standalone' evidence or it could
     * come from an {@link adept.common.EventMention}. This class reflects the uncertainty.
     *
     * This class is locally immutable.
     */
    public static final class Provenance implements Serializable {
		private static final long serialVersionUID = 3671712752097667046L;
		private final EventText eventText;
    	private final Optional<EventMention> eventMention;

        private Provenance(EventText eventText, EventMention eventMention) {
            this.eventText = eventText;
            this.eventMention = Optional.fromNullable(eventMention);
        }

        /**
         * @return Will never be null.
         */
        public EventText getEventText() {
            return eventText;
        }

        /**
         * @return If present, the {@code EventMention} 's
         * own provenance will be {@link #getEventText()}.
         * 
         */
        public Optional<EventMention> getEventMention() {
            return eventMention;
        }

        /**
         * @param eventTextProvenance may not be {@code null}.
         * @return the provenance of the provided event text
         */
        public static Provenance fromEventTextAlone(EventText eventTextProvenance) {
        	checkArgument(eventTextProvenance!=null);
            return new Provenance( eventTextProvenance, null);
        }

        /**
         * @param eventMention May not be {@code null}.
         * @return the provenance of the provided event mention
         */
        public static Provenance fromEventMention(EventMention eventMention) {
        	checkArgument(eventMention!=null);
        	Optional<EventText> eventText = eventMention.getProvenance();
        	checkArgument(eventText.isPresent());
            return new Provenance( eventText.get(),  eventMention);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Provenance that = (Provenance) o;

            if (!eventText.equals(that.eventText)) {
                return false;
            }
            return eventMention.equals(that.eventMention);

        }

        @Override
        public int hashCode() {
            int result = eventText.hashCode();
            result = 31 * result + eventMention.hashCode();
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

        final DocumentEvent that = (DocumentEvent) o;

        if (!eventType.equals(that.eventType)) {
            return false;
        }
        if (!provenances.equals(that.provenances)) {
            return false;
        }
        if (!arguments.equals(that.arguments)) {
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
        result = 31 * result + provenances.hashCode();
        result = 31 * result + arguments.hashCode();
        result = 31 * result + attributes.hashCode();
        result = 31 * result + (score != null ? score.hashCode() : 0);
        return result;
    }
}

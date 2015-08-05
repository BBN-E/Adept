package adept.common;

import com.google.common.collect.ImmutableSet;

/**
 * The Class Event, which represents something
 * that happens or is regarded as happening; 
 * an occurrence, especially one of some importance. In most
 * cases, the new {@link EventMention} class should be used
 * instead of this.  See {@link adept.common.DocumentEvent}'s
 * documentation for a full description of available event
 * classes.
 */
public class Event extends ArgumentTuple {

    /**
     * The event id.
     */
    private final long eventId;

    /**
     * Instantiates a new event.
     *
     * @param eventId   the event id
     * @param eventType the event type
     */
    public Event(long eventId, IType eventType) {
        super(eventType);
        this.eventId = eventId;
    }

    /**
     * Gets the event id.
     *
     * @return the event id
     */
    public long getEventId() {
        return eventId;
    }

    /**
     * Returns an approximation to this {@code Event} as an
     * {@link adept.common.EventMention}.  All attributes will be
     * copied over with score 1.0.  All arguments with non-empty distributions
     * will be copied over to {@link adept.common.EventMentionArgument}s with
     * the chunk and score of the best item in the argument distribution.
     *
     * The resulting {@link adept.common.EventMentionArgument} will have no
     * provenance because none is available.  If ticket BBN-281 is addressed,
     * this method should be modified to use the now-Chunk-based context
     * field to create an {@link adept.common.EventText} to use as provenance.
     *
     * @return
     */
    public EventMention asEventMention() {
        final EventMention.Builder builder = EventMention.builder(getIType());
        // we copy this to an immutable set because the builder
        // does not allow adding duplicate attributes
        for (final IType attribute : ImmutableSet.copyOf(attributes)) {
            builder.addAttribute(attribute, 1.0f);
        }
        for (final Argument arg : getArguments()) {
            if (!arg.getArgumentDistribution().isEmpty()) {
                Chunk bestFiller = null;
                float bestScore = Float.NEGATIVE_INFINITY;

                for (final Pair<Chunk, Float> scoredArg : arg.getArgumentDistribution()) {
                    if (scoredArg.getR() > bestScore) {
                        bestScore = scoredArg.getR();
                        bestFiller = scoredArg.getL();
                    }
                }

                builder.addArgument(
                    EventMentionArgument.builder(getIType(),
                            arg.getArgumentIType(), bestFiller)
                        .setScore(bestScore).build());
            }
        }
        return builder.build();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Event))
            return false;
        Event event = (Event) obj;
        return (event.eventId == this.eventId && event.tupleType.equals(this.tupleType));
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        int id = (int) this.eventId;
        int hash = id * 27;
        return hash;
    }
}

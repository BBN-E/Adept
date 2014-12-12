package adept.common;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;

/**
 * Represents some textual evidence that an event occurred.  Note that this evidence can take many forms:
 * <ul>
 *     <li>A traditional ACE-style trigger can be represented by an {@code EventText} with
 *     with a single element provenance set consisting of only the trigger token.</li>
 *     <li>Event nuggets can be represented by an {@code EventText} with multiple spans
 *     of tokens in the provenance set.</li>
 *     <li>TAC KBP Event Argument-style justifications can be represented by provenance sets with
 *     longer strings of text.</li>
 * </ul>
 *
 *  <p>For high-level documentation of all event classes, see {@link adept.common.DocumentEvent}.
 <p>This class is locally immutable.</p>

 */
public final class EventText extends HltContent implements HasAttributes {
    private final IType eventType;
    private final ImmutableSet<Chunk> provenanceChunks;
    private final ImmutableMap<IType, Float> attributes;
    private final Float score;
	
    private EventText(IType eventType, ImmutableSet<Chunk> provenanceChunks, ImmutableMap<IType, Float> attributes, Float score) {
        this.eventType = checkNotNull(eventType);

        // these are never null
        this.provenanceChunks = ImmutableSet.copyOf(provenanceChunks);

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
     * @return Will never be empty or contain {@code null}.
     */
    public ImmutableSet<Chunk> getProvenanceChunks() {
        return provenanceChunks;
    }

    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }

    @Override
    public ImmutableMap<IType,Float> getAttributes() {
        return attributes;
    }

    /**
     * Creates an object which can create an {@code EventProvenance} with the specified type
     * and provenance chunks.
     * @param eventType May not be {@code null}.
     * @param provenanceChunks May not be empty and may not contain {@code null}.
     */
    public static Builder builder(IType eventType, Iterable<? extends Chunk> provenanceChunks) {
        return new Builder(eventType, provenanceChunks);
    }

    /**
     * Creates an object which can create an {@code EventProvenance} with the specified type
     * and provenance chunks.
     * @param eventType May not be {@code null}.
     * @param provenanceChunk May not be {@code null}.
     */
    public static Builder builder(IType eventType, Chunk provenanceChunk) {
        return new Builder(eventType, ImmutableSet.of(provenanceChunk));
    }

    public static final class Builder {
    	private IType eventType;
        private ImmutableSet.Builder<Chunk> provenanceChunks = ImmutableSet.builder();
        private ImmutableMap.Builder<IType, Float> attributes = ImmutableMap.builder();
        private Float score = null;
        
        private Builder(IType eventType, Iterable<? extends Chunk> provenanceChunks) {
        	// Do checks here because called from 2 places.
        	checkArgument(eventType!=null);
        	this.eventType = eventType;
        	
        	checkArgument(provenanceChunks!=null);
            for (final Chunk chunk : provenanceChunks) {
            	checkArgument(chunk!=null);
            }        	
            this.provenanceChunks.addAll(provenanceChunks);
        }

        /**
         * Sets the score of the {@code EventProvenance} to be built. If not called,
         * the score will be {@link com.google.common.base.Optional#absent()}.
         * @param score
         * @return
         */
        public Builder setScore(float score) {
            this.score = score;
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

        public EventText build() {
            return new EventText( eventType, provenanceChunks.build(), attributes.build(), score);
        }
    }
}

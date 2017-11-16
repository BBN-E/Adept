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

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;

/**
 * Internal abstract class representing the commonality between
 * SentimentMention and BeliefMention.
 *  
 */
public abstract class MentalStateMention extends HltContent implements Serializable {
	private static final long serialVersionUID = -7811879486102889151L;

	/** text chunks representing this MentalState in document, and associated confidence*/
    protected final Chunk justification;
	
    protected final ImmutableSet<Filler> arguments;
	
    /** overall confidence associated with the MentalState mention */
    protected final float confidence;    
    
    // constructor
    protected MentalStateMention(Chunk justification, ImmutableSet<Filler> arguments, float confidence) {
        this.justification = justification;
        this.arguments = ImmutableSet.copyOf(arguments);
        this.confidence = confidence;
    }    

    public float getConfidence() {
        return confidence;
    }    
    
    /**
     * @return May be empty, no element may be {@code null}.
     */
    public ImmutableSet<Filler> getArguments() {
        return arguments;
    }

    /**
     * @return May be empty, may not contain {@code null}, but may differ
     * in MentalState type from this.
     */
    public Chunk getJustification() {
        return justification;
    }    
    
    public static abstract class Builder<BuilderType extends Builder<BuilderType>> {
        protected Chunk justification;
        protected ImmutableSet.Builder<Filler> arguments = ImmutableSet.builder();
        protected float confidence = 1.0f;
    	
        protected Builder() { }

        /**
         * Sets the confidence of the mental state being built. If not called, the confidence will be
         * 1.
         * 
         * @param confidence the confidence to set
         * 
         * @return the BuilderType instance.  Useful for chaining.
         */
        public BuilderType setConfidence(float confidence) {
            this.confidence = confidence;
            return me();
        }
        
        /**
         * @param arguments May not contain {@code null}, but may be empty.
         *                  will be thrown.
         * @return the BuilderType instance.  Useful for chaining.
         */
        public BuilderType addArguments(Iterable<Filler> arguments) {
            checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return me();
        }

        /**
         * @param argument May not be {@code null}.
         * @return the BuilderType instance.  Useful for chaining.
         */
        public BuilderType addArgument(Filler argument) {
            checkArgument(argument!=null);
            this.arguments.add(argument);
            return me();
        }

        /**
         * @param justification may not be {@code null}.
         * @return the BuilderType instance.  Useful for chaining.
         */
        public BuilderType addJustification(Chunk justification) {
        	checkArgument(justification!=null);
            this.justification = justification;
            return me();
        }
        
        public abstract MentalStateMention build();
        
        protected abstract BuilderType me();
    }
    
    /**
     * A filler for a role in a textual MentalState mention.  Such role fillers may be of a limited
     * number of different types, currently {@link EntityMention}, {@link RelationMention} and
     * {@link RelationMention.Filler}. This class represents that uncertainty. The available
     * types may expand in the future.
	 *
     * This class is locally immutable.
     */
    public static final class Filler implements Serializable {
		private static final long serialVersionUID = 8795388958322463713L;
		private final Optional<EntityMention> entityMention;
        private final Optional<NumberPhrase> strength;
    	private final Optional<RelationMention> relationMention;        
        private final Optional<RelationMention.Filler> relationArgument;
        private final Optional<EventMention> eventMention;        
        private final Optional<EventMentionArgument> eventArgument;
    	
        private final float confidence;
    	private final IType argumentType;
    	
        private Filler(EntityMention entityMention, NumberPhrase strength, RelationMention relationMention, 
                RelationMention.Filler relationArgument, EventMention eventMention, EventMentionArgument eventMentionArgument, IType argType, float confidence) {
            this.argumentType = argType;        	
            this.entityMention = Optional.fromNullable(entityMention);
            this.strength = Optional.fromNullable(strength);
            this.relationMention = Optional.fromNullable(relationMention);
            this.relationArgument = Optional.fromNullable(relationArgument);
            this.eventMention = Optional.fromNullable(eventMention);
            this.eventArgument = Optional.fromNullable(eventMentionArgument);
            this.confidence = confidence;
        }

        public Optional<EntityMention> asEntityMention() {
            return entityMention;
        }
        
        public Optional<NumberPhrase> asStrength() {
            return strength;
        }

        public Optional<RelationMention> asRelationMention() {
            return relationMention;
        }
        
        public Optional<RelationMention.Filler> asRelationArgument() {
            return relationArgument;
        }
        
        public Optional<EventMention> asEventMention() {
            return eventMention;
        }
        
        public Optional<EventMentionArgument> asEventArgument() {
            return eventArgument;
        }
        
        /**
         * @param entityMention May not be null.
         */
        public static Filler createSourceArgument(EntityMention entityMention, float confidence) {
            return new Filler(checkNotNull(entityMention), null, null,null, null, null, new Type("source"), confidence);
        }
        
        public static Filler createStrengthArgument(NumberPhrase strength, float confidence) {
            return new Filler(null, strength, null, null, null, null, new Type("strength"), confidence);
        }
        
        /**
         * @param entityMention May not be null.
         */
        public static Filler createTargetArgument(EntityMention entityMention, float confidence) {
            return new Filler(checkNotNull(entityMention), null, null, null,null, null, new Type("target"), confidence);
        }
        
        /**
         * @param relationMention May not be null.
         */
        public static Filler createTargetArgument(RelationMention relationMention, float confidence) {
            return new Filler(null, null, checkNotNull(relationMention), null, null, null, new Type("target"), confidence);
        }
        
        /**
         * @param relationMention May not be null.
         * @param relationArgument May not be null.
         */
        public static Filler createTargetArgument(RelationMention relationMention, RelationMention.Filler relationArgument, float confidence) {
            return new Filler(null, null, checkNotNull(relationMention), checkNotNull(relationArgument), null, null, new Type("target"), confidence);
        }
        
        /**
         * @param eventMention May not be null.
         */
        public static Filler createTargetArgument(EventMention eventMention, float confidence) {
            return new Filler(null, null, null, null, checkNotNull(eventMention), null, new Type("target"), confidence);
        }
        
        /**
         * @param eventMention May not be null.
         * @param eventMentionArgument May not be null.
         */
        public static Filler createTargetArgument(EventMention eventMention, EventMentionArgument eventMentionArgument, float confidence) {
            return new Filler(null, null, null, null, checkNotNull(eventMention), checkNotNull(eventMentionArgument), new Type("target"), confidence);
        }
        
        /**
         * 
         */
        public String getArgumentType()
        {
            return this.argumentType.getType();
        }        
        
        /**
         * 
         */
        public float getConfidence()
        {
            return this.confidence;
        }
    }        
}

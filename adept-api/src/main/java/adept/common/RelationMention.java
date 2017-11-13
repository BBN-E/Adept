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


import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;


/**
 * <p>Represents a textual relation. This class has been
 * newly introduced in the Adept API release v2.0.</p>
 *
 * <p>The relation mention arguments are restricted to the types
 * {@link adept.common.EntityMention} and {@link adept.common.TimePhrase}.
 * In future releases, arguments will also be allowed to reference a numerical
 * value.</p>
 *
 * <p>Instances of this class may be assigned as provenances
 * to instances of type {@link adept.common.DocumentRelation}.</p>
 *
 */
public final class RelationMention extends HltContent implements Serializable  {

	private static final long serialVersionUID = 3100057606290287372L;

	/** relation type from ontology */
	private final IType relationType;

	/** text chunks representing this relation in document, and associated confidence*/
    private final Chunk justification;

	/** textual arguments of the relation mention */
    private final ImmutableSet<Filler> arguments;

	/** overall confidence associated with the relation mention */
    private  final float confidence;

	// constructor
    private RelationMention(IType relationType, Chunk justification, ImmutableSet<Filler> arguments,
    		float confidence) {
        this.relationType = checkNotNull(relationType);
        this.justification = justification;
        this.arguments = ImmutableSet.copyOf(arguments);
        this.confidence = confidence;
    }


    /**
     * @return May not be null.
     */
    public IType getRelationType() {
        return relationType;
    }

    public float getConfidence() {
        return confidence;
    }


    /**
     * @return May be empty, no element may be {@code null}. All arguments
     * will have the same relation type as this.
     */
    public ImmutableSet<Filler> getArguments() {
        return arguments;
    }

    /**
     * @return May be empty, may not contain {@code null}, but may differ
     * in relation type from this.
     */
    public Chunk getJustification() {
        return justification;
    }


    /**
     * @return an instance of RelationMention from ArgumentTuple, if
     * arguments satisfy
     */
    public static Optional<RelationMention> fromArgumentTuple(ArgumentTuple t) {
    	  final RelationMention.Builder ret = RelationMention.builder(t.getIType());
    	  for (final Argument arg : t.getArguments()) {
    	    if (arg.getBestArgument() instanceof EntityMention) {
    	       ret.addArgument(Filler.fromEntityMention((EntityMention)arg.getBestArgument(), arg.getArgumentIType(), arg.getConfidence()));
    	    } else if (arg.getBestArgument() instanceof TimePhrase) {
    	      ret.addArgument(Filler.fromTimePhrase((TimePhrase)arg.getBestArgument(), arg.getArgumentIType(), arg.getConfidence()));
    	    }//TODO: also check for number phrase
    	    else {
    	      return Optional.absent();
    	    }
    	  }
    	  return Optional.of(ret.build());
    }

    /**
     * @param relationType May not be {@code null}.
     * @return
     */
    public static Builder builder(IType relationType) {
        return new Builder(relationType);
    }

    public static final class Builder {
        private final IType relationType;
        private Chunk justification;
        private ImmutableSet.Builder<Filler> arguments = ImmutableSet.builder();
        private float confidence = 1.0f;

        private Builder(IType relationType) {
            this.relationType = checkNotNull(relationType);
        }

        /**
         * Sets the confidence of the relation being built. If not called, the confidence will be
         * 1.
         */
        public Builder setConfidence(float confidence) {
            this.confidence = confidence;
            return this;
        }

        /**
         * @param arguments May not contain {@code null}, but may be empty. All must have the
         *                  same relation type as this, or an {@link java.lang.IllegalArgumentException}
         *                  will be thrown.
         * @return
         */
        public Builder addArguments(Iterable<Filler> arguments) {
        	checkArgument(arguments!=null);
            this.arguments.addAll(arguments);
            return this;
        }

        /**
         * @param argument May not be {@code null}. Must have the same relation type as this or
         *                 an {@link java.lang.IllegalArgumentException} will be thrown.
         * @return
         */
        public Builder addArgument(Filler argument) {
        	checkArgument(argument!=null);
        	this.arguments.add(argument);
            return this;
        }


        /**
         * @param justification may not be {@code null}.
         * @return
         */
        public Builder addJustification(Chunk justification) {
        	checkArgument(justification!=null);
            this.justification = justification;
            return this;
        }

        public RelationMention build() {
            return new RelationMention(relationType, justification, arguments.build(),
                    confidence);
        }
    }


    /**
     * A filler for a role in a textual relation mention.  Such role fillers may be of a limited
     * number of different types, currently {@link EntityMention}, {@link TimePhrase} and
     * {@link NumberPhrase}. This class represents that uncertainty. The available
     * types may expand in the future.
	 *
     * This class is locally immutable.
     */
    public static final class Filler implements Serializable {

		private static final long serialVersionUID = 6331603412832460507L;
		private final Optional<EntityMention> entityMention;
    	private final Optional<TimePhrase> timePhrase;
    	private final Optional<NumberPhrase> numberPhrase;
        private final Optional<Chunk> genericChunk;

    	private final IType argumentType;
    	private final float confidence;

        private Filler(EntityMention entityMention, TimePhrase timePhrase, NumberPhrase numberPhrase, Chunk genericChunk, IType argType, float confidence) {
            this.argumentType = argType;

        	this.entityMention = Optional.fromNullable(entityMention);
            this.timePhrase = Optional.fromNullable(timePhrase);
            this.numberPhrase = Optional.fromNullable(numberPhrase);
            this.genericChunk = Optional.fromNullable(genericChunk);

            this.confidence = confidence;
        }

        public Optional<EntityMention> asEntityMention() {
            return entityMention;
        }

        public Optional<TimePhrase> asTimePhrase() {
        	return timePhrase;
        }


        public Optional<NumberPhrase> asNumberPhrase() {
        	return numberPhrase;
        }

        public Optional<Chunk> asGenericChunk() {
            return genericChunk;
        }


        /**
         * Convenience method that casts the appropriate Filler to Chunk.
         */
        public Optional<Chunk> asChunk() {
            if (entityMention.isPresent()) {
                return Optional.of((Chunk)entityMention.get());
            } else if (timePhrase.isPresent()) {
                return Optional.of((Chunk)timePhrase.get());
            } else if (numberPhrase.isPresent()) {
                return Optional.of((Chunk)numberPhrase.get());
            } else if (genericChunk.isPresent()) {
                return genericChunk;
            } else {
                return Optional.absent();
            }
        }

        /**
         * @param m May not be null.
         */
        public static Filler fromEntityMention(EntityMention m, IType argType, float confidence) {
            return new Filler(checkNotNull(m), null, null, null, checkNotNull(argType), confidence);
        }

        /**
         * @param timePhrase May not be null.
         */
        public static Filler fromTimePhrase(TimePhrase timePhrase, IType argType, float confidence) {
            return new Filler(null, checkNotNull(timePhrase), null, null, checkNotNull(argType), confidence);
        }

        public static Filler fromNumberPhrase(NumberPhrase numberPhrase, IType argType, float confidence){
        	return new Filler(null, null, checkNotNull(numberPhrase), null, checkNotNull(argType), confidence);
        }

        public static Filler fromGenericChunk(Chunk genericChunk, IType argType, float confidence){
        	return new Filler(null, null, null, checkNotNull(genericChunk), checkNotNull(argType), confidence);
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

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Filler filler = (Filler) o;

            if (Float.compare(filler.confidence, confidence) != 0) {
                return false;
            }
            if (!entityMention.equals(filler.entityMention)) {
                return false;
            }
            if (!timePhrase.equals(filler.timePhrase)) {
                return false;
            }
            if (!numberPhrase.equals(filler.numberPhrase)) {
                return false;
            }
            if (!genericChunk.equals(filler.genericChunk)) {
                return false;
            }
            return argumentType.equals(filler.argumentType);

        }

        @Override
        public int hashCode() {
            int result = entityMention.hashCode();
            result = 31 * result + timePhrase.hashCode();
            result = 31 * result + numberPhrase.hashCode();
            result = 31 * result + genericChunk.hashCode();
            result = 31 * result + argumentType.hashCode();
            result = 31 * result + (confidence != +0.0f ? Float.floatToIntBits(confidence) : 0);
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

        final RelationMention that = (RelationMention) o;

        if (Float.compare(that.confidence, confidence) != 0) {
            return false;
        }
        if (!relationType.equals(that.relationType)) {
            return false;
        }
        if (justification != null ? !justification.equals(that.justification)
                                  : that.justification != null) {
            return false;
        }
        return arguments.equals(that.arguments);

    }

    @Override
    public int hashCode() {
        int result = relationType.hashCode();
        result = 31 * result + (justification != null ? justification.hashCode() : 0);
        result = 31 * result + arguments.hashCode();
        result = 31 * result + (confidence != +0.0f ? Float.floatToIntBits(confidence) : 0);
        return result;
    }
}
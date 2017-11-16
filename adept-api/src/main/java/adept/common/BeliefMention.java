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
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;

/**
 * <p>Represents a textual belief. This class has been 
 * newly introduced in the Adept API release v2.1.</p>
 * 
 * <p>The belief mention arguments are restricted to the types 
 * {@link adept.common.EntityMention}, {@link RelationMention.Filler},
 * {@link adept.common.RelationMention}, and {@link Integer}.</p> 
 * 
 * <p>Instances of this class may be assigned as provenances
 * to instances of type {@link adept.common.DocumentBelief}.</p>
 * 
 */
public class BeliefMention extends MentalStateMention implements Serializable  {

	private static final long serialVersionUID = -8884473385563395021L;

	// constructor
    private BeliefMention(Chunk justification, ImmutableSet<Filler> arguments, float confidence) {
        super(justification, arguments, confidence);
    }    
    
    /**
     * @return a new BeliefMention builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MentalStateMention.Builder<Builder> {
        @Override
        public BeliefMention build() {
            ImmutableSet<Filler> builtArguments = arguments.build();
            boolean doesContainSourceArgument = false;
            boolean doesContainStrengthArgument = false;
            boolean doesContainTargetArgument = false;
            for (Filler arg : builtArguments.asList()) {
                if (arg.getArgumentType().equals("source")) {
                    doesContainSourceArgument = true;
                } else if (arg.getArgumentType().equals("strength")) {
                    doesContainStrengthArgument = true;
                    if (arg.asStrength().get().getNumber().intValue() < -2 || arg.asStrength().get().getNumber().intValue() > 2) {
                        throw new RuntimeException("BeliefMention strength argument must be an integer between -2 and 2 inclusive.");
                    }
                } else if (arg.getArgumentType().equals("target")) {
                    doesContainTargetArgument = true;
                }
            }
            if (!doesContainSourceArgument || !doesContainStrengthArgument || !doesContainTargetArgument) {
                throw new RuntimeException("BeliefMention argument set must contain must contain at least one argument each of source, strength, and target types.");
            }
            
            return new BeliefMention(justification, builtArguments, confidence);
        }
        
        protected Builder me(){
        	return this;
        }
    }
}

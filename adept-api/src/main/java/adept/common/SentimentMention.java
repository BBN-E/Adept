/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.common;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ImmutableSet;

/**
 * <p>Represents a textual sentiment. This class has been 
 * newly introduced in the Adept API release v2.1.</p>
 * 
 * <p>The sentiment mention arguments are restricted to the types 
 * {@link adept.common.EntityMention}, {@link RelationMention.Filler},
 * {@link adept.common.RelationMention}, and {@link Integer}.</p> 
 * 
 * <p>Instances of this class may be assigned as provenances
 * to instances of type {@link adept.common.DocumentSentiment}.</p>
 * 
 */
public class SentimentMention extends MentalStateMention  {
    
    // constructor
    private SentimentMention(Chunk justification, ImmutableSet<Filler> arguments, float confidence) {
        super(justification, arguments, confidence);
    }    
    
    /**
     * @return
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends MentalStateMention.Builder {
        @Override
        public SentimentMention build() {
            ImmutableSet<Filler> builtArguments = arguments.build();
            boolean doesContainSourceArgument = false;
            boolean doesContainStrengthArgument = false;
            boolean doesContainTargetArgument = false;
            for (Filler arg : builtArguments.asList()) {
                if (arg.getArgumentType().equals("source")) {
                    doesContainSourceArgument = true;
                } else if (arg.getArgumentType().equals("strength")) {
                    doesContainStrengthArgument = true;
                    if (arg.asStrength().get().getNumber().intValue() < -3 || arg.asStrength().get().getNumber().intValue() > 3) {
                        throw new RuntimeException("SentimentMention strength argument must be an integer between -3 and 3 inclusive.");
                    }
                } else if (arg.getArgumentType().equals("target")) {
                    doesContainTargetArgument = true;
                }
            }
            if (!doesContainSourceArgument || !doesContainStrengthArgument || !doesContainTargetArgument) {
                throw new RuntimeException("SentimentMention argument set must contain must contain at least one argument each of source, strength, and target types.");
            }
            
            return new SentimentMention(justification, builtArguments, confidence);
        }
    }
}

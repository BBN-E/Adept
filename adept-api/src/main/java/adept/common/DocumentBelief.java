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

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.collect.ImmutableSet;
import java.io.Serializable;

/**
 * <p>Represents a belief at the level of participating document-level entities, relations, and arguments,
 * rather than strings of text. The arguments of this class must necessarily be references
 * to document-level entities, relations, relation arguments, or temporal values, i.e. instances of class 
 * {@link adept.common.Entity}, {@link adept.common.Relation},
 * or {@link adept.common.TemporalValue}.</p>
 * 
 * <p>Objects of this class are associated with provenances 
 * that are instances of {@link adept.common.BeliefMention}. </p>
 * 
 * <p>This is a new class introduced in the API release version 2.1, and is the
 * primary class to be used in inserting beliefs into the Adept KB.</p>
 */
public class DocumentBelief extends DocumentMentalState<BeliefMention> implements Serializable {
    
	private static final long serialVersionUID = -5546788409250416409L;

	private DocumentBelief(ImmutableSet<BeliefMention> provenances, ImmutableSet<DocumentMentalStateArgument> arguments, float confidence) {
        super(provenances, arguments, confidence, MentalStateType.Belief);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends DocumentMentalState.Builder<Builder, BeliefMention> {
        @Override
        public DocumentBelief build() {
            ImmutableSet<DocumentMentalStateArgument> builtArguments = arguments.build();
            boolean doesContainSourceArgument = false;
            boolean doesContainStrengthArgument = false;
            boolean doesContainTargetArgument = false;
            for (DocumentMentalStateArgument arg : builtArguments.asList()) {
                if (arg.getRole().getType().equals("source")) {
                    doesContainSourceArgument = true;
                } else if (arg.getRole().getType().equals("strength")) {
                    doesContainStrengthArgument = true;
                    if (arg.getFiller().asStrength().get().asNumber().intValue() < -3 || arg.getFiller().asStrength().get().asNumber().intValue() > 3) {
                        throw new RuntimeException("DocumentBelief strength argument must be an integer between -2 and 2 inclusive.");
                    }
                } else if (arg.getRole().getType().equals("target")) {
                    doesContainTargetArgument = true;
                }
            }
            if (!doesContainSourceArgument || !doesContainStrengthArgument || !doesContainTargetArgument) {
                throw new RuntimeException("DocumentBelief argument set must contain must contain at least one argument each of source, strength, and target types.");
            }
            
            return new DocumentBelief(provenances.build(), builtArguments, confidence);
        }
        
        protected Builder me(){
        	return this;
        }
    }
}
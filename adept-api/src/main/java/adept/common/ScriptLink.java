/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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

import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a link between two script events.  The semantics of such links are
 * algorithm-dependant.
 *
 * All script-related classes are "locally immutable" so long as the {@link adept.common.IType} implementation
 * used is. However, they all inherit from the mutable {@link adept.common.HltContent} class, whose associated
 * fields are mutable.   The {@link #hashCode()} and  {@link #equals(Object)} implementations are the default.
 */
public final class ScriptLink {
    private final IType linkType;
    private final ScriptEvent leftEvent;
    private final ScriptEvent rightEvent;
    private final Float score;

    private ScriptLink(IType linkType, ScriptEvent leftEvent, ScriptEvent rightEvent,
                       Float score) {
        checkArgument(linkType != null);
        checkArgument(leftEvent != null);
        checkArgument(rightEvent != null);
        this.linkType = linkType;
        this.leftEvent = leftEvent;
        this.rightEvent = rightEvent;
        this.score = score;
    }

    /**
     * Returns the type of the link between the events.  Refer to the ontology used by the algorithm
     * producing this for interpretation.
     * @return Will never be {@code null}.
     */
    public IType getLinkType() {
        return linkType;
    }

    /**
     * @return  Will never be {@code null}.
     */
    public ScriptEvent getLeftEvent() {
        return leftEvent;
    }

    /**
     * @return Will never be {@code null}.
     */
    public ScriptEvent getRightEvent() {
        return rightEvent;
    }

    /**
     * The interpretation of the score of a link, if provided, is algorithm-dependant.
      * @return
     */
    public Optional<Float> getScore() {
        return Optional.fromNullable(score);
    }


    /**
     * Creates an unscored {@code ScriptLink}
     * @param linkType May not be {@code null}.
     * @param leftEvent May not be {@code null}.
     * @param rightEvent May not be {@code null}.
     */
    public static ScriptLink create(IType linkType, ScriptEvent leftEvent, ScriptEvent rightEvent) {
        return new ScriptLink( linkType,  leftEvent,  rightEvent, null);
    }

    /**
     * Creates a scroed {@code ScriptLink}
     * @param linkType May not be {@code null}.
     * @param leftEvent May not be {@code null}.
     * @param rightEvent May not be {@code null}.
     * @param score
     */
    public static ScriptLink create(IType linkType, ScriptEvent leftEvent, ScriptEvent rightEvent, float score)    {
        return new ScriptLink( linkType,  leftEvent,  rightEvent, score);
    }

}
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

import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents a generic pattern of events with some relation to one another.
 * For example, "criminal justice proceedings", which would involve charges, arrests, trials,
 * sentenceings, etc.
 * <p/>
 * All script-related classes are "locally immutable" so long as the {@link adept.common.IType} implementation
 * used is. However, they all inherit from the mutable {@link adept.common.HltContent} class, whose associated
 * fields are mutable.   The {@link #hashCode()} and  {@link #equals(Object)} implementations are the default.
 */
public final class Script extends HltContent {

    private final ImmutableSet<ScriptEvent> scriptEvents;
    private final ImmutableSet<ScriptLink> scriptLinks;
    private final ImmutableSet<ScriptVariableBinaryConstraint> binaryConstraints;

    private Script(Iterable<ScriptEvent> scriptEvents,
                   Iterable<ScriptLink> scriptLinks,
                   Iterable<ScriptVariableBinaryConstraint> binaryConstraints) {
        this.scriptEvents = ImmutableSet.copyOf(scriptEvents);
        this.scriptLinks = ImmutableSet.copyOf(scriptLinks);
        this.binaryConstraints = ImmutableSet.copyOf(binaryConstraints);
    }

    /**
     * Creates a new {@code Script} from the specified events, links, and constraints.
     *
     * @param scriptEvents      May not be empty, may not contain {@code null}.
     * @param scriptLinks       May be empty, but may not contain {@code null}.
     * @param binaryConstraints May be empty, but may not contain {@code null}.
     * @return
     */
    public static Script create(Iterable<ScriptEvent> scriptEvents,
                                Iterable<ScriptLink> scriptLinks,
                                Iterable<ScriptVariableBinaryConstraint> binaryConstraints) {
        checkArgument(scriptEvents != null);
        checkArgument(scriptEvents.iterator().hasNext());
        for (ScriptEvent arg : scriptEvents) {
            checkArgument(arg != null);
        }
        checkArgument(scriptLinks != null);
        for (ScriptLink arg : scriptLinks) {
            checkArgument(arg != null);
        }
        checkArgument(binaryConstraints != null);
        for (ScriptVariableBinaryConstraint arg : binaryConstraints) {
            checkArgument(arg != null);
        }
        return new Script(scriptEvents, scriptLinks, binaryConstraints);
    }

    /**
     * The events in the script.
     *
     * @return May not be empty, may not contain null.
     */
    public ImmutableSet<ScriptEvent> getScriptEvents() { return scriptEvents; }

    /**
     * The relationships between the events in the script.
     *
     * @return May be empty, but may not contain {@code null}.
     */
    public ImmutableSet<ScriptLink> getScriptLinks() { return scriptLinks; }

    /**
     * Any constraints which hold between variables in the script.
     *
     * @return May be empty, may not contain {@code null}.
     */
    public ImmutableSet<ScriptVariableBinaryConstraint> getBinaryConstraints() { return binaryConstraints; }
}

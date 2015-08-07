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

import com.google.common.collect.ImmutableSet;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * An event in a {@link adept.common.Script}.
 *
 *  The local data of
 * this class is immutable so long as the {@link adept.common.IType} used is, which is very likely, but the data inherited
 * from {@link adept.common.HltContent} and {@link adept.common.Item} is mutable.  The {@link #hashCode()} and
 * {@link #equals(Object)} implementations are the default.
 */
public final class ScriptEvent {
    private final ImmutableSet<ScriptEventArgument> scriptEventArguments;
    private final IType eventType;

    private ScriptEvent(IType eventType, Iterable<ScriptEventArgument> arguments) {
        this.eventType = eventType;
        this.scriptEventArguments = ImmutableSet.copyOf(arguments);
    }

    /**
     * @return Will never be {@code null}.
     */
    public IType getEventType() { return eventType; }

    /**
     * @return Will never be empty or contain {@code null}.
     */
    public ImmutableSet<ScriptEventArgument> getArguments() { return scriptEventArguments; }

    /**
     * Creates a {@code ScriptEvent}.
     * @param eventType May not be {@code null}.
     * @param arguments May not be empty or contain {@code null}.
     */
    public static final ScriptEvent create(IType eventType, Iterable<ScriptEventArgument> arguments) {
        checkArgument(eventType != null);
        checkArgument(arguments != null);
        checkArgument(arguments.iterator().hasNext());
        for (ScriptEventArgument arg : arguments) {
            checkArgument(arg != null);
        }
        return new ScriptEvent( eventType,  arguments);
    }
}
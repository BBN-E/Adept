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

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Represents an event argument in a {@link adept.common.Script}.
 *
 * All script-related classes are "locally immutable" so long as the {@link adept.common.IType} implementation
 * used is. However, they all inherit from the mutable {@link adept.common.HltContent} class, whose associated
 * fields are mutable.   The {@link #hashCode()} and  {@link #equals(Object)} implementations are the default.
 */
public final class ScriptEventArgument {
    IType role;
    ScriptVariable variable;

    private ScriptEventArgument(IType role, ScriptVariable variable) {

        this.role=role;
        this.variable=variable;
    }

    /**
     * The role of the argument.
     * @return Will never be {@code null}.
     */
    public IType getRole() {
        return role;
    }

    /**
     * The variable for the argument role filler.
     * @return Will never be {@code null}.
     */
    public ScriptVariable getVariable() {
        return variable;
    }

    /**
     * Creates a new {@code ScriptEventArgument}.
     * @param role May not be {@code null}.
     * @param variable May not be {@code null}.
     * @return
     */
    public static ScriptEventArgument create(IType role, ScriptVariable variable) {
        checkArgument(role != null);
        checkArgument(variable != null);
        return new ScriptEventArgument( role,  variable);
    }
}
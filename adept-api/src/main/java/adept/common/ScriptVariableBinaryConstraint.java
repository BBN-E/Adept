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
 * A constraint which holds between two variables in an event script. For example, the time variable for a
 * conviction event may be constrained to be after the time variable for an indictment event.  The semantics of
 * the constraint are specified by the constraint type, which must be interpreted by the application.
 *
 * All script-related classes are "locally immutable" so long as the {@link adept.common.IType} implementation
 * used is. However, they all inherit from the mutable {@link adept.common.HltContent} class, whose associated
 * fields are mutable.   The {@link #hashCode()} and  {@link #equals(Object)} implementations are the default.
 */
public final class ScriptVariableBinaryConstraint extends HltContent {
    private final IType constraintType;
    private final ScriptVariable leftVariable;
    private final ScriptVariable rightVariable;

    private ScriptVariableBinaryConstraint(IType constraintType,
                                           ScriptVariable leftVariable, ScriptVariable rightVariable) {
        this.constraintType = constraintType;
        this.leftVariable = leftVariable;
        this.rightVariable = rightVariable;

    }

    /**
     * Returns the constraint which holds between script variables.  Refer to the ontology used by the algorithm
     * producing the ScriptVariableBinaryConstraint for interpretation.
     * @return Will never be null.
     */
    private final IType getConstraintType() { return constraintType; }

    /**
     *
     * @return Will never be null.
     */
    private final ScriptVariable getLeftVariable() {
        return leftVariable;
    }

    /**
     *
     * @return Will never be null.
     */
    private final ScriptVariable getRightVariable() { return rightVariable; }

    /**
     * Creates a binary constraint between script variables.
     *
     * @param constraintType May not be null.
     * @param leftVariable May not be null.
     * @param rightVariable May not be null.
     * @return
     */
    public static final ScriptVariableBinaryConstraint create(IType constraintType,
                          ScriptVariable leftVariable, ScriptVariable rightVariable)
    {
        checkArgument(constraintType != null);
        checkArgument(leftVariable != null);
        checkArgument(rightVariable != null);
        return new ScriptVariableBinaryConstraint( constraintType, leftVariable,  rightVariable);
    }
}
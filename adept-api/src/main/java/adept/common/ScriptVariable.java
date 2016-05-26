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
import com.google.common.collect.UnmodifiableIterator;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A variable in an event script. For example, in a criminal justice event, there would probably be
 * separate variables for the criminal, the victim, the prosecutor, etc.  These variables may then
 * appear in multiple {@link ScriptEventArgument}s.
 *
 * All script-related classes are "locally immutable" so long as the {@link adept.common.IType} implementation
 * used is. However, they all inherit from the mutable {@link adept.common.HltContent} class, whose associated
 * fields are mutable.   The {@link #hashCode()} and  {@link #equals(Object)} implementations are the default.
 *
 */
public final class ScriptVariable extends HltContent {

    private final ImmutableSet<IType> types;

    private ScriptVariable(Iterable<? extends IType> types) {
        this.types=ImmutableSet.copyOf(types);
    }

    /**
     * The types allowed to fill this variable.  For interpretation of types, refer to the ontology used
     * by the generating algorithm.
     *
     * @return Will never be empty and will never contain {@code null}.
     */
    public ImmutableSet<IType> getAllowableTypes() {
        return types;
    }

    /**
     * Creates a script variable with only a single allowable type.
     * @param type May not be {@code null}.
     */
    public static ScriptVariable createOfType(IType type) {
        checkArgument(type!=null);
        ImmutableSet<IType> types = new ImmutableSet.Builder<IType>().add(type).build();
        return new  ScriptVariable(types);
    }

    /**
     * Creates a script variable with multiple allowable types.
     * @param types May not be empty, may not contain {@code null}.
     * @return
     */
    public static ScriptVariable createOfTypes(Iterable<? extends IType> types) {
        checkArgument(types!=null);
        for( IType arg : types) {
            checkArgument(arg!=null);
        }
        return new ScriptVariable(types);
    }
}

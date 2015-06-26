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

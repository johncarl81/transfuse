package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.variableBuilder.VariableBuilder;

/**
 * Aspect to associate with the InjectionNode a builder used to build the current type in the given scope.
 *
 * @author John Ericksen
 */
public class ScopeAspect {

    private final VariableBuilder scopeBuilder;

    public ScopeAspect(VariableBuilder scopeBuilder) {
        this.scopeBuilder = scopeBuilder;
    }

    public VariableBuilder getScopeBuilder() {
        return scopeBuilder;
    }
}

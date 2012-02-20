package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.variableBuilder.VariableBuilder;

/**
 * @author John Ericksen
 */
public class ScopeAspect {

    private VariableBuilder scopeBuilder;

    public ScopeAspect(VariableBuilder scopeBuilder) {
        this.scopeBuilder = scopeBuilder;
    }

    public VariableBuilder getScopeBuilder() {
        return scopeBuilder;
    }
}

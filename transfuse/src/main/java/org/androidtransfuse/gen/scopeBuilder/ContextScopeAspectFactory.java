package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ContextScopeAspectFactory implements ScopeAspectFactory{

    private ContextScopeVariableBuilder contextScopeVariableBuilder;

    @Inject
    public ContextScopeAspectFactory(ContextScopeVariableBuilder contextScopeVariableBuilder) {
        this.contextScopeVariableBuilder = contextScopeVariableBuilder;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        return new ScopeAspect(contextScopeVariableBuilder);
    }
}

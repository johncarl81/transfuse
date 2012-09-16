package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SingletonScopeAspectFactory implements ScopeAspectFactory {

    private final SingletonScopeBuilder singletonScopeBuilder;

    @Inject
    public SingletonScopeAspectFactory(SingletonScopeBuilder singletonScopeBuilder) {
        this.singletonScopeBuilder = singletonScopeBuilder;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        return new ScopeAspect(singletonScopeBuilder);
    }
}

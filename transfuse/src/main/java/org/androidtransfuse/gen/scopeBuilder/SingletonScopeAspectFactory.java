package org.androidtransfuse.gen.scopeBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class SingletonScopeAspectFactory implements ScopeAspectFactory {

    private final Provider<SingletonScopeBuilder> singletonScopeBuilderProvider;

    @Inject
    public SingletonScopeAspectFactory(Provider<SingletonScopeBuilder> singletonScopeBuilderProvider) {
        this.singletonScopeBuilderProvider = singletonScopeBuilderProvider;
    }

    @Override
    public ScopeAspect buildAspect(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {
        return new ScopeAspect(singletonScopeBuilderProvider.get());
    }
}

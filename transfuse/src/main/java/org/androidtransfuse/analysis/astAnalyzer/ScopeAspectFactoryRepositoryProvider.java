package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.annotations.ContextScope;
import org.androidtransfuse.gen.scopeBuilder.ContextScopeAspectFactory;
import org.androidtransfuse.gen.scopeBuilder.SingletonScopeAspectFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepositoryProvider implements Provider<ScopeAspectFactoryRepository> {

    private SingletonScopeAspectFactory singletonScopeAspectFactory;
    private ContextScopeAspectFactory conextScopeAspectFactory;

    @Inject
    public ScopeAspectFactoryRepositoryProvider(SingletonScopeAspectFactory singletonScopeAspectFactory,
                                                ContextScopeAspectFactory conextScopeAspectFactory) {
        this.singletonScopeAspectFactory = singletonScopeAspectFactory;
        this.conextScopeAspectFactory = conextScopeAspectFactory;
    }


    @Override
    public ScopeAspectFactoryRepository get() {
        ScopeAspectFactoryRepository scopedVariableBuilderRepository = new ScopeAspectFactoryRepository();

        scopedVariableBuilderRepository.putAspectFactory(Singleton.class, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putAspectFactory(ContextScope.class, conextScopeAspectFactory);

        return scopedVariableBuilderRepository;
    }
}

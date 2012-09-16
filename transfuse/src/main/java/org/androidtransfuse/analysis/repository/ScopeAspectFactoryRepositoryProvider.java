package org.androidtransfuse.analysis.repository;

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

    private final SingletonScopeAspectFactory singletonScopeAspectFactory;
    private final ContextScopeAspectFactory contextScopeAspectFactory;

    @Inject
    public ScopeAspectFactoryRepositoryProvider(SingletonScopeAspectFactory singletonScopeAspectFactory,
                                                ContextScopeAspectFactory contextScopeAspectFactory) {
        this.singletonScopeAspectFactory = singletonScopeAspectFactory;
        this.contextScopeAspectFactory = contextScopeAspectFactory;
    }


    @Override
    public ScopeAspectFactoryRepository get() {
        ScopeAspectFactoryRepository scopedVariableBuilderRepository = new ScopeAspectFactoryRepository();

        scopedVariableBuilderRepository.putAspectFactory(Singleton.class, singletonScopeAspectFactory);
        scopedVariableBuilderRepository.putAspectFactory(ContextScope.class, contextScopeAspectFactory);

        return scopedVariableBuilderRepository;
    }
}

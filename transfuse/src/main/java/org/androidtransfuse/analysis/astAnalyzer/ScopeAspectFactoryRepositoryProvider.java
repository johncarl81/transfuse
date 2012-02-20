package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.gen.scopeBuilder.SingletonScopeAspectFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class ScopeAspectFactoryRepositoryProvider implements Provider<ScopeAspectFactoryRepository> {

    private SingletonScopeAspectFactory singletonScopeAspectFactory;

    @Inject
    public ScopeAspectFactoryRepositoryProvider(SingletonScopeAspectFactory singletonScopeAspectFactory) {
        this.singletonScopeAspectFactory = singletonScopeAspectFactory;
    }


    @Override
    public ScopeAspectFactoryRepository get() {
        ScopeAspectFactoryRepository scopedVariableBuilderRepository = new ScopeAspectFactoryRepository();

        scopedVariableBuilderRepository.putAspectFactory(Singleton.class, singletonScopeAspectFactory);

        return scopedVariableBuilderRepository;
    }
}

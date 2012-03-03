package org.androidtransfuse.analysis;

import org.androidtransfuse.gen.InjectionNodeBuilderRepositoryFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class SimpleAnalysisContextFactory {

    @Inject
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    @Inject
    private InjectionNodeBuilderRepositoryFactory variableBuilderRepositoryFactory;
    @Inject
    private Provider<AOPRepository> aopRepositoryProvider;

    public AnalysisContext buildContext() {
        return new AnalysisContext(
                analysisRepositoryFactory.get(),
                variableBuilderRepositoryFactory.get(),
                aopRepositoryProvider.get()
        );
    }
}

package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.repository.AOPRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;

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
                variableBuilderRepositoryFactory.get(),
                analysisRepositoryFactory.get(),
                aopRepositoryProvider.get()
        );
    }
}

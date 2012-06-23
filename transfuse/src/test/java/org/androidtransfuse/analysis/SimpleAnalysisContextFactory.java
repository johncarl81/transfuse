package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.repository.AOPRepository;
import org.androidtransfuse.analysis.repository.AnalysisRepositoryFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class SimpleAnalysisContextFactory {

    @Inject
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    @Inject
    private InjectionNodeBuilderRepository variableBuilderRepository;
    @Inject
    private Provider<AOPRepository> aopRepositoryProvider;

    public AnalysisContext buildContext() {
        return new AnalysisContext(
                variableBuilderRepository,
                analysisRepositoryFactory.get(),
                aopRepositoryProvider.get()
        );
    }
}

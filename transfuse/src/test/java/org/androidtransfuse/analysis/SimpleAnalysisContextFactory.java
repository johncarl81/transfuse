package org.androidtransfuse.analysis;

import org.androidtransfuse.gen.VariableBuilderRepositoryFactory;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class SimpleAnalysisContextFactory {

    @Inject
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    @Inject
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;
    @Inject
    private Provider<AOPRepository> aopRepositoryProvider;

    public AnalysisContext buildContext() {
        return new AnalysisContext(
                analysisRepositoryFactory.buildAnalysisRepository(),
                variableBuilderRepositoryFactory.buildRepository(),
                aopRepositoryProvider.get()
        );
    }
}

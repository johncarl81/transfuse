package org.androidrobotics.analysis;

import org.androidrobotics.gen.VariableBuilderRepositoryFactory;

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
    private Provider<InterceptorRepository> interceptorRepositoryProvider;
    @Inject
    private Provider<AOPRepository> aopRepositoryProvider;

    public AnalysisContext buildContext() {
        return new AnalysisContext(
                analysisRepositoryFactory.buildAnalysisRepository(),
                variableBuilderRepositoryFactory.buildRepository(),
                interceptorRepositoryProvider.get(),
                aopRepositoryProvider.get()
        );
    }
}

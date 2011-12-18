package org.androidrobotics.analysis;

import org.androidrobotics.gen.VariableBuilderRepositoryFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SimpleAnalysisContextFactory {

    @Inject
    private AnalysisRepositoryFactory analysisRepositoryFactory;
    @Inject
    private VariableBuilderRepositoryFactory variableBuilderRepositoryFactory;

    public AnalysisContext buildContext() {
        return new AnalysisContext(
                analysisRepositoryFactory.buildAnalysisRepository(),
                variableBuilderRepositoryFactory.buildRepository()
        );
    }
}

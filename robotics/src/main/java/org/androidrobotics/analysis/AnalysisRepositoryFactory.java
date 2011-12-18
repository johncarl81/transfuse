package org.androidrobotics.analysis;

import org.androidrobotics.analysis.astAnalyzer.InjectionAnalyzer;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory {

    @Inject
    private InjectionPointFactory injectionPointFactory;

    public AnalysisRepository buildAnalysisRepository() {
        AnalysisRepository analysisRepository = new AnalysisRepository();

        InjectionAnalyzer injectionAnalyzer = new InjectionAnalyzer(injectionPointFactory);

        analysisRepository.addAnalysis(injectionAnalyzer);

        return analysisRepository;
    }
}

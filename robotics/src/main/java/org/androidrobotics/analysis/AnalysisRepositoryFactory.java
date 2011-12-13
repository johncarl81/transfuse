package org.androidrobotics.analysis;

import org.androidrobotics.analysis.astAnalyzer.InjectionAnalyzer;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory {

    private InjectionPointFactory injectionPointFactory;

    public AnalysisRepositoryFactory(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }


    public AnalysisRepository buildAnalysisRepository() {
        AnalysisRepository analysisRepository = new AnalysisRepository();

        InjectionAnalyzer injectionAnalyzer = new InjectionAnalyzer(injectionPointFactory);

        analysisRepository.addAnalysis(injectionAnalyzer);

        return analysisRepository;
    }
}

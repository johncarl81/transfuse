package org.androidrobotics.analysis;

import org.androidrobotics.analysis.astAnalyzer.InjectionAnalyzer;
import org.androidrobotics.analysis.astAnalyzer.MethodCallbackAnalysis;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory {

    @Inject
    private InjectionPointFactory injectionPointFactory;

    public AnalysisRepository buildAnalysisRepository() {
        AnalysisRepository analysisRepository = new AnalysisRepository();

        analysisRepository.addAnalysis(new InjectionAnalyzer(injectionPointFactory));
        analysisRepository.addAnalysis(new MethodCallbackAnalysis());

        return analysisRepository;
    }
}

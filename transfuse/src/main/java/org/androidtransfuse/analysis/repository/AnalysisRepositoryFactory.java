package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.analysis.astAnalyzer.*;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory implements Provider<AnalysisRepository> {

    private AOPProxyAnalyzer aopProxyAnalyzer;
    private InjectionAnalyzer injectionAnalyzer;
    private MethodCallbackAnalysis methodCallbackAnalysis;
    private ScopeAnalysis scopeAnalysis;
    private RegistrationAnalyzer registrationAnalysis;
    private DeclareFieldAnalysis declareFieldAnalysis;
    private ObservesAnalysis observesAnalysis;
    private NonConfigurationAnalysis nonConfigurationAnalysis;

    @Inject
    public AnalysisRepositoryFactory(AOPProxyAnalyzer aopProxyAnalyzer,
                                     InjectionAnalyzer injectionAnalyzer,
                                     MethodCallbackAnalysis methodCallbackAnalysis,
                                     ScopeAnalysis scopeAnalysis,
                                     RegistrationAnalyzer registrationAnalysis,
                                     DeclareFieldAnalysis declareFieldAnalysis,
                                     ObservesAnalysis observesAnalysis,
                                     NonConfigurationAnalysis nonConfigurationAnalysis) {
        this.aopProxyAnalyzer = aopProxyAnalyzer;
        this.injectionAnalyzer = injectionAnalyzer;
        this.methodCallbackAnalysis = methodCallbackAnalysis;
        this.scopeAnalysis = scopeAnalysis;
        this.registrationAnalysis = registrationAnalysis;
        this.declareFieldAnalysis = declareFieldAnalysis;
        this.observesAnalysis = observesAnalysis;
        this.nonConfigurationAnalysis = nonConfigurationAnalysis;
    }

    public AnalysisRepository get() {
        AnalysisRepository analysisRepository = new AnalysisRepository();

        analysisRepository.addAnalysis(aopProxyAnalyzer);
        analysisRepository.addAnalysis(injectionAnalyzer);
        analysisRepository.addAnalysis(methodCallbackAnalysis);
        analysisRepository.addAnalysis(scopeAnalysis);
        analysisRepository.addAnalysis(registrationAnalysis);
        analysisRepository.addAnalysis(declareFieldAnalysis);
        analysisRepository.addAnalysis(observesAnalysis);
        analysisRepository.addAnalysis(nonConfigurationAnalysis);

        return analysisRepository;
    }
}

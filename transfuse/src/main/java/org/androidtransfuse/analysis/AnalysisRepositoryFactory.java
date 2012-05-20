package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.astAnalyzer.*;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory implements Provider<AnalysisRepository> {

    private Provider<AOPProxyAnalyzer> aopProxyAnalyzerProvider;
    private Provider<InjectionAnalyzer> injectionAnalyzerProvider;
    private Provider<MethodCallbackAnalysis> methodCallbackAnalysisProvider;
    private Provider<ScopeAnalysis> scopeAnalysisProvider;
    private Provider<RegistrationAnalyzer> registrationAnalysisProvider;

    @Inject
    public AnalysisRepositoryFactory(Provider<AOPProxyAnalyzer> aopProxyAnalyzerProvider,
                                     Provider<InjectionAnalyzer> injectionAnalyzerProvider,
                                     Provider<MethodCallbackAnalysis> methodCallbackAnalysisProvider,
                                     Provider<ScopeAnalysis> scopeAnalysisProvider,
                                     Provider<RegistrationAnalyzer> registrationAnalysisProvider) {
        this.aopProxyAnalyzerProvider = aopProxyAnalyzerProvider;
        this.injectionAnalyzerProvider = injectionAnalyzerProvider;
        this.methodCallbackAnalysisProvider = methodCallbackAnalysisProvider;
        this.scopeAnalysisProvider = scopeAnalysisProvider;
        this.registrationAnalysisProvider = registrationAnalysisProvider;
    }

    public AnalysisRepository get() {
        AnalysisRepository analysisRepository = new AnalysisRepository();

        analysisRepository.addAnalysis(aopProxyAnalyzerProvider.get());
        analysisRepository.addAnalysis(injectionAnalyzerProvider.get());
        analysisRepository.addAnalysis(methodCallbackAnalysisProvider.get());
        analysisRepository.addAnalysis(scopeAnalysisProvider.get());
        analysisRepository.addAnalysis(registrationAnalysisProvider.get());

        return analysisRepository;
    }
}

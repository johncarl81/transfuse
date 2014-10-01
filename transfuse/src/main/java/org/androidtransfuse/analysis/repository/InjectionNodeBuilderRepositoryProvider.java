package org.androidtransfuse.analysis.repository;

import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.astAnalyzer.*;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

/**
 * @author John Ericksen
 */
public class InjectionNodeBuilderRepositoryProvider implements Provider<InjectionNodeBuilderRepository> {

    private final ASTClassFactory astClassFactory;
    private final AOPProxyAnalyzer aopProxyAnalyzer;
    private final InjectionAnalyzer injectionAnalyzer;
    private final ListenerAnalysis methodCallbackAnalysis;
    private final ScopeAnalysis scopeAnalysis;
    private final DeclareFieldAnalysis declareFieldAnalysis;
    private final ObservesAnalysis observesAnalysis;
    private final NonConfigurationAnalysis nonConfigurationAnalysis;
    private final AnnotationValidationAnalysis annotationValidationAnalysis;
    private final ManualSuperAnalysis manualSuperAnalysis;

    @Inject
    public InjectionNodeBuilderRepositoryProvider(ASTClassFactory astClassFactory, AOPProxyAnalyzer aopProxyAnalyzer, InjectionAnalyzer injectionAnalyzer, ListenerAnalysis methodCallbackAnalysis, ScopeAnalysis scopeAnalysis, DeclareFieldAnalysis declareFieldAnalysis, ObservesAnalysis observesAnalysis, NonConfigurationAnalysis nonConfigurationAnalysis, AnnotationValidationAnalysis annotationValidationAnalysis, ManualSuperAnalysis manualSuperAnalysis) {
        this.astClassFactory = astClassFactory;
        this.aopProxyAnalyzer = aopProxyAnalyzer;
        this.injectionAnalyzer = injectionAnalyzer;
        this.methodCallbackAnalysis = methodCallbackAnalysis;
        this.scopeAnalysis = scopeAnalysis;
        this.declareFieldAnalysis = declareFieldAnalysis;
        this.observesAnalysis = observesAnalysis;
        this.nonConfigurationAnalysis = nonConfigurationAnalysis;
        this.annotationValidationAnalysis = annotationValidationAnalysis;
        this.manualSuperAnalysis = manualSuperAnalysis;
    }

    @Override
    public InjectionNodeBuilderRepository get() {
        Set<ASTAnalysis> analysisRepository = new HashSet<ASTAnalysis>();

        analysisRepository.add(aopProxyAnalyzer);
        analysisRepository.add(injectionAnalyzer);
        analysisRepository.add(methodCallbackAnalysis);
        analysisRepository.add(scopeAnalysis);
        analysisRepository.add(declareFieldAnalysis);
        analysisRepository.add(observesAnalysis);
        analysisRepository.add(nonConfigurationAnalysis);
        analysisRepository.add(annotationValidationAnalysis);
        analysisRepository.add(manualSuperAnalysis);

        return new InjectionNodeBuilderRepository(analysisRepository, astClassFactory);
    }
}

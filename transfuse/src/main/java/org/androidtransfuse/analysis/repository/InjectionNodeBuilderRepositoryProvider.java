/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

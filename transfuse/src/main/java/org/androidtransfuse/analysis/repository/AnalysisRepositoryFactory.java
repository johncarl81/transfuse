/**
 * Copyright 2012 John Ericksen
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

import org.androidtransfuse.analysis.astAnalyzer.*;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisRepositoryFactory implements Provider<AnalysisRepository> {

    private final AOPProxyAnalyzer aopProxyAnalyzer;
    private final InjectionAnalyzer injectionAnalyzer;
    private final ListenerAnalysis methodCallbackAnalysis;
    private final ScopeAnalysis scopeAnalysis;
    private final RegistrationAnalyzer registrationAnalysis;
    private final DeclareFieldAnalysis declareFieldAnalysis;
    private final ObservesAnalysis observesAnalysis;
    private final NonConfigurationAnalysis nonConfigurationAnalysis;

    @Inject
    public AnalysisRepositoryFactory(AOPProxyAnalyzer aopProxyAnalyzer,
                                     InjectionAnalyzer injectionAnalyzer,
                                     ListenerAnalysis methodCallbackAnalysis,
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

/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.gen;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.TransactionWorker;
import org.androidtransfuse.processor.WorkerProvider;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGenerationFactory {

    @Inject
    private Provider<ComponentGenerator> componentGeneratorProvider;

    public WorkerProvider buildAnalysisGenerationProvider(Provider<? extends Analysis<ComponentDescriptor>> analysis) {
        return new AnalysisGenerationProvider(analysis, componentGeneratorProvider);
    }

    private static final class AnalysisGenerationProvider implements WorkerProvider {

        private Provider<? extends Analysis<ComponentDescriptor>> analysis;
        private Provider<ComponentGenerator> generator;

        private AnalysisGenerationProvider(Provider<? extends Analysis<ComponentDescriptor>> analysis, Provider<ComponentGenerator> generator) {
            this.analysis = analysis;
            this.generator = generator;
        }

        @Override
        public TransactionWorker<Provider<ASTType>, JDefinedClass> get() {
            return new AnalysisGeneration(analysis, generator);
        }
    }
}

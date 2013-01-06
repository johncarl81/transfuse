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
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.Analysis;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.processor.AbstractCompletionTransactionWorker;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class AnalysisGeneration extends AbstractCompletionTransactionWorker<Provider<ASTType>, JDefinedClass> {

    private final Provider<? extends Analysis<ComponentDescriptor>> analysis;
    private final Provider<ComponentGenerator> generatorProvider;

    public AnalysisGeneration(Provider<? extends Analysis<ComponentDescriptor>> analysis,
                              Provider<ComponentGenerator> generatorProvider) {
        this.analysis = analysis;
        this.generatorProvider = generatorProvider;
    }

    @Override
    public JDefinedClass innerRun(Provider<ASTType> astTypeProvider) {

        ASTType astType = astTypeProvider.get();

        ComponentDescriptor descriptor = analysis.get().analyze(astType);

        return generatorProvider.get().generate(descriptor);
    }
}

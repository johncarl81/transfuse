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
package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.FactoriesGenerator;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class FactoriesTransactionWorker extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private final FactoriesGenerator factoriesGenerator;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public FactoriesTransactionWorker(FactoriesGenerator factoriesGenerator,
                                      InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                                      VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.factoriesGenerator = factoriesGenerator;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> aggregate) {

        //register factory configuration
        for (Provider<ASTType> typeProvider : aggregate.keySet()) {
            ASTType type = typeProvider.get();

            injectionNodeBuilders.putModuleConfig(type, variableInjectionBuilderFactory.buildFactoryNodeBuilder(type));
        }


        //setup Factories class
        factoriesGenerator.generateFactories(aggregate);

        return null;
    }
}

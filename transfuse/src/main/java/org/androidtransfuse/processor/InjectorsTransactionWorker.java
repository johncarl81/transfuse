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
package org.androidtransfuse.processor;

import com.sun.codemodel.JDefinedClass;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.InjectorsGenerator;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectorsTransactionWorker extends AbstractCompletionTransactionWorker<Map<Provider<ASTType>, JDefinedClass>, Void> {

    private final InjectorsGenerator injectorsGenerator;
    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public InjectorsTransactionWorker(InjectorsGenerator injectorsGenerator,
                                      InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                                      VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.injectorsGenerator = injectorsGenerator;
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public Void innerRun(Map<Provider<ASTType>, JDefinedClass> aggregate) {

        //setup Injectors class
        injectorsGenerator.generateInjectors(aggregate);

        //register injector configuration
        for (Provider<ASTType> typeProvider : aggregate.keySet()) {
            ASTType type = typeProvider.get();

            injectionNodeBuilders.putModuleConfig(Matchers.type(type).build(),
                    variableInjectionBuilderFactory.buildInjectorNodeBuilder(type));
        }
        return null;
    }
}

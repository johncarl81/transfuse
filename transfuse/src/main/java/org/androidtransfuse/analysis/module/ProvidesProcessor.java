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
package org.androidtransfuse.analysis.module;

import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProvidesProcessor implements MethodProcessor {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ProvidesProcessor(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                             VariableInjectionBuilderFactory variableInjectionBuilderFactory) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    public ModuleConfiguration process(ASTType moduleType, ASTMethod astMethod, ASTAnnotation astAnnotation) {

        ASTType astType = astAnnotation.getProperty("value", ASTType.class);

        return new ProvidesModuleConfiguration(moduleType, astMethod, astType);
    }

    private final class ProvidesModuleConfiguration implements ModuleConfiguration {

        private final ASTType moduleType;
        private final ASTMethod astMethod;
        private final ASTType to;

        private ProvidesModuleConfiguration(ASTType moduleType, ASTMethod astMethod, ASTType to) {
            this.moduleType = moduleType;
            this.astMethod = astMethod;
            this.to = to;
        }

        @Override
        public void setConfiguration() {
            injectionNodeBuilders.putModuleConfig(astMethod.getReturnType(),
                    variableInjectionBuilderFactory.buildProvidesInjectionNodeBuilder(moduleType, astMethod));
        }
    }
}

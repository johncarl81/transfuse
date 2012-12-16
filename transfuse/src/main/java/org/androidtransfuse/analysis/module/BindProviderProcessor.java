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
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepositoryFactory;
import org.androidtransfuse.analysis.repository.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;

import javax.inject.Inject;

public class BindProviderProcessor implements TypeProcessor {

    private final InjectionNodeBuilderRepositoryFactory injectionNodeBuilders;
    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;

    @Inject
    public BindProviderProcessor(InjectionNodeBuilderRepositoryFactory injectionNodeBuilders,
                                 VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation bindProvider) {
        ASTType type = bindProvider.getProperty("type", ASTType.class);
        ASTType provider = bindProvider.getProperty("provider", ASTType.class);

        return new ProviderConfiguration(type,  provider);
    }

    private final class ProviderConfiguration implements ModuleConfiguration {

        private final ASTType type;
        private final ASTType provider;

        private ProviderConfiguration(ASTType type, ASTType provider) {
            this.type = type;
            this.provider = provider;
        }

        @Override
        public void setConfiguration() {
            injectionNodeBuilders.putModuleConfig(type,
                    variableInjectionBuilderFactory.buildProviderInjectionNodeBuilder(provider));

            providerInjectionNodeBuilderRepository.addProvider(type,
                    variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(provider));
        }
    }
}
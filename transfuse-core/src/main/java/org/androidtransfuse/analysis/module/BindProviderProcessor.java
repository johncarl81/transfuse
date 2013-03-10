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
package org.androidtransfuse.analysis.module;

import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.analysis.repository.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.ProviderInjectionNodeBuilderFactory;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * Configured the given @BindProvider annotation parameters as Provider bindings.
 *
 * @author John Ericksen
 */
public class BindProviderProcessor implements TypeProcessor {

    private final ModuleRepository injectionNodeBuilders;
    private final ProviderInjectionNodeBuilderFactory variableInjectionBuilderFactory;
    private final ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;

    @Inject
    public BindProviderProcessor(ModuleRepository injectionNodeBuilders,
                                 ProviderInjectionNodeBuilderFactory variableInjectionBuilderFactory,
                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
    }

    @Override
    public ModuleConfiguration process(ASTAnnotation bindProvider) {
        ASTType type = bindProvider.getProperty("type", ASTType.class);
        ASTType provider = bindProvider.getProperty("provider", ASTType.class);
        ASTType scope = bindProvider.getProperty("scope", ASTType.class);
        ASTAnnotation named = bindProvider.getProperty("named", ASTAnnotation.class);

        return new ProviderConfiguration(type,  provider, scope, named);
    }

    private final class ProviderConfiguration implements ModuleConfiguration {

        private final ASTType type;
        private final ASTType provider;
        private final ASTType scope;
        private final ASTAnnotation named;

        private ProviderConfiguration(ASTType type, ASTType provider, ASTType scope, ASTAnnotation named) {
            this.type = type;
            this.provider = provider;
            this.scope = scope;
            this.named = named;
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            if(named == null){
                configurationRepository.putType(type, variableInjectionBuilderFactory.builderProviderBuilder(provider));
            }
            else{
                configurationRepository.putType(new InjectionSignature(type, ImmutableSet.of(named)),
                        variableInjectionBuilderFactory.builderProviderBuilder(provider));
            }

            providerInjectionNodeBuilderRepository.addProvider(type, provider);

            if(scope != null) {
                injectionNodeBuilders.putScoped(scope, type);
            }
        }
    }
}
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

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.ProviderInjectionNodeBuilderFactory;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * Configured the given @BindProvider annotation parameters as Provider bindings.
 *
 * @author John Ericksen
 */
public class BindProviderProcessor implements TypeProcessor {

    private final ProviderInjectionNodeBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public BindProviderProcessor(ProviderInjectionNodeBuilderFactory variableInjectionBuilderFactory) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public ModuleConfiguration process(ASTType moduleType, ASTAnnotation bindProvider) {
        ASTType type = bindProvider.getProperty("type", ASTType.class);
        ASTType provider = bindProvider.getProperty("provider", ASTType.class);
        ASTType scope = bindProvider.getProperty("scope", ASTType.class);

        return new ProviderConfiguration(type,  provider, scope);
    }

    private final class ProviderConfiguration implements ModuleConfiguration {

        private final ASTType type;
        private final ASTType provider;
        private final ASTType scope;

        private ProviderConfiguration(ASTType type, ASTType provider, ASTType scope) {
            this.type = type;
            this.provider = provider;
            this.scope = scope;
        }

        @Override
        public void setConfiguration(InjectionNodeBuilderRepository configurationRepository) {
            configurationRepository.putType(type, variableInjectionBuilderFactory.builderProviderBuilder(provider));

            if(scope != null) {
                configurationRepository.putScoped(new InjectionSignature(type), scope);
            }
        }
    }
}
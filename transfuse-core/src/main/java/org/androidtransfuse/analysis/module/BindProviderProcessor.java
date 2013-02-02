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
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.ProviderInjectionNodeBuilderFactory;
import org.androidtransfuse.gen.variableBuilder.VariableASTImplementationFactory;
import org.androidtransfuse.util.matcher.Matchers;

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
    private final VariableASTImplementationFactory variableASTImplementationFactory;
    private final ASTClassFactory astClassFactory;

    @Inject
    public BindProviderProcessor(ModuleRepository injectionNodeBuilders,
                                 ProviderInjectionNodeBuilderFactory variableInjectionBuilderFactory,
                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository,
                                 VariableASTImplementationFactory variableASTImplementationFactory,
                                 ASTClassFactory astClassFactory) {
        this.injectionNodeBuilders = injectionNodeBuilders;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
        this.variableASTImplementationFactory = variableASTImplementationFactory;
        this.astClassFactory = astClassFactory;
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
        public void setConfiguration() {
            if(named == null){
                injectionNodeBuilders.putModuleConfig(Matchers.type(type).build(),
                        variableInjectionBuilderFactory.builderProviderBuilder(provider));

                /*ASTType providerType = new ASTGenericTypeWrapper(astClassFactory.getType(Provider.class), new LazyTypeParameterBuilder() {
                    @Override
                    public List<ASTType> buildGenericParameters() {
                        return Collections.singletonList(type);
                    }
                });

                injectionNodeBuilders.putModuleConfig(Matchers.type(providerType).build(),
                        variableASTImplementationFactory.buildVariableASTBuilder(provider));*/
            }
            else{
                injectionNodeBuilders.putInjectionSignatureConfig(Matchers.type(type).annotated().byAnnotation(named).build(),
                        variableInjectionBuilderFactory.builderProviderBuilder(provider));

                /*ASTType providerType = new ASTGenericTypeWrapper(astClassFactory.getType(Provider.class), new LazyTypeParameterBuilder() {
                    @Override
                    public List<ASTType> buildGenericParameters() {
                        return Collections.singletonList(type);
                    }
                });

                injectionNodeBuilders.putInjectionSignatureConfig(Matchers.type(providerType).annotated().byAnnotation(named).build(),
                        variableASTImplementationFactory.buildVariableASTBuilder(provider));*/
            }

            providerInjectionNodeBuilderRepository.addProvider(type, provider);

            if(scope != null) {
                injectionNodeBuilders.putScoped(scope, type);
            }
        }
    }
}
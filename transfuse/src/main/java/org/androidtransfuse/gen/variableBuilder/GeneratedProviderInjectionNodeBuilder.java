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
package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.repository.ProviderInjectionNodeBuilderRepository;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class GeneratedProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private final VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private final ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository;
    private final Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    private final InjectionPointFactory injectionPointFactory;
    private final Analyzer analyzer;

    @Inject
    public GeneratedProviderInjectionNodeBuilder(VariableInjectionBuilderFactory variableInjectionBuilderFactory,
                                                 ProviderInjectionNodeBuilderRepository providerInjectionNodeBuilderRepository,
                                                 Provider<VariableInjectionBuilder> variableInjectionBuilderProvider,
                                                 InjectionPointFactory injectionPointFactory,
                                                 Analyzer analyzer) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.providerInjectionNodeBuilderRepository = providerInjectionNodeBuilderRepository;
        this.variableInjectionBuilderProvider = variableInjectionBuilderProvider;
        this.injectionPointFactory = injectionPointFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTType providerGenericType = getProviderTemplateType(astType);

        if (providerInjectionNodeBuilderRepository.isProviderDefined(providerGenericType)) {
            //already defined
            InjectionNodeBuilder providerInjectionNodeBuilder = providerInjectionNodeBuilderRepository.getProvider(providerGenericType);

            return providerInjectionNodeBuilder.buildInjectionNode(astType, context, annotations);
        }

        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);
        InjectionNode providerInjectionNode = injectionPointFactory.buildInjectionNode(providerGenericType, context);
        providerInjectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildGeneratedProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }

    private ASTType getProviderTemplateType(ASTType astType) {
        return astType.getGenericParameters().get(0);
    }
}

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
package org.androidtransfuse.gen.variableDecorator;

import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Collection;

/**
 * @author John Ericksen
 */
public class GeneratedProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private final GeneratedProviderBuilderFactory variableInjectionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;
    private final Analyzer analyzer;

    @Inject
    public GeneratedProviderInjectionNodeBuilder(GeneratedProviderBuilderFactory variableInjectionBuilderFactory,
                                                 InjectionPointFactory injectionPointFactory,
                                                 Analyzer analyzer) {
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, Collection<ASTAnnotation> annotations) {

        ASTType providerGenericType = getProviderTemplateType(astType);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);
        InjectionNode providerInjectionNode = injectionPointFactory.buildInjectionNode(annotations, providerGenericType, context.addDependent(injectionNode));

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }

    private ASTType getProviderTemplateType(ASTType astType) {
        return astType.getGenericParameters().get(0);
    }
}

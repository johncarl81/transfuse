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

import org.androidtransfuse.adapter.ASTBase;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderInjectionNodeBuilder implements InjectionNodeBuilder {

    private final ASTType providerType;
    private final Analyzer analyzer;
    private final ProviderVariableBuilderFactory variableInjectionBuilderFactory;

    @Inject
    public ProviderInjectionNodeBuilder(/*@Assisted*/ ASTType providerType,
                                        Analyzer analyzer,
                                        ProviderVariableBuilderFactory variableInjectionBuilderFactory) {
        this.providerType = providerType;
        this.analyzer = analyzer;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTBase target, InjectionSignature signature, AnalysisContext context) {
        InjectionNode injectionNode = analyzer.analyze(signature, context);

        InjectionSignature providerSignature = new InjectionSignature(providerType);
        InjectionNode providerInjectionNode = analyzer.analyze(providerSignature, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildProviderVariableBuilder(providerInjectionNode));

        return injectionNode;
    }
}

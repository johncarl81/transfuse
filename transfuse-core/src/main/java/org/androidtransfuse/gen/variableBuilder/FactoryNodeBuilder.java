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

import com.google.common.collect.ImmutableSet;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class FactoryNodeBuilder implements InjectionNodeBuilder {

    private final ASTType factoryType;
    private final VariableFactoryBuilderFactory2 variableInjectionBuilderFactory;
    private final Analyzer analyzer;

    @Inject
    public FactoryNodeBuilder(@Assisted ASTType factoryType,
                              VariableFactoryBuilderFactory2 variableInjectionBuilderFactory, Analyzer analyzer) {
        this.factoryType = factoryType;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.analyzer = analyzer;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ImmutableSet<ASTAnnotation> annotations) {
        InjectionNode injectionNode = analyzer.analyze(astType, astType, context);

        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderFactory.buildFactoryVariableBuilder(factoryType));

        return injectionNode;
    }
}

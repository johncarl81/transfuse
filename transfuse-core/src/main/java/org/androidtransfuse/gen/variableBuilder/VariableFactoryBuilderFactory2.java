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

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.Analyzer;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableFactoryBuilderFactory2 {

    private final TypedExpressionFactory typedExpressionFactory;
    private final ClassGenerationUtil generationUtil;
    private final Analyzer analyzer;

    @Inject
    public VariableFactoryBuilderFactory2(TypedExpressionFactory typedExpressionFactory,
                                          ClassGenerationUtil generationUtil,
                                          Analyzer analyzer) {
        this.typedExpressionFactory = typedExpressionFactory;
        this.generationUtil = generationUtil;
        this.analyzer = analyzer;
    }

    public FactoryVariableBuilder buildFactoryVariableBuilder(ASTType factoryType) {
        return new FactoryVariableBuilder(factoryType, typedExpressionFactory, generationUtil);
    }

    public IndependentInjectionNodeBuilder buildInjectionNodeBuilder(ExpressionVariableBuilderWrapper wrapper) {
        return new IndependentInjectionNodeBuilder(wrapper, analyzer);
    }

    public ExpressionVariableBuilderWrapper buildExpressionWrapper(TypedExpression typedExpression) {
        return new ExpressionVariableBuilderWrapper(typedExpression);
    }
}

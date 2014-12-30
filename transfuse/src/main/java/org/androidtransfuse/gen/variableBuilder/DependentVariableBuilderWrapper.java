/**
 * Copyright 2011-2015 John Ericksen
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

import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class DependentVariableBuilderWrapper extends ConsistentTypeVariableBuilder {

    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final InjectionNode dependency;
    private final DependentVariableBuilder dependentVariableBuilder;

    @Inject
    public DependentVariableBuilderWrapper(/*@Assisted*/ InjectionNode dependency,
                                           /*@Assisted*/ DependentVariableBuilder dependentVariableBuilder,
                                           /*@Assisted*/ ASTType type,
                                           TypedExpressionFactory typedExpressionFactory,
                                           InjectionExpressionBuilder injectionExpressionBuilder) {
        super(type, typedExpressionFactory);
        this.dependency = dependency;
        this.dependentVariableBuilder = dependentVariableBuilder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        TypedExpression dependentExpression = injectionExpressionBuilder.buildVariable(context, dependency);

        return dependentVariableBuilder.buildVariable(dependentExpression.getExpression());
    }
}

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

import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTJDefinedClassType;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProvidesVariableBuilder extends ConsistentTypeVariableBuilder {

    private final InjectionNode module;
    private final ASTMethod method;
    private final Map<ASTParameter, InjectionNode> dependencyAnalysis;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ProvidesVariableBuilder(/*@Assisted*/ InjectionNode module,
                                   /*@Assisted*/ ASTMethod method,
                                   /*@Assisted*/ Map<ASTParameter, InjectionNode> dependencyAnalysis,
                                   InjectionExpressionBuilder injectionExpressionBuilder,
                                   TypedExpressionFactory typedExpressionFactory,
                                   InvocationBuilder invocationBuilder) {
        super(method.getReturnType(), typedExpressionFactory);
        this.module = module;
        this.method = method;
        this.dependencyAnalysis = dependencyAnalysis;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        JExpression moduleVar = injectionExpressionBuilder.buildVariable(context, module).getExpression();

        List<JExpression> paramExpressions = new ArrayList<JExpression>();

        for (ASTParameter parameter : method.getParameters()) {
            JExpression paramExpression = injectionExpressionBuilder.buildVariable(context, dependencyAnalysis.get(parameter)).getExpression();
            paramExpressions.add(paramExpression);
        }

        return invocationBuilder.buildMethodCall(
                new ASTJDefinedClassType(context.getDefinedClass()),
                injectionNode.getASTType(),
                method,
                paramExpressions,
                new TypedExpression(module.getASTType(), moduleVar));
    }
}

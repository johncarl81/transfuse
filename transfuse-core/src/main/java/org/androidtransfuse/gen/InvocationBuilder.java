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
package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.ModifierInjectionBuilder;
import org.androidtransfuse.model.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds the invocations of constructors, methods and field get/set.
 *
 * @author John Ericksen
 */
public class InvocationBuilder {

    private final InvocationBuilderStrategy invocationBuilderStrategy;

    @Inject
    public InvocationBuilder(InvocationBuilderStrategy invocationBuilderStrategy) {
        this.invocationBuilderStrategy = invocationBuilderStrategy;
    }

    private ModifierInjectionBuilder getInjectionBuilder(ASTAccessModifier modifier) {
        return invocationBuilderStrategy.getInjectionBuilder(modifier);
    }

    public JInvocation buildMethodCall(ASTType returnType, MethodInjectionPoint methodInjectionPoint, Iterable<? extends JExpression> parameters, JExpression variable) {
        return buildMethodCall(methodInjectionPoint.getAccessModifier(), returnType, methodInjectionPoint.getName(), parameters, pullASTTypes(methodInjectionPoint.getInjectionNodes()), methodInjectionPoint.getContainingType(), variable);
    }

    public JInvocation buildMethodCall(ASTAccessModifier accessModifier, ASTType returnType, String name, Iterable<? extends JExpression> parameters, List<ASTType> parameterTypes, ASTType targetExpressionType, JExpression targetExpression) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);

        return injectionBuilder.buildMethodCall(returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
    }

    private List<ASTType> pullASTTypes(List<InjectionNode> injectionNodes) {
        List<ASTType> astTypes = new ArrayList<ASTType>();

        for (InjectionNode injectionNode : injectionNodes) {
            astTypes.add(injectionNode.getASTType());
        }

        return astTypes;
    }

    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        return buildFieldSet(
                fieldInjectionPoint.getAccessModifier(),
                fieldInjectionPoint.getContainingType(),
                variable,
                fieldInjectionPoint.getInjectionNode().getASTType(),
                fieldInjectionPoint.getName(),
                expression.getType(),
                expression.getExpression());
    }

    public JStatement buildFieldSet(ASTAccessModifier accessModifier, ASTType containingType, JExpression containingExpression, ASTType fieldType, String name, ASTType expressionType, JExpression expression) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);
        return injectionBuilder.buildFieldSet(expressionType, expression, containingType, fieldType, name, containingExpression);
    }

    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name, ASTAccessModifier accessModifier) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);
        return injectionBuilder.buildFieldGet(returnType, variableType, variable, name);
    }

    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, ASTType type) {
        List<ASTType> parameterTypes = new ArrayList<ASTType>();
        for (InjectionNode injectionNode : constructorInjectionPoint.getInjectionNodes()) {
            parameterTypes.add(injectionNode.getASTType());
        }

        return buildConstructorCall(constructorInjectionPoint.getAccessModifier(), parameterTypes, parameters, type);
    }

    public JExpression buildConstructorCall(ASTAccessModifier accessModifier, List<ASTType> parameterTypes, Iterable<JExpression> parameters, ASTType type) {
        ModifierInjectionBuilder injectionBuilder = getInjectionBuilder(accessModifier);
        return injectionBuilder.buildConstructorCall(type, parameterTypes, parameters);
    }
}

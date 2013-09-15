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

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTAccessModifier;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.invocationBuilder.DefaultInvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.PrivateInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.ProtectedInjectionBuilder;
import org.androidtransfuse.gen.invocationBuilder.PublicInjectionBuilder;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.InstanceProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class InvocationBuilderTest {

    private PublicInjectionBuilder publicInjectionBuilder;
    private ProtectedInjectionBuilder protectedInjectionBuilder;
    private PrivateInjectionBuilder privateInjectionBuilder;
    private InvocationBuilder invocationBuilder;

    @Before
    public void setup() {

        publicInjectionBuilder = mock(PublicInjectionBuilder.class);
        protectedInjectionBuilder = mock(ProtectedInjectionBuilder.class);
        privateInjectionBuilder = mock(PrivateInjectionBuilder.class);

        invocationBuilder = new InvocationBuilder(new DefaultInvocationBuilderStrategy(
                new InstanceProvider<PublicInjectionBuilder>(publicInjectionBuilder),
                new InstanceProvider<ProtectedInjectionBuilder>(protectedInjectionBuilder),
                new InstanceProvider<PrivateInjectionBuilder>(privateInjectionBuilder)
        ));
    }

    @Test
    public void testFieldGet() {
        ASTType returnType = mock(ASTType.class);
        ASTType variableType = mock(ASTType.class);
        JExpression variable = mock(JExpression.class);
        String name = "test";

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PUBLIC);
        verify(publicInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PROTECTED);
        verify(protectedInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PACKAGE_PRIVATE);
        verify(protectedInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, returnType, variable, variableType);

        invocationBuilder.buildFieldGet(returnType, variableType, variable, name, ASTAccessModifier.PRIVATE);
        verify(privateInjectionBuilder).buildFieldGet(returnType, variableType, variable, name);
    }

    @Test
    public void testFieldSet() throws ClassNotFoundException, JClassAlreadyExistsException {
        ASTType variableType = mock(ASTType.class);
        JExpression variable = mock(JExpression.class);
        ASTType expressionType = mock(ASTType.class);
        JExpression expression = mock(JExpression.class);
        InjectionNode injectionNode = mock(InjectionNode.class);
        String name = "test";
        TypedExpression typedExpression = new TypedExpression(expressionType, expression);

        FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PUBLIC, name, injectionNode);
        invocationBuilder.buildFieldSet(typedExpression, fieldInjectionPoint, variable);
        verify(publicInjectionBuilder).buildFieldSet(expressionType, expression, fieldInjectionPoint.getContainingType(), fieldInjectionPoint.getInjectionNode().getASTType(), fieldInjectionPoint.getName(), variable);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint protectedFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PROTECTED, name, injectionNode);
        invocationBuilder.buildFieldSet(typedExpression, protectedFieldInjectionPoint, variable);
        verify(protectedInjectionBuilder).buildFieldSet(expressionType, expression, protectedFieldInjectionPoint.getContainingType(), protectedFieldInjectionPoint.getInjectionNode().getASTType(), protectedFieldInjectionPoint.getName(), variable);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint packagePrivateFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PACKAGE_PRIVATE, name, injectionNode);
        invocationBuilder.buildFieldSet(typedExpression, packagePrivateFieldInjectionPoint, variable);
        verify(protectedInjectionBuilder).buildFieldSet(expressionType, expression, packagePrivateFieldInjectionPoint.getContainingType(), packagePrivateFieldInjectionPoint.getInjectionNode().getASTType(), packagePrivateFieldInjectionPoint.getName(), variable);
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, variable, variableType);

        FieldInjectionPoint privateFieldInjectionPoint = new FieldInjectionPoint(variableType, ASTAccessModifier.PRIVATE, name, injectionNode);
        invocationBuilder.buildFieldSet(typedExpression, privateFieldInjectionPoint, variable);
        verify(privateInjectionBuilder).buildFieldSet(expressionType, expression, privateFieldInjectionPoint.getContainingType(), privateFieldInjectionPoint.getInjectionNode().getASTType(), privateFieldInjectionPoint.getName(), variable);
    }

    @Test
    public void testConstructorCall() throws ClassNotFoundException, JClassAlreadyExistsException {
        ASTType type = mock(ASTType.class);
        ASTType containingType = mock(ASTType.class);
        JExpression variable = mock(JExpression.class);
        List parameters = mock(List.class);

        ConstructorInjectionPoint publicConstructorInjectionPoint = new ConstructorInjectionPoint(containingType, ASTAccessModifier.PUBLIC);
        invocationBuilder.buildConstructorCall(publicConstructorInjectionPoint, parameters, type);
        verify(publicInjectionBuilder).buildConstructorCall(eq(type), any(List.class), eq(parameters));
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parameters, variable);

        ConstructorInjectionPoint protectedConstructorInjectionPoint = new ConstructorInjectionPoint(containingType, ASTAccessModifier.PROTECTED);
        invocationBuilder.buildConstructorCall(protectedConstructorInjectionPoint, parameters, type);
        verify(protectedInjectionBuilder).buildConstructorCall(eq(type), any(List.class), eq(parameters));
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parameters, variable);

        ConstructorInjectionPoint packagePrivateConstructorInjectionPoint = new ConstructorInjectionPoint(containingType, ASTAccessModifier.PACKAGE_PRIVATE);
        invocationBuilder.buildConstructorCall(packagePrivateConstructorInjectionPoint, parameters, type);
        verify(protectedInjectionBuilder).buildConstructorCall(eq(type), any(List.class), eq(parameters));
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parameters, variable);

        ConstructorInjectionPoint privateConstructorInjectionPoint = new ConstructorInjectionPoint(containingType, ASTAccessModifier.PRIVATE);
        invocationBuilder.buildConstructorCall(privateConstructorInjectionPoint, parameters, type);
        verify(privateInjectionBuilder).buildConstructorCall(eq(type), any(List.class), eq(parameters));
        reset(publicInjectionBuilder, protectedInjectionBuilder, privateInjectionBuilder, parameters, variable);
    }

    @Test
    public void testMethodInvocation() {

        ASTType returnType = mock(ASTType.class);
        String name = "test";
        Iterable parameters = mock(Iterable.class);
        List<ASTType> parameterTypes = mock(List.class);
        ASTType targetExpressionType = mock(ASTType.class);
        JExpression targetExpression = mock(JExpression.class);

        invocationBuilder.buildMethodCall(ASTAccessModifier.PUBLIC, returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
        verify(publicInjectionBuilder).buildMethodCall(eq(returnType), eq(name), eq(parameters), eq(parameterTypes), eq(targetExpressionType), eq(targetExpression));
        reset(publicInjectionBuilder, returnType, parameters, parameterTypes, targetExpressionType, targetExpression);

        invocationBuilder.buildMethodCall(ASTAccessModifier.PROTECTED, returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
        verify(protectedInjectionBuilder).buildMethodCall(eq(returnType), eq(name), eq(parameters), eq(parameterTypes), eq(targetExpressionType), eq(targetExpression));
        reset(protectedInjectionBuilder, returnType, parameters, parameterTypes, targetExpressionType, targetExpression);

        invocationBuilder.buildMethodCall(ASTAccessModifier.PACKAGE_PRIVATE, returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
        verify(protectedInjectionBuilder).buildMethodCall(eq(returnType), eq(name), eq(parameters), eq(parameterTypes), eq(targetExpressionType), eq(targetExpression));
        reset(protectedInjectionBuilder, returnType, parameters, parameterTypes, targetExpressionType, targetExpression);

        invocationBuilder.buildMethodCall(ASTAccessModifier.PRIVATE, returnType, name, parameters, parameterTypes, targetExpressionType, targetExpression);
        verify(privateInjectionBuilder).buildMethodCall(eq(returnType), eq(name), eq(parameters), eq(parameterTypes), eq(targetExpressionType), eq(targetExpression));
        reset(privateInjectionBuilder, returnType, parameters, parameterTypes, targetExpressionType, targetExpression);
    }
}
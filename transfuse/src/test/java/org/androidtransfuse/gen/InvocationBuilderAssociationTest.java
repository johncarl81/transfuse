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
import org.androidtransfuse.NoOpMessager;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.gen.invocationBuilder.*;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.validation.Validator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class InvocationBuilderAssociationTest {

    private PublicInvocationBuilder publicInjectionBuilder;
    private ProtectedInvocationBuilder protectedInvocationBuilder;
    private PrivateInvocationBuilder privateInjectionBuilder;
    private InvocationBuilder invocationBuilder;
    private Map<ASTAccessModifier, ModifiedInvocationBuilder> modifierAssociationMap;
    private PackageClass packageClassOne;
    private PackageClass packageClassTwo;

    @Before
    public void setup() {

        publicInjectionBuilder = mock(PublicInvocationBuilder.class);
        protectedInvocationBuilder = mock(ProtectedInvocationBuilder.class);
        privateInjectionBuilder = mock(PrivateInvocationBuilder.class);
        Validator validator = new Validator("Test", new NoOpMessager());

        invocationBuilder = new InvocationBuilder(new DefaultInvocationBuilderStrategy(
                publicInjectionBuilder,
                protectedInvocationBuilder,
                privateInjectionBuilder,
                validator
        ));

        modifierAssociationMap = new EnumMap<ASTAccessModifier, ModifiedInvocationBuilder>(ASTAccessModifier.class);

        modifierAssociationMap.put(ASTAccessModifier.PUBLIC, publicInjectionBuilder);
        modifierAssociationMap.put(ASTAccessModifier.PROTECTED, protectedInvocationBuilder);
        modifierAssociationMap.put(ASTAccessModifier.PACKAGE_PRIVATE, protectedInvocationBuilder);
        modifierAssociationMap.put(ASTAccessModifier.PRIVATE, privateInjectionBuilder);

        packageClassOne = new PackageClass("org.test", "Tester");
        packageClassTwo = new PackageClass("org.androidtransfuse", "Tester");

        Assert.assertFalse(packageClassOne.getPackage().equals(packageClassTwo.getPackage()));
    }

    @Test
    public void testFieldGet() {
        ASTType userType = mock(ASTType.class);
        ASTType returnType = mock(ASTType.class);
        ASTField field = mock(ASTField.class);
        ASTType variableType = mock(ASTType.class);
        JExpression variable = mock(JExpression.class);
        String name = "test";
        TypedExpression target = new TypedExpression(returnType, variable);

        for (Map.Entry<ASTAccessModifier, ModifiedInvocationBuilder> modifierAssociationEntry : modifierAssociationMap.entrySet()) {
            when(field.getAccessModifier()).thenReturn(modifierAssociationEntry.getKey());
            when(field.getName()).thenReturn(name);
            when(userType.getPackageClass()).thenReturn(packageClassOne);
            when(returnType.getPackageClass()).thenReturn(packageClassTwo);
            invocationBuilder.buildFieldGet(userType, field, returnType, target);
            verify(modifierAssociationEntry.getValue()).buildFieldGet(false, field, target);
            reset(publicInjectionBuilder, protectedInvocationBuilder, privateInjectionBuilder, userType, returnType, variable, variableType);
        }
    }

    @Test
    public void testFieldSet() throws ClassNotFoundException, JClassAlreadyExistsException {
        ASTType userType = mock(ASTType.class);
        ASTType variableType = mock(ASTType.class);
        ASTField field = mock(ASTField.class);
        JExpression variable = mock(JExpression.class);
        ASTType expressionType = mock(ASTType.class);
        JExpression expression = mock(JExpression.class);
        InjectionNode injectionNode = mock(InjectionNode.class);
        TypedExpression typedExpression = new TypedExpression(expressionType, expression);
        String name = "test";

        for (Map.Entry<ASTAccessModifier, ModifiedInvocationBuilder> modifierAssociationEntry : modifierAssociationMap.entrySet()) {
            when(field.getAccessModifier()).thenReturn(modifierAssociationEntry.getKey());
            when(field.getName()).thenReturn(name);
            FieldInjectionPoint fieldInjectionPoint = new FieldInjectionPoint(variableType, field, injectionNode);
            when(userType.getPackageClass()).thenReturn(packageClassOne);
            when(expressionType.getPackageClass()).thenReturn(packageClassTwo);
            invocationBuilder.buildFieldSet(userType, typedExpression, fieldInjectionPoint, variable);
            verify(modifierAssociationEntry.getValue()).buildFieldSet(eq(false), eq(field), eq(typedExpression), any(TypedExpression.class));
            reset(publicInjectionBuilder, protectedInvocationBuilder, privateInjectionBuilder, userType, variable, variableType);
        }
    }

    @Test
    public void testConstructorCall() throws ClassNotFoundException, JClassAlreadyExistsException {
        ASTType userType = mock(ASTType.class);
        ASTType type = mock(ASTType.class);
        JExpression variable = mock(JExpression.class);
        List<JExpression> parameters = mock(List.class);
        ASTConstructor constructor =  mock(ASTConstructor.class);

        for (Map.Entry<ASTAccessModifier, ModifiedInvocationBuilder> modifierAssociationEntry : modifierAssociationMap.entrySet()) {
            when(constructor.getAccessModifier()).thenReturn(modifierAssociationEntry.getKey());
            when(userType.getPackageClass()).thenReturn(packageClassOne);
            when(type.getPackageClass()).thenReturn(packageClassTwo);
            invocationBuilder.buildConstructorCall(userType, constructor, type, parameters);
            verify(modifierAssociationEntry.getValue()).buildConstructorCall(eq(constructor), eq(type), eq(parameters));
            reset(publicInjectionBuilder, protectedInvocationBuilder, privateInjectionBuilder, parameters, variable);
        }
    }

    @Test
    public void testMethodInvocation() {

        ASTType userType = mock(ASTType.class);
        ASTType returnType = mock(ASTType.class);
        String name = "test";
        List<JExpression> parameters = mock(List.class);
        List<ASTType> parameterTypes = mock(List.class);
        ASTType targetExpressionType = mock(ASTType.class);
        JExpression targetExpression = mock(JExpression.class);
        ASTMethod method = mock(ASTMethod.class);
        TypedExpression expression = new TypedExpression(targetExpressionType, targetExpression);

        for (Map.Entry<ASTAccessModifier, ModifiedInvocationBuilder> modifierAssociationEntry : modifierAssociationMap.entrySet()) {
            when(method.getAccessModifier()).thenReturn(modifierAssociationEntry.getKey());
            when(method.getReturnType()).thenReturn(returnType);
            when(method.getName()).thenReturn(name);
            when(userType.getPackageClass()).thenReturn(packageClassOne);
            when(targetExpressionType.getPackageClass()).thenReturn(packageClassTwo);
            invocationBuilder.buildMethodCall(userType, method, parameters, expression);
            verify(modifierAssociationEntry.getValue()).buildMethodCall(eq(method), eq(parameters), eq(expression));
            reset(publicInjectionBuilder, protectedInvocationBuilder, privateInjectionBuilder, userType, returnType, parameters, parameterTypes, targetExpressionType, targetExpression);
        }
    }
}
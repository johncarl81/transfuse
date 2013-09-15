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
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;


/**
 * @author John Ericksen
 */
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {

    private final PackageHelperRepository packageHelperGenerator;
    private final ClassGenerationUtil generationUtil;
    private final TypeInvocationHelper invocationHelper;

    @Inject
    public ProtectedInjectionBuilder(PackageHelperRepository packageHelperGenerator, ClassGenerationUtil generationUtil, TypeInvocationHelper invocationHelper) {
        this.packageHelperGenerator = packageHelperGenerator;
        this.generationUtil = generationUtil;
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(ASTType type, List<ASTType> parameterTypes, Iterable<? extends JExpression> parameters) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getConstructorCall(type, parameterTypes);
        JInvocation invocation = accessorMethod.invoke(generationUtil);
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }


    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<? extends JExpression> parameters, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getMethodCall(returnType, targetExpressionType, methodName, injectionNodeType);
        JInvocation invocation = accessorMethod.invoke(generationUtil).arg(targetExpression);
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldGetter(returnType, variableType, name);
        return accessorMethod.invoke(generationUtil).arg(variable);
    }

    @Override
    public JStatement buildFieldSet(ASTType expressionType, JExpression expression, ASTType containingType, ASTType fieldType, String fieldName, JExpression variable) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldSetter(containingType, fieldType, fieldName);
        return accessorMethod.invoke(generationUtil)
                .arg(variable)
                .arg(invocationHelper.coerceType(fieldType, new TypedExpression(expressionType, expression)));
    }
}

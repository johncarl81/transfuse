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

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;


/**
 * @author John Ericksen
 */
public class ProtectedInjectionBuilder implements ModifierInjectionBuilder {
    ;

    private final PackageHelperRepository packageHelperGenerator;
    private final JCodeModel codeModel;
    private final TypeInvocationHelper invocationHelper;

    @Inject
    public ProtectedInjectionBuilder(PackageHelperRepository packageHelperGenerator, JCodeModel codeModel, TypeInvocationHelper invocationHelper) {
        this.packageHelperGenerator = packageHelperGenerator;
        this.codeModel = codeModel;
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(ConstructorInjectionPoint constructorInjectionPoint, Iterable<JExpression> parameters, JType type) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getConstructorCall(constructorInjectionPoint);
        JInvocation invocation = accessorMethod.invoke(codeModel);
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }


    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<JExpression> parameters, List<ASTType> injectionNodeType, ASTType targetExpressionType, JExpression targetExpression) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getMethodCall(returnType, targetExpressionType, methodName, injectionNodeType);
        JInvocation invocation = accessorMethod.invoke(codeModel).arg(targetExpression);
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldGetter(returnType, variableType, name);
        return accessorMethod.invoke(codeModel).arg(variable);
    }

    @Override
    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldSetter(fieldInjectionPoint);
        return accessorMethod.invoke(codeModel)
                .arg(variable)
                .arg(invocationHelper.coerceType(fieldInjectionPoint.getInjectionNode().getASTType(), expression));
    }
}

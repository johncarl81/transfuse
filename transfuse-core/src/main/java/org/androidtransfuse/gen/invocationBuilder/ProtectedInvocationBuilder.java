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

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;


/**
 * @author John Ericksen
 */
public class ProtectedInvocationBuilder implements ModifiedInvocationBuilder {

    private final PackageHelperRepository packageHelperGenerator;
    private final ClassGenerationUtil generationUtil;
    private final TypeInvocationHelper invocationHelper;

    @Inject
    public ProtectedInvocationBuilder(PackageHelperRepository packageHelperGenerator, ClassGenerationUtil generationUtil, TypeInvocationHelper invocationHelper) {
        this.packageHelperGenerator = packageHelperGenerator;
        this.generationUtil = generationUtil;
        this.invocationHelper = invocationHelper;
    }

    @Override
    public JExpression buildConstructorCall(ASTConstructor constructor, ASTType type, List<? extends JExpression> parameters) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getConstructorCall(type, constructor.getParameters());
        JInvocation invocation = accessorMethod.invoke(generationUtil);
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }


    @Override
    public JInvocation buildMethodCall(ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression) {

        List<ASTType> paramerTypes = FluentIterable.from(method.getParameters()).transform(new Function<ASTParameter, ASTType>() {
            public ASTType apply(ASTParameter parameter) {
                return parameter.getASTType();
            }
        }).toList();

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getMethodCall(method.getReturnType(), expression.getType(), method.getName(), paramerTypes);
        JInvocation invocation = accessorMethod.invoke(generationUtil).arg(expression.getExpression());
        for (JExpression parameter : parameters) {
            invocation.arg(parameter);
        }
        return invocation;
    }

    @Override
    public JExpression buildFieldGet(ASTField field, TypedExpression variable) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldGetter(field.getASTType(), variable.getType(), field.getName());
        return accessorMethod.invoke(generationUtil).arg(variable.getExpression());
    }

    @Override
    public JStatement buildFieldSet(ASTField field, TypedExpression expression, TypedExpression containingExpression) {

        ProtectedAccessorMethod accessorMethod = packageHelperGenerator.getFieldSetter(containingExpression.getType(), field.getASTType(), field.getName());
        return accessorMethod.invoke(generationUtil)
                .arg(containingExpression.getExpression())
                .arg(invocationHelper.coerceType(field.getASTType(), expression));
    }
}

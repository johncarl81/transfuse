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
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.List;

/**
 * Injection Builder for building publicly scoped elements.
 *
 * @author John Ericksen
 */
public class PublicInjectionBuilder implements ModifierInjectionBuilder {

    private final TypeInvocationHelper invocationHelper;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public PublicInjectionBuilder(TypeInvocationHelper invocationHelper, ClassGenerationUtil generationUtil) {
        this.invocationHelper = invocationHelper;
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildConstructorCall(ASTType type, List<ASTType> parameterTypes, Iterable<? extends JExpression> parameters) {
        JInvocation constructorInvocation = JExpr._new(generationUtil.ref(type));

        for (JExpression parameter : parameters) {
            constructorInvocation.arg(parameter);
        }

        return constructorInvocation;
    }

    @Override
    public JInvocation buildMethodCall(ASTType returnType, String methodName, Iterable<? extends JExpression> parameters, List<ASTType> types, ASTType targetExpressionType, JExpression targetExpression) {
        //public case:
        JInvocation methodInvocation = targetExpression.invoke(methodName);

        for (JExpression parameter : parameters) {
            methodInvocation.arg(parameter);
        }

        return methodInvocation;
    }

    @Override
    public JExpression buildFieldGet(ASTType returnType, ASTType variableType, JExpression variable, String name) {
        return variable.ref(name);
    }

    @Override
    public JStatement buildFieldSet(ASTType expressionType, JExpression expression, ASTType containingType, ASTType fieldType, String fieldName, JExpression variable) {
        JBlock assignmentBlock = new JBlock(false, false);

        assignmentBlock.assign(variable.ref(fieldName), invocationHelper.coerceType(fieldType, new TypedExpression(expressionType, expression)));

        return assignmentBlock;
    }
}

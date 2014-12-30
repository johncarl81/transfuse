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
package org.androidtransfuse.gen.invocationBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTPrimitiveType;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class TypeInvocationHelper {

    private ASTClassFactory astClassFactory;
    private ClassGenerationUtil generationUtil;

    @Inject
    public TypeInvocationHelper(ASTClassFactory astClassFactory, ClassGenerationUtil generationUtil) {
        this.astClassFactory = astClassFactory;
        this.generationUtil = generationUtil;
    }

    public <T> JExpression getExpression(ASTType type, Map<T, TypedExpression> expressionMap, T parameter) {

        if (parameter == null) {
            return JExpr._null();
        } else if (type != null) {
            return coerceType(type, expressionMap.get(parameter));
        } else {
            return expressionMap.get(parameter).getExpression();
        }
    }

    public JExpression coerceType(Class targetType, TypedExpression typedExpression) {
        return coerceType(astClassFactory.getType(targetType), typedExpression);
    }

    public JExpression coerceType(ASTType targetType, TypedExpression typedExpression) {
        if (typedExpression.getType().inheritsFrom(targetType)) {
            return typedExpression.getExpression();
        }
        if (targetType.inheritsFrom(typedExpression.getType())) {
            return JExpr.cast(generationUtil.ref(targetType), typedExpression.getExpression());
        }
        if (targetType instanceof ASTPrimitiveType) {
            ASTPrimitiveType primitiveTargetType = (ASTPrimitiveType) targetType;

            ASTType objectType = astClassFactory.getType(primitiveTargetType.getObjectClass());

            if (objectType.inheritsFrom(typedExpression.getType())) {
                return JExpr.cast(generationUtil.ref(objectType), typedExpression.getExpression());
            }
        }
        throw new TransfuseAnalysisException("Non-coercible types encountered");
    }
}

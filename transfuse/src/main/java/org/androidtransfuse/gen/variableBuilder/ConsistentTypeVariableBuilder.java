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
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public abstract class ConsistentTypeVariableBuilder implements VariableBuilder {

    private final TypedExpressionFactory typedExpressionFactory;
    private final Class clazz;
    private final ASTType astType;

    public ConsistentTypeVariableBuilder(Class clazz, TypedExpressionFactory typedExpressionFactory) {
        this.clazz = clazz;
        this.astType = null;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    public ConsistentTypeVariableBuilder(ASTType astType, TypedExpressionFactory typedExpressionFactory) {
        this.clazz = null;
        this.astType = astType;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext context, InjectionNode injectionNode) {
        if(clazz != null){
            return typedExpressionFactory.build(clazz, buildExpression(context, injectionNode));
        }
        else{
            return typedExpressionFactory.build(astType, buildExpression(context, injectionNode));
        }
    }

    public abstract JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode);
}

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

import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.validation.Validator;

import java.util.List;

/**
 * @author John Ericksen
 */
public class WarningInvocationBuilderDecorator implements ModifiedInvocationBuilder {

    private final ModifiedInvocationBuilder delegate;
    private final Validator validator;

    public WarningInvocationBuilderDecorator(ModifiedInvocationBuilder delegate, Validator validator) {
        this.delegate = delegate;
        this.validator = validator;
    }

    @Override
    public JExpression buildConstructorCall(ASTConstructor constructor, ASTType type, List<? extends JExpression> parameters) {
        validator.warn(
                "Reflection is required to access private constructor: " + constructor + ", consider using non-private."
        ).element(constructor).build();

        return delegate.buildConstructorCall(constructor, type, parameters);
    }

    @Override
    public JExpression buildFieldGet(boolean cast, ASTField field, TypedExpression targetExpression) {
        validator.warn(
                "Reflection is required to access private field: " + field + ", consider using non-private."
        ).element(field).build();

        return delegate.buildFieldGet(cast, field, targetExpression);
    }

    @Override
    public JStatement buildFieldSet(boolean cast, ASTField field, TypedExpression expression, TypedExpression containingExpression) {
        StringBuilder builder = new StringBuilder();
        builder.append("Reflection is required to modify ");
        boolean privateModifier = field.getAccessModifier().equals(ASTAccessModifier.PRIVATE);
        boolean finalModifier = field.isFinal();
        if(privateModifier){
            builder.append("private ");
        }
        if(finalModifier){
            builder.append("final ");
        }
        builder.append( "field: " + field + ", consider using ");
        if(privateModifier){
            builder.append("non-private");
        }
        if(finalModifier){
            if(privateModifier){
                builder.append(" and ");
            }
            builder.append("non-final");
        }
        builder.append(".");

        validator.warn(builder.toString()).element(field).build();

        return delegate.buildFieldSet(cast, field, expression, containingExpression);
    }

    @Override
    public JInvocation buildMethodCall(boolean cast, ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression) {
        validator.warn(
                "Reflection is required to call private method: " + method + ", consider using non-private."
        ).element(method).build();

        return delegate.buildMethodCall(cast, method, parameters, expression);
    }
}

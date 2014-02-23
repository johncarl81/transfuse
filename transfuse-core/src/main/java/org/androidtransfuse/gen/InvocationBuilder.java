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
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.gen.invocationBuilder.InvocationBuilderStrategy;
import org.androidtransfuse.gen.invocationBuilder.ModifiedInvocationBuilder;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
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

    public JInvocation buildMethodCall(ASTMethod method, List<? extends JExpression> parameters, TypedExpression expression) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(method.getAccessModifier());

        return injectionBuilder.buildMethodCall(method, parameters, expression);
    }

    public JStatement buildFieldSet(TypedExpression expression, FieldInjectionPoint fieldInjectionPoint, JExpression variable) {
        return buildFieldSet(fieldInjectionPoint.getField(), new TypedExpression(fieldInjectionPoint.getContainingType(), variable), expression);
    }

    public JStatement buildFieldSet(ASTField field, TypedExpression containingExpression, TypedExpression expression) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(field.getAccessModifier());
        return injectionBuilder.buildFieldSet(field, expression, containingExpression);
    }

    public JExpression buildFieldGet(ASTField field, TypedExpression targetExpression) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(field.getAccessModifier());
        return injectionBuilder.buildFieldGet(field, targetExpression);
    }

    public JExpression buildConstructorCall(ASTConstructor constructor, ASTType type, List<JExpression> parameters) {
        ModifiedInvocationBuilder injectionBuilder = getInjectionBuilder(constructor.getAccessModifier());

        return injectionBuilder.buildConstructorCall(constructor, type, parameters);
    }

    private ModifiedInvocationBuilder getInjectionBuilder(ASTAccessModifier modifier) {
        return invocationBuilderStrategy.getInjectionBuilder(modifier);
    }
}

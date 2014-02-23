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
package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTField;
import org.androidtransfuse.adapter.ASTJDefinedClassType;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ViewFieldRegistrationInvocationBuilderImpl implements ViewRegistrationInvocationBuilder{

    private final InvocationBuilder invocationBuilder;
    private final ASTField astField;

    @Inject
    public ViewFieldRegistrationInvocationBuilderImpl(/*@Assisted*/ ASTField astField, InvocationBuilder invocationBuilder) {
        this.invocationBuilder = invocationBuilder;
        this.astField = astField;
    }

    @Override
    public void buildInvocation(JDefinedClass definedClass, JBlock block, TypedExpression expression, JExpression viewExpression, String method, InjectionNode injectionNode) {
        block.invoke(viewExpression, method)
                .arg(invocationBuilder.buildFieldGet(
                        new ASTJDefinedClassType(definedClass),
                        astField,
                        expression.getType(),
                        expression
                ));
    }
}

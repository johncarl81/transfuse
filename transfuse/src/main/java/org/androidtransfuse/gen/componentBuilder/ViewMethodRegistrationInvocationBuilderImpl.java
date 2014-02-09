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
import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author John Ericksen
 */
public class ViewMethodRegistrationInvocationBuilderImpl implements ViewRegistrationInvocationBuilder {

    private final InvocationBuilder invocationBuilder;
    private final ASTMethod getterMethod;

    @Inject
    public ViewMethodRegistrationInvocationBuilderImpl(/*@Assisted*/ ASTMethod getterMethod, InvocationBuilder invocationBuilder) {
        this.invocationBuilder = invocationBuilder;
        this.getterMethod = getterMethod;
    }

    @Override
    public void buildInvocation(JBlock block, TypedExpression expression, JExpression viewExpression, String method, InjectionNode injectionNode) {

        block.invoke(viewExpression, method)
                .arg(invocationBuilder.buildMethodCall(
                        getterMethod,
                        Collections.EMPTY_LIST,
                        expression));
    }
}

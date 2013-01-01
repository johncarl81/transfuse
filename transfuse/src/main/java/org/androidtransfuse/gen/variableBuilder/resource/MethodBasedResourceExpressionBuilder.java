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
package org.androidtransfuse.gen.variableBuilder.resource;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class MethodBasedResourceExpressionBuilder implements ResourceExpressionBuilder {

    private final Class returnType;
    private final String accessMethod;
    private final InjectionNode resourcesInjectionNode;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public MethodBasedResourceExpressionBuilder(@Assisted Class returnType,
                                                @Assisted String accessMethod,
                                                @Assisted InjectionNode resourcesInjectionNode,
                                                InjectionExpressionBuilder injectionExpressionBuilder,
                                                TypedExpressionFactory typedExpressionFactory) {
        this.returnType = returnType;
        this.accessMethod = accessMethod;
        this.resourcesInjectionNode = resourcesInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        TypedExpression resourcesVar = injectionExpressionBuilder.buildVariable(context, resourcesInjectionNode);

        JInvocation expression = resourcesVar.getExpression().invoke(accessMethod).arg(resourceIdExpr);

        return typedExpressionFactory.build(returnType, expression);
    }
}
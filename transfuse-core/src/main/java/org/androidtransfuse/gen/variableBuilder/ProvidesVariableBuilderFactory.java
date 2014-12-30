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
package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ProvidesVariableBuilderFactory {

    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ProvidesVariableBuilderFactory(InjectionExpressionBuilder injectionExpressionBuilder, TypedExpressionFactory typedExpressionFactory, InvocationBuilder invocationBuilder) {
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
        this.invocationBuilder = invocationBuilder;
    }

    public ProvidesVariableBuilder buildProvidesVariableBuilder(InjectionNode module, ASTMethod method, Map<ASTParameter, InjectionNode> dependencyAnalysis){
        return new ProvidesVariableBuilder(module, method, dependencyAnalysis, injectionExpressionBuilder, typedExpressionFactory, invocationBuilder);
    }
}

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

import com.sun.codemodel.JExpression;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.Scopes;

/**
 * @author John Ericksen
 */
public class ScopeReferenceVariableBuilder extends ConsistentTypeVariableBuilder {

    private final ASTType scopeAnnotation;
    private final ClassGenerationUtil generationUtil;

    public ScopeReferenceVariableBuilder(ASTType scopeAnnotation,
                                         TypedExpressionFactory typedExpressionFactory,
                                         ClassGenerationUtil generationUtil) {
        super(Scope.class, typedExpressionFactory);
        this.scopeAnnotation = scopeAnnotation;
        this.generationUtil = generationUtil;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        JExpression scopesVar = context.getScopeVar();
        return scopesVar.invoke(Scopes.GET_SCOPE).arg(generationUtil.ref(scopeAnnotation).dotclass());
    }
}

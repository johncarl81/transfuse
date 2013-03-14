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
package org.androidtransfuse.gen.scopeBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.scope.Scopes;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
public class SingletonScopeBuilder implements VariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final JCodeModel codeModel;
    private final TypedExpressionFactory typedExpressionFactory;
    private final UniqueVariableNamer namer;

    @Inject
    public SingletonScopeBuilder(JCodeModel codeModel,
                                 ProviderGenerator providerGenerator,
                                 TypedExpressionFactory typedExpressionFactory,
                                 UniqueVariableNamer namer) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.typedExpressionFactory = typedExpressionFactory;
        this.namer = namer;
    }

    public TypedExpression buildVariable(InjectionBuilderContext context, InjectionNode injectionNode) {

        //build provider
        JDefinedClass providerClass = providerGenerator.generateProvider(injectionNode, true);
        JExpression provider = JExpr._new(providerClass).arg(context.getScopeVar());

        //build scope call
        JClass injectionNodeClassRef = codeModel.ref(injectionNode.getClassName());
        JExpression scopesVar = context.getScopeVar();
        JExpression scopeVar = scopesVar.invoke(Scopes.GET_SCOPE).arg(codeModel.ref(Singleton.class).dotclass());

        JInvocation scopeKey = JExpr._new(codeModel.ref(ScopeKey.class).narrow(injectionNodeClassRef)).arg(JExpr.lit(injectionNode.getClassName()));

        JExpression expression = scopeVar.invoke(Scope.GET_SCOPED_OBJECT).arg(scopeKey).arg(provider);

        JVar decl = context.getBlock().decl(codeModel.ref(injectionNode.getClassName()),
                namer.generateName(injectionNode), expression);

        return typedExpressionFactory.build(injectionNode.getASTType(), decl);
    }
}

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

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.ContextScopeHolder;
import org.androidtransfuse.scope.Scope;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ContextScopeVariableBuilder implements VariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final JCodeModel codeModel;
    private final TypedExpressionFactory typedExpressionFactory;
    private final InjectionNode contextScopeHolder;
    private final InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ContextScopeVariableBuilder(/*@Assisted*/ InjectionNode contextScopeHolder,
                                       JCodeModel codeModel,
                                       ProviderGenerator providerGenerator,
                                       TypedExpressionFactory typedExpressionFactory,
                                       InjectionExpressionBuilder injectionExpressionBuilder) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.typedExpressionFactory = typedExpressionFactory;
        this.contextScopeHolder = contextScopeHolder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        //build provider
        JDefinedClass providerClass = providerGenerator.generateProvider(injectionNode, true);
        JExpression provider = JExpr._new(providerClass).arg(injectionBuilderContext.getScopeVar());

        //build scope call
        // <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        TypedExpression contextScopeHolderExpression = injectionExpressionBuilder.buildVariable(injectionBuilderContext, this.contextScopeHolder);
        JExpression injectionNodeClassRef = codeModel.ref(injectionNode.getClassName()).dotclass();
        //todo:coerce type?
        JExpression cast = JExpr.cast(codeModel.ref(ContextScopeHolder.class), contextScopeHolderExpression.getExpression());
        JExpression scopeVar = cast.invoke(ContextScopeHolder.GET_SCOPE);

        JExpression expression = scopeVar.invoke(Scope.GET_SCOPED_OBJECT).arg(injectionNodeClassRef).arg(provider);

        return typedExpressionFactory.build(injectionNode.getASTType(), expression);
    }
}

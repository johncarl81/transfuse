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
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.SingletonScope;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SingletonScopeBuilder implements VariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final JCodeModel codeModel;
    private final TypedExpressionFactory typedExpressionFactory;

    @Inject
    public SingletonScopeBuilder(JCodeModel codeModel,
                                 ProviderGenerator providerGenerator, TypedExpressionFactory typedExpressionFactory) {
        this.codeModel = codeModel;
        this.providerGenerator = providerGenerator;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        //build provider
        JDefinedClass providerClass = providerGenerator.generateProvider(injectionNode, true);
        JExpression provider = JExpr._new(providerClass);

        //build scope call
        // <T> T getScopedObject(Class<T> clazz, Provider<T> provider);
        JExpression injectionNodeClassRef = codeModel.ref(injectionNode.getClassName()).dotclass();
        JExpression scopeVar = codeModel.ref(SingletonScope.class).staticInvoke(SingletonScope.GET_INSTANCE);

        JExpression expression = scopeVar.invoke(Scope.GET_SCOPED_OBJECT).arg(injectionNodeClassRef).arg(provider);

        return typedExpressionFactory.build(injectionNode.getASTType(), expression);
    }
}

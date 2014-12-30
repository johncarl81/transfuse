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
package org.androidtransfuse.gen.variableDecorator;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InstantiationStrategy;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.variableBuilder.ConsistentTypeVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratedProviderVariableBuilder extends ConsistentTypeVariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final InjectionNode providerTypeInjectionNode;

    @Inject
    public GeneratedProviderVariableBuilder(/*@Assisted*/ InjectionNode providerTypeInjectionNode,
                                            ProviderGenerator providerGenerator,
                                            TypedExpressionFactory typedExpressionFactory) {
        super(Provider.class, typedExpressionFactory);
        this.providerGenerator = providerGenerator;
        this.providerTypeInjectionNode = providerTypeInjectionNode;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        final JDefinedClass providerClass = providerGenerator.generateProvider(providerTypeInjectionNode, false);

        return injectionBuilderContext.instantiateOnce(providerClass, providerClass, new InstantiationStrategy.ExpressionBuilder() {
            @Override
            public JExpression build(JBlock constructorBlock, JExpression scopesVar) {
                return JExpr._new(providerClass).arg(scopesVar);
            }
        });
    }
}

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
package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.ProviderGenerator;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.gen.variableBuilder.ConsistentTypeVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class GeneratedProviderVariableBuilder extends ConsistentTypeVariableBuilder {

    private final ProviderGenerator providerGenerator;
    private final UniqueVariableNamer variableNamer;
    private final InjectionNode providerTypeInjectionNode;

    @Inject
    public GeneratedProviderVariableBuilder(@Assisted InjectionNode providerTypeInjectionNode,
                                            ProviderGenerator providerGenerator,
                                            UniqueVariableNamer variableNamer,
                                            TypedExpressionFactory typedExpressionFactory) {
        super(Provider.class, typedExpressionFactory);
        this.providerGenerator = providerGenerator;
        this.variableNamer = variableNamer;
        this.providerTypeInjectionNode = providerTypeInjectionNode;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JDefinedClass providerClass = generateProviderType(providerTypeInjectionNode);

        return injectionBuilderContext.getBlock().decl(providerClass, variableNamer.generateName(providerClass),
                JExpr._new(providerClass).arg(injectionBuilderContext.getScopeVar()));
    }

    private JDefinedClass generateProviderType(InjectionNode providerTypeInjectionNode) {

        return providerGenerator.generateProvider(providerTypeInjectionNode, false);
    }
}

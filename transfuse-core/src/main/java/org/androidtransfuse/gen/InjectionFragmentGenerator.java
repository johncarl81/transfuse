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
package org.androidtransfuse.gen;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.gen.proxy.VirtualProxyGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionFragmentGenerator {

    private final InjectionBuilderContextFactory injectionBuilderContextFactory;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final VirtualProxyGenerator virtualProxyGenerator;

    @Inject
    public InjectionFragmentGenerator(InjectionBuilderContextFactory injectionBuilderContextFactory,
                                      InjectionExpressionBuilder injectionExpressionBuilder,
                                      VirtualProxyGenerator virtualProxyGenerator) {
        this.injectionBuilderContextFactory = injectionBuilderContextFactory;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.virtualProxyGenerator = virtualProxyGenerator;
    }

    public Map<InjectionNode, TypedExpression> buildFragment(JBlock block, JBlock constructorBlock, JDefinedClass definedClass, InjectionNode injectionNode, JExpression scopeVar) throws ClassNotFoundException, JClassAlreadyExistsException {
        return buildFragment(block, constructorBlock, definedClass, injectionNode, scopeVar, new HashMap<InjectionNode, TypedExpression>());
    }

    public Map<InjectionNode, TypedExpression> buildFragment(JBlock block, JBlock constructorBlock, JDefinedClass definedClass, InjectionNode injectionNode, JExpression scopeVar, Map<InjectionNode, TypedExpression> expressionMap) throws ClassNotFoundException, JClassAlreadyExistsException {

        InjectionBuilderContext injectionBuilderContext = injectionBuilderContextFactory.buildContext(block, constructorBlock, definedClass, scopeVar, expressionMap);

        injectionExpressionBuilder.buildVariable(injectionBuilderContext, injectionNode);

        //loop over remaining injection chains, building cyclic graph
        while(!injectionBuilderContext.getProxyLoad().isEmpty()){
            Map<InjectionNode, TypedExpression> proxied = new HashMap<InjectionNode, TypedExpression>(injectionBuilderContext.getProxyLoad());

            injectionBuilderContext.getProxyLoad().clear();
            for (Map.Entry<InjectionNode, TypedExpression> node : proxied.entrySet()) {
                injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, node.getKey());

                VirtualProxyAspect aspect = node.getKey().getAspect(VirtualProxyAspect.class);
                aspect.setProxyDefined(true);

                injectionBuilderContext.getVariableMap().remove(node.getKey());
                TypedExpression delegateVariable = injectionExpressionBuilder.buildVariable(injectionBuilderContext, node.getKey());

                //init proxy
                virtualProxyGenerator.initializeProxy(injectionBuilderContext, node.getValue(), delegateVariable);
            }
        }

        return injectionBuilderContext.getVariableMap();
    }
}

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

import com.sun.codemodel.*;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.layout.LayoutHandlerDelegate;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LayoutHandlerBuilder implements LayoutBuilder {

    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final InjectionNode layoutHandlerInjectionNode;
    private final Logger logger;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public LayoutHandlerBuilder(InjectionFragmentGenerator injectionFragmentGenerator,
                                InstantiationStrategyFactory instantiationStrategyFactory,
                                /*@Assisted*/ InjectionNode layoutHandlerInjectionNode,
                                Logger logger,
                                UniqueVariableNamer namer,
                                ClassGenerationUtil generationUtil) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.layoutHandlerInjectionNode = layoutHandlerInjectionNode;
        this.logger = logger;
        this.namer = namer;
        this.generationUtil = generationUtil;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {

        try {
            // Scopes instance
            JClass scopesRef = generationUtil.ref(Scopes.class);
            JInvocation scopesBuildInvocation = generationUtil.ref(ScopesGenerator.TRANSFUSE_SCOPES_UTIL).staticInvoke(ScopesGenerator.GET_INSTANCE);
            JVar scopesVar = block.decl(scopesRef, namer.generateName(Scopes.class), scopesBuildInvocation);

            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block,
                    instantiationStrategyFactory.buildMethodStrategy(block, scopesVar),
                    definedClass,
                    layoutHandlerInjectionNode,
                    scopesVar);

            //LayoutHandlerDelegate.invokeLayout()
            JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode).getExpression();

            block.add(layoutHandlerDelegate.invoke(LayoutHandlerDelegate.INVOKE_LAYOUT_METHOD));

        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while trying to generate LayoutHandler", e);
        }
    }
}

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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.layout.LayoutHandlerDelegate;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.Logger;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class LayoutHandlerBuilder implements LayoutBuilder {

    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InjectionNode layoutHandlerInjectionNode;
    private final Logger logger;

    @Inject
    public LayoutHandlerBuilder(InjectionFragmentGenerator injectionFragmentGenerator,
                                /*@Assisted*/ InjectionNode layoutHandlerInjectionNode,
                                Logger logger) {
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.layoutHandlerInjectionNode = layoutHandlerInjectionNode;
        this.logger = logger;
    }

    @Override
    public void buildLayoutCall(JDefinedClass definedClass, JBlock block) {

        try {
            //todo: wire in scopes
            Map<InjectionNode, TypedExpression> expressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, layoutHandlerInjectionNode, null);

            //LayoutHandlerDelegate.invokeLayout()
            JExpression layoutHandlerDelegate = expressionMap.get(layoutHandlerInjectionNode).getExpression();

            block.add(layoutHandlerDelegate.invoke(LayoutHandlerDelegate.INVOKE_LAYOUT_METHOD));

        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException while trying to generate LayoutHandler", e);
        } catch (JClassAlreadyExistsException e) {
            logger.error("JClassAlreadyExistsException while trying to generate LayoutHandler", e);
        }
    }
}

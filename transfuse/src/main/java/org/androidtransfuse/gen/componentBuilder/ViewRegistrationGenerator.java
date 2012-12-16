/**
 * Copyright 2012 John Ericksen
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

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ViewRegistrationGenerator implements RegistrationGenerator {

    private final InjectionNode viewInjectionNode;
    private final String method;
    private final InjectionNode injectionNode;
    private final ViewRegistrationInvocationBuilder viewRegistrationInvocationBuilder;
    private final InjectionFragmentGenerator injectionFragmentGenerator;

    @Inject
    public ViewRegistrationGenerator(@Assisted("viewInjectionNode") InjectionNode viewInjectionNode,
                                     @Assisted String method,
                                     @Assisted("targetInjectionNode") InjectionNode injectionNode,
                                     @Assisted ViewRegistrationInvocationBuilder viewRegistrationInvocationBuilder,
                                     InjectionFragmentGenerator injectionFragmentGenerator) {
        this.viewInjectionNode = viewInjectionNode;
        this.method = method;
        this.injectionNode = injectionNode;
        this.viewRegistrationInvocationBuilder = viewRegistrationInvocationBuilder;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
    }

    @Override
    public void build(JDefinedClass definedClass, JBlock block, TypedExpression value) {
        try{

            Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block, definedClass, viewInjectionNode);

            JExpression viewExpression = viewExpressionMap.get(viewInjectionNode).getExpression();

            viewRegistrationInvocationBuilder.buildInvocation(block, value, viewExpression, method, injectionNode);

        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Class not found", e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Class already exists", e);
        }
    }
}

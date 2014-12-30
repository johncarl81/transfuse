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
package org.androidtransfuse.gen.componentBuilder;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.experiment.ComponentBuilder;
import org.androidtransfuse.experiment.ComponentMethodGenerator;
import org.androidtransfuse.experiment.GenerationPhase;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.InstantiationStrategyFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Named;
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
    private final InstantiationStrategyFactory instantiationStrategyFactory;

    @Inject
    public ViewRegistrationGenerator(/*@Assisted("viewInjectionNode")*/ @Named("viewInjectionNode") InjectionNode viewInjectionNode,
                                     /*@Assisted*/ String method,
                                     /*@Assisted("targetInjectionNode")*/ @Named("targetInjectionNode") InjectionNode injectionNode,
                                     /*@Assisted*/ ViewRegistrationInvocationBuilder viewRegistrationInvocationBuilder,
                                     InjectionFragmentGenerator injectionFragmentGenerator,
                                     InstantiationStrategyFactory instantiationStrategyFactory) {
        this.viewInjectionNode = viewInjectionNode;
        this.method = method;
        this.injectionNode = injectionNode;
        this.viewRegistrationInvocationBuilder = viewRegistrationInvocationBuilder;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
    }

    @Override
    public void build(final ComponentBuilder componentBuilder, ASTMethod creationMethod, final TypedExpression value) {

        componentBuilder.add(creationMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                try {
                    //todo: map scopes
                    Map<InjectionNode, TypedExpression> viewExpressionMap = injectionFragmentGenerator.buildFragment(block,
                            instantiationStrategyFactory.buildMethodStrategy(block, null), componentBuilder.getDefinedClass(), viewInjectionNode, null);

                    JExpression viewExpression = viewExpressionMap.get(viewInjectionNode).getExpression();

                    viewRegistrationInvocationBuilder.buildInvocation(componentBuilder.getDefinedClass(), block, value, viewExpression, method, injectionNode);

                } catch (JClassAlreadyExistsException e) {
                    throw new TransfuseAnalysisException("Class already exists", e);
                }
            }
        });
    }
}

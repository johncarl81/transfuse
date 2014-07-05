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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JStatement;
import org.androidtransfuse.adapter.ASTJDefinedClassType;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.*;

public class MethodCallbackGenerator implements PostInjectionGeneration {

    private final ASTType eventAnnotation;
    private final InvocationBuilder invocationBuilder;
    private final ASTMethod eventMethod;

    @Inject
    public MethodCallbackGenerator(/*@Assisted*/ ASTType eventAnnotation, /*@Assisted*/ ASTMethod eventMethod, InvocationBuilder invocationBuilder) {
        this.eventAnnotation = eventAnnotation;
        this.invocationBuilder = invocationBuilder;
        this.eventMethod = eventMethod;
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor, Map<InjectionNode, TypedExpression> expressionMap) {

        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);
            final TypedExpression eventReceiverExpression = injectionNodeJExpressionEntry.getValue();

            if (methodCallbackAspect != null && methodCallbackAspect.contains(eventAnnotation)) {
                Set<ASTMethod> methods = methodCallbackAspect.getListeners(eventAnnotation);

                for (final ASTMethod methodCallback : methods) {

                    builder.add(eventMethod, GenerationPhase.EVENT, new ComponentMethodGenerator() {
                        @Override
                        public void generate(MethodDescriptor methodDescriptor, JBlock block) {

                            List<ASTParameter> matchedParameters = matchMethodArguments(methodDescriptor.getASTMethod().getParameters(), methodCallback);
                            List<JExpression> matchedExpressions = new ArrayList<JExpression>();

                            for (ASTParameter matchedParameter : matchedParameters) {
                                matchedExpressions.add(methodDescriptor.getParameters().get(matchedParameter).getExpression());
                            }

                            JStatement methodCall = invocationBuilder.buildMethodCall(
                                    new ASTJDefinedClassType(builder.getDefinedClass()),
                                    new ASTJDefinedClassType(builder.getDefinedClass()),
                                    methodCallback,
                                    matchedExpressions,
                                    eventReceiverExpression
                            );

                            block.add(methodCall);
                        }
                    });
                }
            }
        }
    }

    private List<ASTParameter> matchMethodArguments(List<ASTParameter> parametersToMatch, ASTMethod methodToCall) {
        List<ASTParameter> arguments = new ArrayList<ASTParameter>();

        List<ASTParameter> overrideParameters = new ArrayList<ASTParameter>(parametersToMatch);

        for (ASTParameter callParameter : methodToCall.getParameters()) {
            Iterator<ASTParameter> overrideParameterIterator = overrideParameters.iterator();

            while (overrideParameterIterator.hasNext()) {
                ASTParameter overrideParameter = overrideParameterIterator.next();
                if (overrideParameter.getASTType().equals(callParameter.getASTType())) {
                    arguments.add(overrideParameter);
                    overrideParameterIterator.remove();
                    break;
                }
            }
        }

        return arguments;
    }
}
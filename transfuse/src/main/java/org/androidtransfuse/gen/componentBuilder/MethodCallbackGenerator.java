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
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class MethodCallbackGenerator implements ExpressionVariableDependentGenerator {

    private final ASTType eventAnnotation;
    private final MethodGenerator methodGenerator;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public MethodCallbackGenerator(/*@Assisted*/ ASTType eventAnnotation, /*@Assisted*/ MethodGenerator methodGenerator, InvocationBuilder invocationBuilder) {
        this.eventAnnotation = eventAnnotation;
        this.methodGenerator = methodGenerator;
        this.invocationBuilder = invocationBuilder;
    }

    public void generate(JDefinedClass definedClass, MethodDescriptor creationMethodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, JExpression scopesExpression) {

        MethodDescriptor methodDescriptor = null;
        for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : expressionMap.entrySet()) {
            ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);

            if (methodCallbackAspect != null && methodCallbackAspect.contains(eventAnnotation)) {
                Set<ASTMethod> methods = methodCallbackAspect.getListeners(eventAnnotation);

                //define method on demand for possible lazy init
                if (methodDescriptor == null) {
                    methodDescriptor = methodGenerator.buildMethod(definedClass);
                }
                JBlock body = methodDescriptor.getMethod().body();

                for (ASTMethod methodCallback : methods) {

                    List<ASTParameter> matchedParameters = matchMethodArguments(methodDescriptor.getASTMethod().getParameters(), methodCallback);
                    List<ASTType> parameterTypes = new ArrayList<ASTType>();
                    List<JExpression> parameters = new ArrayList<JExpression>();

                    for (ASTParameter matchedParameter : matchedParameters) {
                        parameterTypes.add(matchedParameter.getASTType());
                        parameters.add(methodDescriptor.getParameters().get(matchedParameter).getExpression());
                    }

                    JInvocation methodCall = invocationBuilder.buildMethodCall(
                            methodCallback.getAccessModifier(),
                            methodDescriptor.getASTMethod().getReturnType(),
                            methodCallback.getName(),
                            parameters,
                            parameterTypes,
                            injectionNodeJExpressionEntry.getValue().getType(),
                            injectionNodeJExpressionEntry.getValue().getExpression()
                    );

                    if(ASTVoidType.VOID.equals(methodCallback.getReturnType())){
                      body.add(methodCall);
                    } else {
                      body._return(methodCall);
                    }
                }
            }
        }

        methodGenerator.closeMethod(methodDescriptor);
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

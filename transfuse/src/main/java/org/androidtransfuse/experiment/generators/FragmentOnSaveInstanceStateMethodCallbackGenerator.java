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
package org.androidtransfuse.experiment.generators;

import com.sun.codemodel.*;
import org.androidtransfuse.adapter.ASTJDefinedClassType;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.element.ASTElementFactory;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.AndroidLiterals;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class FragmentOnSaveInstanceStateMethodCallbackGenerator implements Generation {

    private final ASTType eventAnnotation;
    private final InvocationBuilder invocationBuilder;
    private final ASTMethod eventMethod;
    private final ASTMethod creationMethod;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;
    private final ASTElementFactory astElementFactory;

    @Inject
    public FragmentOnSaveInstanceStateMethodCallbackGenerator(/*@Assisted*/ ASTType eventAnnotation,
                                                              /*@Assisted*/ @Named("eventMethod") ASTMethod eventMethod,
                                                              /*@Assisted */ @Named("creationMethod")
                                                              ASTMethod creationMethod,
                                                              InvocationBuilder invocationBuilder,
                                                              UniqueVariableNamer namer,
                                                              ClassGenerationUtil generationUtil,
                                                              ASTElementFactory astElementFactory) {
        this.eventAnnotation = eventAnnotation;
        this.invocationBuilder = invocationBuilder;
        this.eventMethod = eventMethod;
        this.creationMethod = creationMethod;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.astElementFactory = astElementFactory;
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        builder.add(creationMethod, GenerationPhase.POSTINJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                final Map<TypedExpression, Set<ASTMethod>> listenerAspects = new HashMap<TypedExpression, Set<ASTMethod>>();
                for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : builder.getExpressionMap().entrySet()) {
                    ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);
                    TypedExpression eventReceiverExpression = injectionNodeJExpressionEntry.getValue();

                    if (methodCallbackAspect != null && methodCallbackAspect.contains(eventAnnotation)) {
                        listenerAspects.put(eventReceiverExpression, methodCallbackAspect.getListeners(eventAnnotation));
                    }
                }

                if(!listenerAspects.isEmpty()){

                    final JVar bundleField = builder.getDefinedClass().field(JMod.PRIVATE, generationUtil.type(AndroidLiterals.BUNDLE), namer.generateName(AndroidLiterals.BUNDLE));
                    //build onCreate to save bundle
                    ASTMethod onCreateMethod = astElementFactory.findMethod(AndroidLiterals.FRAGMENT, "onCreate", AndroidLiterals.BUNDLE);
                    builder.add(onCreateMethod, GenerationPhase.INIT, new ComponentMethodGenerator() {
                        @Override
                        public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                            //super call
                            JExpression bundle = methodDescriptor.getExpression(AndroidLiterals.BUNDLE).getExpression();
                            methodDescriptor.getMethod().body().add(JExpr._super().invoke("onCreate").arg(bundle));
                            methodDescriptor.getMethod().body().assign(bundleField, bundle);
                        }
                    });

                    builder.add(eventMethod, GenerationPhase.EVENT, new ComponentMethodGenerator() {
                        @Override
                        public void generate(MethodDescriptor methodDescriptor, JBlock block) {

                            for(Map.Entry<TypedExpression, Set<ASTMethod>> methodCallbackEntry : listenerAspects.entrySet()) {
                                Set<ASTMethod> methods = methodCallbackEntry.getValue();
                                final TypedExpression eventReceiverExpression = methodCallbackEntry.getKey();

                                //bundle addAll
                                JExpression delegate = builder.getExpressionMap().get(descriptor.getRootInjectionNode()).getExpression();
                                JConditional nullCondition = block._if(delegate.eq(JExpr._null()));
                                nullCondition._then()._if(bundleField.ne(JExpr._null()))._then()
                                        .add(methodDescriptor.getParameters().values().iterator().next().getExpression().invoke("putAll").arg(bundleField));

                                JBlock body = nullCondition._else();

                                for (final ASTMethod methodCallback : methods) {

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

                                    body.add(methodCall);
                                }
                            }
                        }
                    });
                }

            }
        });
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
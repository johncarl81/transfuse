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

import com.google.common.collect.ImmutableList;
import com.sun.codemodel.*;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.ManualSuperGenerator;
import org.androidtransfuse.analysis.astAnalyzer.ListenerAspect;
import org.androidtransfuse.analysis.astAnalyzer.ManualSuperAspect;
import org.androidtransfuse.event.SuperCaller;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.ClassGenerationUtil;
import org.androidtransfuse.gen.InvocationBuilder;
import org.androidtransfuse.gen.UniqueVariableNamer;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class MethodCallbackGenerator implements Generation {

    private final ASTType eventAnnotation;
    private final InvocationBuilder invocationBuilder;
    private final ASTMethod eventMethod;
    private final ASTMethod creationMethod;
    private final ASTType superCallerType;
    private final JCodeModel codeModel;
    private final UniqueVariableNamer namer;
    private final ClassGenerationUtil generationUtil;

    @Inject
    public MethodCallbackGenerator(/*@Assisted*/ ASTType eventAnnotation, /*@Assisted*/ @Named("eventMethod") ASTMethod eventMethod, /*@Assisted */ @Named("creationMethod") ASTMethod creationMethod, InvocationBuilder invocationBuilder, ASTClassFactory astClassFactory, JCodeModel codeModel, UniqueVariableNamer namer, ClassGenerationUtil generationUtil) {
        this.eventAnnotation = eventAnnotation;
        this.invocationBuilder = invocationBuilder;
        this.eventMethod = eventMethod;
        this.creationMethod = creationMethod;
        this.codeModel = codeModel;
        this.namer = namer;
        this.generationUtil = generationUtil;
        this.superCallerType = astClassFactory.getType(SuperCaller.class);
    }

    @Override
    public String getName() {
        return "Method Callback Generator @" + eventAnnotation.getPackageClass().getClassName() + " -> " + eventMethod;
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {

        builder.add(creationMethod, GenerationPhase.POSTINJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                for (Map.Entry<InjectionNode, TypedExpression> injectionNodeJExpressionEntry : builder.getExpressionMap().entrySet()) {
                    ListenerAspect methodCallbackAspect = injectionNodeJExpressionEntry.getKey().getAspect(ListenerAspect.class);
                    final TypedExpression eventReceiverExpression = injectionNodeJExpressionEntry.getValue();

                    if (methodCallbackAspect != null && methodCallbackAspect.contains(eventAnnotation)) {
                        Set<ASTMethod> methods = methodCallbackAspect.getListeners(eventAnnotation);

                        for (final ASTMethod methodCallback : methods) {

                            final boolean containsSuperCaller = containsSuperCaller(methodCallback.getParameters());
                            if(containsSuperCaller){
                                if(!injectionNodeJExpressionEntry.getKey().containsAspect(ManualSuperAspect.class)){
                                    injectionNodeJExpressionEntry.getKey().addAspect(new ManualSuperAspect());
                                }

                                ManualSuperAspect manualSuperAspect = injectionNodeJExpressionEntry.getKey().getAspect(ManualSuperAspect.class);
                                manualSuperAspect.add(eventMethod);
                            }

                            builder.add(eventMethod, GenerationPhase.EVENT, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {

                                    Map<ASTType, Queue<JExpression>> methodArgumentExpressions = buildMethodArgumentExpressions(methodDescriptor);
                                    if(containsSuperCaller) {
                                        methodArgumentExpressions.put(superCallerType, new LinkedList<JExpression>(ImmutableList.of(buildSuperCaller(eventMethod))));
                                    }

                                    List<JExpression> matchedExpressions = matchMethodArguments(methodCallback.getParameters(), methodArgumentExpressions);

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
        });
    }

    private JExpression buildSuperCaller(ASTMethod eventMethod) {

        JDefinedClass superCallerClass = codeModel.anonymousClass(SuperCaller.class);

        JMethod callMethod = superCallerClass.method(JMod.PUBLIC, Object.class, SuperCaller.CALL_METHOD);
        JVar objectVarargs = callMethod.varParam(Object.class, namer.generateName(Object.class));

        JInvocation superInvocation = JExpr.ref(ManualSuperGenerator.SUPER_NAME).invoke(eventMethod.getName());

        for(int i = 0; i < eventMethod.getParameters().size(); i++){
            ASTParameter parameter = eventMethod.getParameters().get(i);
            superInvocation = superInvocation.arg(JExpr.cast(generationUtil.ref(parameter.getASTType()), objectVarargs.component(JExpr.lit(i))));
        }

        JBlock body = callMethod.body();
        if(eventMethod.getReturnType().equals(ASTVoidType.VOID)) {
            body.add(superInvocation);
            body._return(JExpr._null());
        }
        else{
            body._return(superInvocation);
        }

        return JExpr._new(superCallerClass);
    }

    private boolean containsSuperCaller(ImmutableList<ASTParameter> parameters) {

        for (ASTParameter parameter : parameters) {
            if(parameter.getASTType().equals(superCallerType)){
                return true;
            }
        }
        return false;
    }

    private List<JExpression> matchMethodArguments(List<ASTParameter> parametersToMatch, Map<ASTType, Queue<JExpression>> expressions) {
        List<JExpression> arguments = new ArrayList<JExpression>();

        for (ASTParameter callParameter : parametersToMatch) {
            ASTType type = callParameter.getASTType();
            if(expressions.containsKey(type) && !expressions.get(type).isEmpty()){
                arguments.add(expressions.get(type).remove());
            }
            else{
                //todo: validation error
            }
        }

        return arguments;
    }

    private Map<ASTType, Queue<JExpression>> buildMethodArgumentExpressions(MethodDescriptor method){
        Map<ASTType, Queue<JExpression>> argumentExpressions = new HashMap<ASTType, Queue<JExpression>>();
        for (ASTParameter parameter : method.getASTMethod().getParameters()) {
            ASTType type = parameter.getASTType();
            if(!argumentExpressions.containsKey(type)){
                argumentExpressions.put(type, new LinkedList<JExpression>());
            }

            argumentExpressions.get(type).add(method.getParameter(parameter).getExpression());
        }
        return argumentExpressions;
    }
}
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
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.ObservesAspect;
import org.androidtransfuse.event.EventObserver;
import org.androidtransfuse.event.EventTending;
import org.androidtransfuse.event.WeakObserver;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.model.ComponentDescriptor;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import java.util.*;

/**
 * @author John Ericksen
 */
public class ObservesRegistrationGenerator implements ExpressionVariableDependentGenerator {

    private static final String SUPER_REF = "super";

    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;
    private final ClassNamer classNamer;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final InvocationBuilder invocationBuilder;

    @Inject
    public ObservesRegistrationGenerator(JCodeModel codeModel,
                                         ClassGenerationUtil generationUtil,
                                         UniqueVariableNamer variableNamer,
                                         ClassNamer classNamer,
                                         InjectionFragmentGenerator injectionFragmentGenerator,
                                         InstantiationStrategyFactory instantiationStrategyFactory,
                                         InvocationBuilder invocationBuilder) {
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
        this.classNamer = classNamer;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.invocationBuilder = invocationBuilder;
    }

    @Override
    public void generate(JDefinedClass definedClass, MethodDescriptor methodDescriptor, Map<InjectionNode, TypedExpression> expressionMap, ComponentDescriptor descriptor, JExpression scopesExpression) {

        try {
            //mapping from event type -> observer
            InjectionNode tendingInjectionNode = getTendingInjectionNode(expressionMap);
            JBlock block = methodDescriptor.getMethod().body();
            Map<JClass, JVar> observerTuples = getObservers(definedClass, block, expressionMap);

            if (!observerTuples.isEmpty() && tendingInjectionNode != null) {
                //build observer tuple array and observer tending class
                TypedExpression tendingExpression = buildEventTending(block, definedClass, tendingInjectionNode, scopesExpression, expressionMap);

                for (Map.Entry<JClass, JVar> tupleEntry : observerTuples.entrySet()) {
                    block.invoke(tendingExpression.getExpression(), EventTending.ADD_OBSERVER_METHOD)
                            .arg(tupleEntry.getKey().dotclass())
                            .arg(tupleEntry.getValue());
                }
            }

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("Tried to generate a class that already exists", e);
        }
    }

    private Map<JClass, JVar> getObservers(JDefinedClass definedClass, JBlock block, Map<InjectionNode, TypedExpression> expressionMap) throws JClassAlreadyExistsException {
        Map<JClass, JVar> observerTuples = new HashMap<JClass, JVar>();

        for (Map.Entry<InjectionNode, TypedExpression> expressionEntry : expressionMap.entrySet()) {

            if (expressionEntry.getKey().containsAspect(ObservesAspect.class)) {
                ObservesAspect aspect = expressionEntry.getKey().getAspect(ObservesAspect.class);
                TypedExpression typedExpression = expressionEntry.getValue();


                for (ASTType event : aspect.getEvents()) {

                    //generate WeakObserver<E, T> (E = event, T = target injection node)
                    JClass eventRef = generationUtil.ref(event);
                    JClass targetRef = generationUtil.ref(typedExpression.getType());

                    JDefinedClass observerClass = definedClass._class(JMod.PROTECTED | JMod.STATIC | JMod.FINAL, classNamer.numberedClassName(typedExpression.getType()).build().getClassName());

                    //match default constructor public WeakObserver(T target){
                    JMethod constructor = observerClass.constructor(JMod.PUBLIC);
                    JVar constTargetParam = constructor.param(targetRef, variableNamer.generateName(targetRef));
                    constructor.body().invoke(SUPER_REF).arg(constTargetParam);

                    observerClass._extends(
                            generationUtil.ref(WeakObserver.class)
                                    .narrow(eventRef)
                                    .narrow(targetRef));


                    JMethod triggerMethod = observerClass.method(JMod.PUBLIC, codeModel.VOID, EventObserver.TRIGGER);
                    triggerMethod.annotate(Override.class);
                    JVar eventParam = triggerMethod.param(eventRef, variableNamer.generateName(event));
                    JVar targetParam = triggerMethod.param(targetRef, variableNamer.generateName(typedExpression.getType()));
                    JBlock triggerBody = triggerMethod.body();

                    Set<JExpression> parameters = new HashSet<JExpression>();
                    parameters.add(eventParam);

                    for (ASTMethod observerMethod : aspect.getObserverMethods(event)) {
                        triggerBody.add(invocationBuilder.buildMethodCall(
                                observerMethod.getAccessModifier(),
                                ASTVoidType.VOID,
                                observerMethod.getName(),
                                parameters,
                                Collections.singletonList(event),
                                typedExpression.getType(),
                                targetParam));
                    }

                    JVar observer = block.decl(observerClass, variableNamer.generateName(EventObserver.class), JExpr._new(observerClass).arg(typedExpression.getExpression()));

                    observerTuples.put(eventRef, observer);
                }
            }
        }

        return observerTuples;
    }

    private InjectionNode getTendingInjectionNode(Map<InjectionNode, TypedExpression> expressionMap) {
        for (Map.Entry<InjectionNode, TypedExpression> expressionEntry : expressionMap.entrySet()) {

            if (expressionEntry.getKey().containsAspect(ObservesAspect.class)) {
                ObservesAspect aspect = expressionEntry.getKey().getAspect(ObservesAspect.class);
                return aspect.getObserverTendingInjectionNode();
            }
        }
        return null;
    }

    private TypedExpression buildEventTending(JBlock block, JDefinedClass definedClass, InjectionNode tendingInjectionNode, JExpression scopesExpression, Map<InjectionNode, TypedExpression> expressionMap) throws JClassAlreadyExistsException {
        injectionFragmentGenerator.buildFragment(block, instantiationStrategyFactory.buildMethodStrategy(definedClass, block, scopesExpression), definedClass, tendingInjectionNode, scopesExpression, expressionMap);

        return expressionMap.get(tendingInjectionNode);
    }
}

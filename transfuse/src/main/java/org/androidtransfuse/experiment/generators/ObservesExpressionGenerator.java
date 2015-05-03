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
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTJDefinedClassType;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.astAnalyzer.ObservesAspect;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventObserver;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ObservesExpressionGenerator implements Generation {

    private final ASTMethod creationMethod;
    private final ASTMethod registerMethod;
    private final ASTMethod unregisterMethod;
    private final JCodeModel codeModel;
    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;
    private final ClassNamer classNamer;
    private final InvocationBuilder invocationBuilder;
    private final ASTClassFactory astClassFactory;
    private final InjectionPointFactory injectionPointFactory;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;

    @Factory
    public interface ObservesExpressionGeneratorFactory {
        ObservesExpressionGenerator build(@Named("creationMethod") ASTMethod creationMethod,
                                          @Named("registerMethod") ASTMethod registerMethod,
                                          @Named("unregisterMethod") ASTMethod unregisterMethod);
    }

    @Inject
    public ObservesExpressionGenerator(@Named("creationMethod") ASTMethod creationMethod,
                                       @Named("registerMethod") ASTMethod registerMethod,
                                       @Named("unregisterMethod") ASTMethod unregisterMethod,
                                       JCodeModel codeModel,
                                       ClassGenerationUtil generationUtil,
                                       UniqueVariableNamer variableNamer,
                                       ClassNamer classNamer,
                                       InvocationBuilder invocationBuilder,
                                       ASTClassFactory astClassFactory,
                                       InjectionPointFactory injectionPointFactory,
                                       InjectionFragmentGenerator injectionFragmentGenerator,
                                       InstantiationStrategyFactory instantiationStrategyFactory) {
        this.creationMethod = creationMethod;
        this.registerMethod = registerMethod;
        this.unregisterMethod = unregisterMethod;
        this.codeModel = codeModel;
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
        this.classNamer = classNamer;
        this.invocationBuilder = invocationBuilder;
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
    }

    @Override
    public String getName() {
        return "Observes Generator";
    }

    @Override
    public void schedule(final ComponentBuilder builder, ComponentDescriptor descriptor) {
        builder.add(creationMethod, GenerationPhase.POSTINJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                try {
                    //mapping from event type -> observer
                    Map<JClass, JVar> observerTuples = getObservers(builder, builder.getExpressionMap());

                    if (!observerTuples.isEmpty()) {
                        final JVar eventManager = getEventManager(builder, builder.getExpressionMap(), builder.getScopes());

                        for (final Map.Entry<JClass, JVar> tupleEntry : observerTuples.entrySet()) {

                            builder.add(registerMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                                    block.invoke(eventManager, "register")
                                            .arg(tupleEntry.getKey().dotclass())
                                            .arg(tupleEntry.getValue());
                                }
                            });

                            builder.add(unregisterMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                                    block.invoke(eventManager, "unregister")
                                            .arg(tupleEntry.getValue());
                                }
                            });
                        }
                    }

                } catch (JClassAlreadyExistsException e) {
                    throw new TransfuseAnalysisException("Tried to generate a class that already exists", e);
                }
            }
        });
    }

    private JVar getEventManager(final ComponentBuilder builder, final Map<InjectionNode, TypedExpression> expressionMap, final JExpression scopes) {

        ASTType eventManagerType = astClassFactory.getType(EventManager.class);
        final InjectionNode eventManagerInjectionNode = injectionPointFactory.buildInjectionNode(eventManagerType, builder.getAnalysisContext());
        final JVar eventManagerVar = builder.getDefinedClass().field(JMod.PRIVATE, generationUtil.type(eventManagerType), variableNamer.generateName(eventManagerType));

        builder.add(creationMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {

                try {
                    Map<InjectionNode, TypedExpression> eventManagerExpressionMap = injectionFragmentGenerator.buildFragment(block,
                            instantiationStrategyFactory.buildMethodStrategy(block, scopes),
                            builder.getDefinedClass(),
                            eventManagerInjectionNode,
                            scopes,
                            expressionMap);
                    TypedExpression expression = eventManagerExpressionMap.get(eventManagerInjectionNode);
                    block.assign(eventManagerVar, expression.getExpression());

                } catch (JClassAlreadyExistsException e) {
                    throw new TransfuseAnalysisException("Tried to generate a class that already exists", e);
                }

            }
        });

        return eventManagerVar;
    }

    private Map<JClass, JVar> getObservers(final ComponentBuilder builder, Map<InjectionNode, TypedExpression> expressionMap) throws JClassAlreadyExistsException {
        Map<JClass, JVar> observerTuples = new HashMap<JClass, JVar>();

        for (Map.Entry<InjectionNode, TypedExpression> expressionEntry : expressionMap.entrySet()) {

            if (expressionEntry.getKey().containsAspect(ObservesAspect.class)) {
                ObservesAspect aspect = expressionEntry.getKey().getAspect(ObservesAspect.class);
                TypedExpression typedExpression = expressionEntry.getValue();
                final JExpression observerExpression = expressionEntry.getValue().getExpression();

                for (ASTType event : aspect.getEvents()) {

                    //generate inner class EventObserver<E> (E = event)
                    JClass eventRef = generationUtil.ref(event);
                    JClass targetRef = generationUtil.ref(typedExpression.getType());

                    final JDefinedClass observerClass = builder.getDefinedClass()._class(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, classNamer.numberedClassName(typedExpression.getType()).build().getClassName());

                    //target variable
                    JFieldVar targetField = observerClass.field(JMod.PRIVATE, targetRef, variableNamer.generateName(typedExpression.getType()));

                    //match default constructor public WeakObserver(T target){
                    JMethod constructor = observerClass.constructor(JMod.PUBLIC);
                    JVar constTargetParam = constructor.param(targetRef, variableNamer.generateName(targetRef));
                    constructor.body().assign(targetField, constTargetParam);

                    observerClass._implements(generationUtil.ref(EventObserver.class).narrow(eventRef));

                    JMethod triggerMethod = observerClass.method(JMod.PUBLIC, codeModel.VOID, EventObserver.TRIGGER);
                    triggerMethod.annotate(Override.class);
                    JVar eventParam = triggerMethod.param(eventRef, variableNamer.generateName(event));
                    JBlock triggerBody = triggerMethod.body();

                    List<JExpression> parameters = new ArrayList<JExpression>();
                    parameters.add(eventParam);

                    for (ASTMethod observerMethod : aspect.getObserverMethods(event)) {
                        triggerBody.add(invocationBuilder.buildMethodCall(
                                new ASTJDefinedClassType(observerClass),
                                expressionEntry.getKey().getASTType(),
                                observerMethod,
                                parameters,
                                new TypedExpression(typedExpression.getType(), targetField)));
                    }

                    final JFieldVar observerField = builder.getDefinedClass().field(JMod.PRIVATE, observerClass, variableNamer.generateName(EventObserver.class));

                    observerTuples.put(eventRef, observerField);

                    builder.add(creationMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                        @Override
                        public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                            block.assign(observerField, JExpr._new(observerClass).arg(observerExpression));
                        }
                    });

                }
            }
        }

        return observerTuples;
    }
}

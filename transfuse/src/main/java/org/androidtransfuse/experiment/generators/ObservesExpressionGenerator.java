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

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class ObservesExpressionGenerator implements Generation {

    private final ASTMethod creationMethod;
    private final ASTMethod destroyMethod;
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
                                          @Named("destroyMethod") ASTMethod destroyMethod,
                                          @Named("registerMethod") ASTMethod registerMethod,
                                          @Named("unregisterMethod") ASTMethod unregisterMethod);
    }

    @Inject
    public ObservesExpressionGenerator(@Named("creationMethod") ASTMethod creationMethod,
                                       @Named("destroyMethod") ASTMethod destroyMethod,
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
        this.destroyMethod = destroyMethod;
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

                    ImmutableList<InjectionNode> observableInjectionNodes = FluentIterable.from(builder.getExpressionMap().keySet())
                            .filter(new Predicate<InjectionNode>() {
                                @Override
                                public boolean apply(InjectionNode injectionNode) {
                                    return injectionNode.containsAspect(ObservesAspect.class);
                                }
                            }).toList();

                    if(!observableInjectionNodes.isEmpty()) {
                        //mapping from event type -> observer
                        final JVar observerCollection = getObserversCollection(builder, observableInjectionNodes);

                        if (observerCollection != null) {
                            final JVar eventManager = getEventManager(builder, builder.getExpressionMap(), builder.getScopes());

                            builder.add(registerMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                                    final JClass mapEntryType = generationUtil.ref(Map.Entry.class).narrow(EventObserver.class, Class.class);
                                    JForEach forEachLoop = block.forEach(mapEntryType, variableNamer.generateName(EventObserver.class), observerCollection.invoke("entrySet"));
                                    forEachLoop.body().invoke(eventManager, "register")
                                            .arg(forEachLoop.var().invoke("getValue"))
                                            .arg(forEachLoop.var().invoke("getKey"));
                                }
                            });

                            builder.add(unregisterMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                                    JForEach forEachLoop = block.forEach(generationUtil.ref(EventObserver.class), variableNamer.generateName(EventObserver.class), observerCollection.invoke("keySet"));
                                    forEachLoop.body().invoke(eventManager, "unregister")
                                            .arg(forEachLoop.var());
                                }
                            });

                            builder.add(destroyMethod, GenerationPhase.TEARDOWN, new ComponentMethodGenerator() {
                                @Override
                                public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                                    block.invoke(observerCollection, "clear");
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

    private JVar getObserversCollection(final ComponentBuilder builder, ImmutableList<InjectionNode> observableInjectionNodes) throws JClassAlreadyExistsException {
        JClass mapType = generationUtil.ref(Map.class).narrow(EventObserver.class, Class.class);
        JClass hashMapType = generationUtil.ref(HashMap.class).narrow(EventObserver.class, Class.class);
        final JVar observersCollection = builder.getDefinedClass().field(Modifier.PRIVATE, mapType, variableNamer.generateName("observesMap"), JExpr._new(hashMapType));

        for (InjectionNode observableInjectionNode : observableInjectionNodes) {
            TypedExpression typedExpression = builder.getExpressionMap().get(observableInjectionNode);
            final JExpression observerExpression = builder.getExpressionMap().get(observableInjectionNode).getExpression();
            ObservesAspect aspect = observableInjectionNode.getAspect(ObservesAspect.class);


            for (ASTType event : aspect.getEvents()) {

                //generate inner class EventObserver<E> (E = event)
                final JClass eventRef = generationUtil.ref(event);
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
                            typedExpression.getType(),
                            observerMethod,
                            parameters,
                            new TypedExpression(typedExpression.getType(), targetField)));
                }

                builder.add(creationMethod, GenerationPhase.REGISTRATION, new ComponentMethodGenerator() {
                    @Override
                    public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                        block.invoke(observersCollection, "put").arg(JExpr._new(observerClass).arg(observerExpression)).arg(eventRef.dotclass());
                    }
                });

            }
        }

        return observersCollection;
    }
}

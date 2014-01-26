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
package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.*;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.AOPProxyAspect;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.proxy.AOPProxyGenerator;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.transaction.TransactionRuntimeException;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class VariableInjectionBuilder implements VariableBuilder {

    private final ClassGenerationUtil generationUtil;
    private final UniqueVariableNamer variableNamer;
    private final InvocationBuilder injectionInvocationBuilder;
    private final AOPProxyGenerator aopProxyGenerator;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;
    private final ExceptionWrapper exceptionWrapper;
    private final ExpressionMatchingIterableFactory generatorFactory;
    private final Validator validator;

    @Inject
    public VariableInjectionBuilder(ClassGenerationUtil generationUtil,
                                    UniqueVariableNamer variableNamer,
                                    InvocationBuilder injectionInvocationBuilder,
                                    AOPProxyGenerator aopProxyGenerator,
                                    InjectionExpressionBuilder injectionExpressionBuilder,
                                    TypedExpressionFactory typedExpressionFactory,
                                    ExceptionWrapper exceptionWrapper,
                                    ExpressionMatchingIterableFactory generatorFactory,
                                    Validator validator) {
        this.generationUtil = generationUtil;
        this.variableNamer = variableNamer;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.aopProxyGenerator = aopProxyGenerator;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
        this.exceptionWrapper = exceptionWrapper;
        this.generatorFactory = generatorFactory;
        this.validator = validator;
    }

    @Override
    public TypedExpression buildVariable(final InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        final JVar variableRef;
        try {

            //AOP proxy (if applicable).  This method will return the injectionNode (without proxying) if no AOPProxyAspect exists
            final InjectionNode proxyableInjectionNode = injectionNode.containsAspect(AOPProxyAspect.class) ?
                    aopProxyGenerator.generateProxy(injectionNode) : injectionNode;

            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, proxyableInjectionNode);

            final ASTType nodeType = proxyableInjectionNode.getASTType();

            final ASTInjectionAspect injectionAspect = proxyableInjectionNode.getAspect(ASTInjectionAspect.class);
            JBlock block = injectionBuilderContext.getBlock();

            if (injectionAspect == null) {
                validator.error(proxyableInjectionNode.getASTType() + " injection not specified")
                        .element(injectionNode.getASTType())
                        .build();
                throw new TransactionRuntimeException("Injection node not mapped: " + proxyableInjectionNode.getASTType());
            } else if (injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint() == null) {
                validator.error("Injection requires either a default no-argument constructor or an @Inject annotated constructor.")
                        .element(injectionNode.getASTType())
                        .build();
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            } else {
                variableRef = exceptionWrapper.wrapException(block,
                        injectionAspect.getConstructorInjectionPoint().getThrowsTypes(),
                        new ExceptionWrapper.BlockWriter<JVar>() {
                            @Override
                            public JVar write(JBlock block) {

                                //constructor injection
                                JExpression constructionExpression = injectionInvocationBuilder.buildConstructorCall(
                                        injectionAspect.getConstructorInjectionPoint(),
                                        generatorFactory.buildExpressionMatchingIterable(
                                                injectionBuilderContext.getVariableMap(),
                                                injectionAspect.getConstructorInjectionPoint().getInjectionNodes()),
                                        nodeType);

                                if (injectionAspect.getAssignmentType().equals(ASTInjectionAspect.InjectionAssignmentType.LOCAL)) {
                                    return injectionBuilderContext.getBlock().decl(generationUtil.ref(nodeType), variableNamer.generateName(proxyableInjectionNode), constructionExpression);
                                } else {
                                    JVar variableRef = injectionBuilderContext.getDefinedClass().field(JMod.PRIVATE, generationUtil.ref(nodeType), variableNamer.generateName(proxyableInjectionNode));
                                    block.assign(variableRef, constructionExpression);
                                    return variableRef;
                                }
                            }
                        });

                for (ASTInjectionAspect.InjectionGroup injectionGroup : injectionAspect.getGroups()) {
                    //field injection
                    for (FieldInjectionPoint fieldInjectionPoint : injectionGroup.getFieldInjectionPoints()) {
                        block.add(
                                injectionInvocationBuilder.buildFieldSet(
                                        injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                                        fieldInjectionPoint,
                                        variableRef));
                    }

                    //method injection
                    for (final MethodInjectionPoint methodInjectionPoint : injectionGroup.getMethodInjectionPoints()) {
                        exceptionWrapper.wrapException(block,
                                methodInjectionPoint.getThrowsTypes(),
                                new ExceptionWrapper.BlockWriter<Void>() {
                                    @Override
                                    public Void write(JBlock block) throws JClassAlreadyExistsException {
                                        block.add(
                                                injectionInvocationBuilder.buildMethodCall(
                                                        ASTVoidType.VOID,
                                                        methodInjectionPoint,
                                                        generatorFactory.buildExpressionMatchingIterable(
                                                                injectionBuilderContext.getVariableMap(),
                                                                methodInjectionPoint.getInjectionNodes()),
                                                        variableRef));
                                        return null;
                                    }
                                });
                    }
                }
            }

        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }

        return typedExpressionFactory.build(injectionNode.getASTType(), variableRef);
    }
}

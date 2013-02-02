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
package org.androidtransfuse.bootstrap;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.TransactionRuntimeException;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.ASTVoidType;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.*;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableDecorator.TypedExpressionFactory;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

public class ExistingVariableInjectionBuilder implements VariableBuilder {

    private final JExpression expression;
    private final InvocationBuilder injectionInvocationBuilder;
    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final TypedExpressionFactory typedExpressionFactory;
    private final ExceptionWrapper exceptionWrapper;
    private final GeneratorFactory2 generatorFactory;

    @Inject
    public ExistingVariableInjectionBuilder(JExpression expression,
                                            InvocationBuilder injectionInvocationBuilder,
                                            InjectionExpressionBuilder injectionExpressionBuilder,
                                            TypedExpressionFactory typedExpressionFactory,
                                            ExceptionWrapper exceptionWrapper,
                                            GeneratorFactory2 generatorFactory) {
        this.expression = expression;
        this.injectionInvocationBuilder = injectionInvocationBuilder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
        this.exceptionWrapper = exceptionWrapper;
        this.generatorFactory = generatorFactory;
    }

    @Override
    public TypedExpression buildVariable(final InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        try {

            injectionExpressionBuilder.setupInjectionRequirements(injectionBuilderContext, injectionNode);

            final ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
            JBlock block = injectionBuilderContext.getBlock();

            if (injectionAspect == null) {
                throw new TransactionRuntimeException("Injection node not mapped: " + injectionNode.getClassName());
            } else if (injectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoints().isEmpty()) {
                throw new TransfuseAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            }

            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                block.add(
                        injectionInvocationBuilder.buildFieldSet(
                                injectionBuilderContext.getVariableMap().get(fieldInjectionPoint.getInjectionNode()),
                                fieldInjectionPoint,
                                expression));
            }

            //method injection
            for (final MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                exceptionWrapper.wrapException(block,
                        methodInjectionPoint.getThrowsTypes(),
                        new ExceptionWrapper.BlockWriter<Void>() {
                            @Override
                            public Void write(JBlock block) throws ClassNotFoundException, JClassAlreadyExistsException {
                                block.add(
                                        injectionInvocationBuilder.buildMethodCall(
                                                ASTVoidType.VOID,
                                                methodInjectionPoint,
                                                generatorFactory.buildExpressionMatchingIterable(
                                                        injectionBuilderContext.getVariableMap(),
                                                        methodInjectionPoint.getInjectionNodes()),
                                                expression));
                                return null;
                            }
                        });
            }

        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse class: " + injectionNode.getClassName(), e);
        } catch (JClassAlreadyExistsException e) {
            throw new TransfuseAnalysisException("JClassAlreadyExistsException while generating injection: " + injectionNode.getClassName(), e);
        }

        return typedExpressionFactory.build(injectionNode.getASTType(), expression);
    }
}
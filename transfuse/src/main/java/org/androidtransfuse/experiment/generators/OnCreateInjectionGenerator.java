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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.annotations.Factory;
import org.androidtransfuse.experiment.*;
import org.androidtransfuse.gen.InjectionFragmentGenerator;
import org.androidtransfuse.gen.InstantiationStrategyFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionBindingBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.Logger;
import org.androidtransfuse.util.TransfuseRuntimeException;
import org.androidtransfuse.validation.Validator;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class OnCreateInjectionGenerator implements Generation {

    private final ASTMethod method;
    private final ASTType target;
    private final InjectionFragmentGenerator injectionFragmentGenerator;
    private final InstantiationStrategyFactory instantiationStrategyFactory;
    private final InjectionBindingBuilder injectionBindingBuilder;
    private final InjectionPointFactory injectionPointFactory;
    private final Validator validator;
    private final Logger log;

    @Factory
    public interface InjectionGeneratorFactory {
        OnCreateInjectionGenerator build(ASTMethod method, ASTType target);
    }

    @Inject
    public OnCreateInjectionGenerator(/*@Assisted*/ASTMethod method,
                                      /*@Assisted*/ASTType target,
                                      InjectionFragmentGenerator injectionFragmentGenerator,
                                      InstantiationStrategyFactory instantiationStrategyFactory,
                                      InjectionBindingBuilder injectionBindingBuilder,
                                      InjectionPointFactory injectionPointFactory,
                                      Validator validator,
                                      Logger log) {
        this.method = method;
        this.injectionFragmentGenerator = injectionFragmentGenerator;
        this.instantiationStrategyFactory = instantiationStrategyFactory;
        this.target = target;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.injectionPointFactory = injectionPointFactory;
        this.validator = validator;
        this.log = log;
    }

    @Override
    public String getName() {
        return "OnCreate Injection Generator";
    }

    @Override
    public void schedule(final ComponentBuilder builder, final ComponentDescriptor descriptor) {

        builder.add(method, GenerationPhase.INJECTION, new ComponentMethodGenerator() {
            @Override
            public void generate(MethodDescriptor methodDescriptor, JBlock block) {
                for (ASTParameter astParameter : methodDescriptor.getASTMethod().getParameters()) {
                    builder.getAnalysisContext().getInjectionNodeBuilders().putType(astParameter.getASTType(), injectionBindingBuilder.buildExpression(methodDescriptor.getParameter(astParameter)));
                }

                descriptor.setRootInjectionNode(injectionPointFactory.buildInjectionNode(target, builder.getAnalysisContext()));

                if(validator.isInError()) {
                    log.debug("Canceling injection generation due to error during analysis.");
                } else {

                    try {
                        injectionFragmentGenerator.buildFragment(
                                block,
                                instantiationStrategyFactory.buildMethodStrategy(block, builder.getScopes()),
                                builder.getDefinedClass(),
                                descriptor.getRootInjectionNode(),
                                builder.getScopes(),
                                builder.getExpressionMap());
                        
                        logExpressionMap(builder.getExpressionMap());
                    } catch (JClassAlreadyExistsException e) {
                        throw new TransfuseRuntimeException("Class already exists", e);
                    }
                }

            }
        });
    }

    private void logExpressionMap(Map<InjectionNode, TypedExpression> expressionMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("Expression Map {");
        for (Map.Entry<InjectionNode, TypedExpression> entry : expressionMap.entrySet()) {
            builder.append("\n\t");
            builder.append(entry.getKey()).append(" -> ").append(entry.getValue().getType());
        }
        builder.append('}');
        log.debug(builder.toString());
    }
}

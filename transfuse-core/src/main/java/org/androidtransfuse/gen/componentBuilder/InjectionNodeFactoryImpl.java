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

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.adapter.ASTAnnotation;
import org.androidtransfuse.adapter.ASTParameter;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableFactoryBuilderFactory2;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.InjectionSignature;
import org.androidtransfuse.model.MethodDescriptor;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.util.QualifierPredicate;
import org.androidtransfuse.util.matcher.Matchers;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class InjectionNodeFactoryImpl implements InjectionNodeFactory {

    private final ImmutableSet<ASTAnnotation> annotations;
    private final ASTType astType;
    private final AnalysisContext context;
    private final InjectionPointFactory injectionPointFactory;
    private final VariableFactoryBuilderFactory2 injectionBindingBuilder;
    private final QualifierPredicate qualifierPredicate;

    @Inject
    public InjectionNodeFactoryImpl(/*@Assisted*/ ImmutableSet<ASTAnnotation> annotations,
                                    /*@Assisted*/ ASTType astType,
                                    /*@Assisted*/ AnalysisContext context,
                                    InjectionPointFactory injectionPointFactory,
                                    VariableFactoryBuilderFactory2 injectionBindingBuilder,
                                    QualifierPredicate qualifierPredicate) {
        this.annotations = annotations;
        this.astType = astType;
        this.context = context;
        this.injectionPointFactory = injectionPointFactory;
        this.injectionBindingBuilder = injectionBindingBuilder;
        this.qualifierPredicate = qualifierPredicate;
    }

    @Override
    public InjectionNode buildInjectionNode(MethodDescriptor onCreateMethodDescriptor) {
        buildVariableBuilderMap(onCreateMethodDescriptor, context.getInjectionNodeBuilders());

        return injectionPointFactory.buildInjectionNode(annotations, astType, context);
    }

    private void buildVariableBuilderMap(MethodDescriptor methodDescriptor, InjectionNodeBuilderRepository injectionNodeBuilders) {

        for (Map.Entry<ASTParameter, TypedExpression> parameterTypedExpressionEntry : methodDescriptor.getParameters().entrySet()) {
            ASTParameter parameter = parameterTypedExpressionEntry.getKey();
            ASTType parameterType = parameterTypedExpressionEntry.getKey().getASTType();
            TypedExpression expression = parameterTypedExpressionEntry.getValue();

            ImmutableSet<ASTAnnotation> qualifiers = FluentIterable.from(parameter.getAnnotations()).filter(qualifierPredicate).toSet();

            if(qualifiers.isEmpty()){
                injectionNodeBuilders.putType(new InjectionSignature(parameterType), buildExpression(expression));
            }
            else{
                injectionNodeBuilders.putSignatureMatcher(Matchers.type(parameterType).annotated().byAnnotation(qualifiers).build(),
                        buildExpression(expression));
            }
        }
    }

    private InjectionNodeBuilder buildExpression(TypedExpression typedExpression){
        return injectionBindingBuilder.buildInjectionNodeBuilder(
                injectionBindingBuilder.buildExpressionWrapper(typedExpression));
    }
}

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
package org.androidtransfuse.analysis;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import org.androidtransfuse.TransfuseAnalysisException;
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionNodeBuilder;
import org.androidtransfuse.gen.variableDecorator.GeneratedProviderInjectionNodeBuilder;
import org.androidtransfuse.model.*;
import org.androidtransfuse.util.QualifierPredicate;
import org.androidtransfuse.util.matcher.Matcher;
import org.androidtransfuse.util.matcher.Matchers;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

/**
 * InjectionPoint Factory for building the various InjectionPoints from the AST
 *
 * @author John Ericksen
 */
public class InjectionPointFactory {

    private final ASTClassFactory astClassFactory;
    private final QualifierPredicate qualifierPredicate;
    private final VariableInjectionNodeBuilder defaultBinding;
    private final Matcher<ASTType> providerMatcher;
    private final Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider;

    @Inject
    public InjectionPointFactory(ASTClassFactory astClassFactory,
                                 QualifierPredicate qualifierPredicate,
                                 VariableInjectionNodeBuilder defaultBinding,
                                 Provider<GeneratedProviderInjectionNodeBuilder> generatedProviderInjectionNodeBuilderProvider) {
        this.astClassFactory = astClassFactory;
        this.qualifierPredicate = qualifierPredicate;
        this.defaultBinding = defaultBinding;
        this.generatedProviderInjectionNodeBuilderProvider = generatedProviderInjectionNodeBuilderProvider;

        this.providerMatcher = Matchers.type(astClassFactory.getType(Provider.class)).ignoreGenerics().build();
    }

    /**
     * Build a Constructor InjectionPoint from the given ASTConstructor
     *
     * @param astConstructor required ASTConstructor
     * @param context        required AnalysisContext
     * @return ConstructorInjectionPoint
     */
    public ConstructorInjectionPoint buildInjectionPoint(ASTType concreteType, ASTConstructor astConstructor, AnalysisContext context) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(concreteType, astConstructor.getAccessModifier());
        constructorInjectionPoint.addThrows(astConstructor.getThrowsTypes());

        List<ASTAnnotation> methodAnnotations = new ArrayList<ASTAnnotation>();
        //bindingAnnotations for single parameter from method level
        if (astConstructor.getParameters().size() == 1) {
            methodAnnotations.addAll(astConstructor.getAnnotations());
        }

        for (ASTParameter astParameter : astConstructor.getParameters()) {
            List<ASTAnnotation> parameterAnnotations = new ArrayList<ASTAnnotation>(methodAnnotations);
            parameterAnnotations.addAll(astParameter.getAnnotations());
            constructorInjectionPoint.addInjectionNode(buildInjectionNode(parameterAnnotations, astParameter.getASTType(), context));
        }

        return constructorInjectionPoint;
    }

    /**
     * Build a Method Injection Point from the given ASTMethod
     *
     * @param concreteType
     * @param astMethod    required ASTMethod
     * @param context      analysis context
     * @return MethodInjectionPoint
     */
    public MethodInjectionPoint buildInjectionPoint(ASTType concreteType, ASTMethod astMethod, AnalysisContext context) {

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(concreteType, astMethod.getAccessModifier(), astMethod.getName());
        methodInjectionPoint.addThrows(astMethod.getThrowsTypes());

        List<ASTAnnotation> methodAnnotations = new ArrayList<ASTAnnotation>();
        //bindingAnnotations for single parameter from method level
        if (astMethod.getParameters().size() == 1) {
            methodAnnotations.addAll(astMethod.getAnnotations());
        }

        for (ASTParameter astField : astMethod.getParameters()) {
            List<ASTAnnotation> parameterAnnotations = new ArrayList<ASTAnnotation>(methodAnnotations);
            parameterAnnotations.addAll(astField.getAnnotations());
            methodInjectionPoint.addInjectionNode(buildInjectionNode(parameterAnnotations, astField.getASTType(), context));
        }

        return methodInjectionPoint;
    }

    /**
     * Build a Field InjectionPoint from the given ASTField
     *
     * @param concreteType
     * @param astField     required ASTField
     * @param context      analysis context
     * @return FieldInjectionPoint
     */
    public FieldInjectionPoint buildInjectionPoint(ASTType concreteType, ASTField astField, AnalysisContext context) {
        return new FieldInjectionPoint(concreteType, astField.getAccessModifier(), astField.getName(), buildInjectionNode(astField.getAnnotations(), astField.getASTType(), context));
    }

    /**
     * Build a InjectionPoint directly from the given ASTType
     *
     * @param astType required type
     * @param context analysis context
     * @return Injection Node
     */
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return buildInjectionNode(Collections.EMPTY_LIST, astType, context);
    }

    public InjectionNode buildInjectionNode(Class type, AnalysisContext context) {
        return buildInjectionNode(astClassFactory.getType(type), context);
    }

    public InjectionNode buildInjectionNode(Collection<ASTAnnotation> annotations, ASTType astType, AnalysisContext context) {

        ImmutableSet<ASTAnnotation> qualifiers =
                FluentIterable.from(annotations).filter(qualifierPredicate).toImmutableSet();

        //specific binding annotation lookup
        return buildInjectionNode(context.getInjectionNodeBuilders(), astType, context, qualifiers);
    }

    public InjectionNode buildInjectionNode(InjectionNodeBuilderRepository repository, ASTType astType, AnalysisContext context, ImmutableSet<ASTAnnotation> qualifiers) {
        //check type and qualifiers
        InjectionSignature injectionSignature = new InjectionSignature(astType, qualifiers);
        InjectionNodeBuilder typeQualifierBuilder = get(repository.getTypeQualifierBindings(), injectionSignature);

        if(typeQualifierBuilder != null){
            return typeQualifierBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        //check type
        InjectionNodeBuilder typeBindingBuilder = repository.getTypeBindings().get(injectionSignature);

        if (typeBindingBuilder != null) {
            return typeBindingBuilder.buildInjectionNode(astType, context, qualifiers);
        }

        if(qualifiers.size() > 0){
            throw new TransfuseAnalysisException("Unable to find injection node for annotated type: " + astType + " " +
                    StringUtils.join(qualifiers, ", "));
        }

        //generated provider
        if(providerMatcher.matches(astType)){
            return generatedProviderInjectionNodeBuilderProvider.get().buildInjectionNode(astType, context, qualifiers);
        }

        //default case
        return defaultBinding.buildInjectionNode(astType, context, qualifiers);
    }

    private <T> InjectionNodeBuilder get(Map<Matcher<T>, InjectionNodeBuilder> builderMap, T input){
        List<InjectionNodeBuilder> builders = new ArrayList<InjectionNodeBuilder>();
        for (Map.Entry<Matcher<T>, InjectionNodeBuilder> bindingEntry : builderMap.entrySet()) {
            if (bindingEntry.getKey().matches(input)) {
                builders.add(bindingEntry.getValue());
            }
        }
        if(builders.size() > 1){
            throw new TransfuseAnalysisException("Multiple types matched on type " + input + ":" + StringUtils.join(builders, ","));
        }
        if(builders.size() == 1){
            return builders.get(0);
        }
        return null;
    }
}

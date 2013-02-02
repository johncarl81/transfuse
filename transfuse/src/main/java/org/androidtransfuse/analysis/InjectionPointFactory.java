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
import org.androidtransfuse.adapter.*;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.androidtransfuse.util.QualifierPredicate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * InjectionPoint Factory for building the various InjectionPoints from the AST
 *
 * @author John Ericksen
 */
public class InjectionPointFactory {

    private final ASTClassFactory astClassFactory;
    private final QualifierPredicate qualifierPredicate;

    @Inject
    public InjectionPointFactory(ASTClassFactory astClassFactory, QualifierPredicate qualifierPredicate) {
        this.astClassFactory = astClassFactory;
        this.qualifierPredicate = qualifierPredicate;
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

        InjectionNodeBuilder injectionNodeBuilders = context.getInjectionNodeBuilders();

        ImmutableSet<ASTAnnotation> qualifiers =
                FluentIterable.from(annotations).filter(qualifierPredicate).toImmutableSet();

        //specific binding annotation lookup
        return injectionNodeBuilders.buildInjectionNode(astType, context, qualifiers);
    }
}

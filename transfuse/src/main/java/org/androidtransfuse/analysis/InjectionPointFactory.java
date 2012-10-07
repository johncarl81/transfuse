package org.androidtransfuse.analysis;

import org.androidtransfuse.analysis.adapter.*;
import org.androidtransfuse.analysis.repository.InjectionNodeBuilderRepository;
import org.androidtransfuse.gen.variableBuilder.InjectionNodeBuilder;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;

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

    @Inject
    public InjectionPointFactory(ASTClassFactory astClassFactory) {
        this.astClassFactory = astClassFactory;
    }

    /**
     * Build a Constructor InjectionPoint from the given ASTConstructor
     *
     * @param astConstructor required ASTConstructor
     * @param context        required AnalysisContext
     * @return ConstructorInjectionPoint
     */
    public ConstructorInjectionPoint buildInjectionPoint(ASTType concreteType, ASTConstructor astConstructor, AnalysisContext context) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(astConstructor.getAccessModifier(), concreteType);
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
     *
     * @param concreteType
     * @param astMethod required ASTMethod
     * @param context   analysis context
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
     *
     * @param concreteType
     * @param astField required ASTField
     * @param context  analysis context
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
        return buildInjectionNode(astClassFactory.buildASTClassType(type), context);
    }

    private InjectionNode buildInjectionNode(Collection<ASTAnnotation> annotations, ASTType astType, AnalysisContext context) {

        int bindingCount = 0;
        InjectionNodeBuilder injectionNodeBuilder = null;
        InjectionNodeBuilderRepository injectionNodeBuilders = context.getInjectionNodeBuilders();

        //specific binding annotation lookup
        for (ASTAnnotation bindingAnnotation : annotations) {
            if (injectionNodeBuilders.containsBinding(bindingAnnotation)) {
                bindingCount++;
                injectionNodeBuilder = injectionNodeBuilders.getBinding(bindingAnnotation);
            }
        }

        if (bindingCount > 1) {
            throw new TransfuseAnalysisException("More than one binding annotation is not valid.");
        }

        if (injectionNodeBuilder != null) {
            return injectionNodeBuilder.buildInjectionNode(astType, context, annotations);
        }

        InjectionNodeBuilder noBindingInjectionNodeBuilder = injectionNodeBuilders.getBinding(astType);
        return noBindingInjectionNodeBuilder.buildInjectionNode(astType, context, null);
    }
}

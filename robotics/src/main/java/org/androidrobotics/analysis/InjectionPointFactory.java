package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.*;
import org.androidrobotics.analysis.astAnalyzer.BindingRepository;
import org.androidrobotics.gen.variableBuilder.InjectionNodeBuilder;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

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

    private BindingRepository bindingRepository = null;

    /**
     * Build a Constructor InjectionPoint from the given ASTConstructor
     *
     * @param astConstructor required ASTConstructor
     * @return ConstructorInjectionPoint
     */
    public ConstructorInjectionPoint buildInjectionPoint(ASTConstructor astConstructor, AnalysisContext context) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(astConstructor.getAccessModifier());

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
     * @param astMethod required ASTMEthod
     * @return MethodInjectionPoint
     */
    public MethodInjectionPoint buildInjectionPoint(ASTMethod astMethod, AnalysisContext context) {

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(astMethod.getAccessModifier(), astMethod.getName(), context.getSuperClassLevel());

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
     * @param astField required ASTField
     * @param context
     * @return FieldInjectionPoint
     */
    public FieldInjectionPoint buildInjectionPoint(ASTField astField, AnalysisContext context) {
        return new FieldInjectionPoint(astField.getAccessModifier(), astField.getName(), buildInjectionNode(astField.getAnnotations(), astField.getASTType(), context), context.getSuperClassLevel());
    }

    /**
     * Build a InjectionPoint directly from the given ASTType
     *
     * @param astType
     * @param context
     * @return Injection Node
     */
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        return buildInjectionNode(Collections.EMPTY_LIST, astType, context);
    }

    private InjectionNode buildInjectionNode(Collection<ASTAnnotation> bindingAnnotations, ASTType astType, AnalysisContext context) {

        int bindingCount = 0;
        InjectionNodeBuilder injectionNodeBuilder = null;
        ASTAnnotation foundBindingAnnotation = null;

        for (ASTAnnotation bindingAnnotation : bindingAnnotations) {
            if (bindingRepository.containsBindingVariableBuilder(bindingAnnotation)) {
                bindingCount++;
                injectionNodeBuilder = bindingRepository.getBindingVariableBuilder(bindingAnnotation);
                foundBindingAnnotation = bindingAnnotation;
            }
        }

        if (bindingCount > 1) {
            throw new RoboticsAnalysisException("More than one binding annotations is not valid.");
        }

        if (injectionNodeBuilder != null) {
            return injectionNodeBuilder.buildInjectionNode(astType, context, foundBindingAnnotation);
        }

        InjectionNodeBuilder noBindingInjectionNodeBuilder = context.getInjectionNodeBuilders().get(astType.getName());
        return noBindingInjectionNodeBuilder.buildInjectionNode(astType, context, null);
    }

    @Inject
    public void setBindingRepository(BindingRepository bindingRepository) {
        this.bindingRepository = bindingRepository;
    }
}

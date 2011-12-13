package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.*;
import org.androidrobotics.gen.VariableBuilder;
import org.androidrobotics.gen.VariableBuilderRepository;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

/**
 * InjectionPoint Factory for building the various InjectionPoints from the AST
 *
 * @author John Ericksen
 */
public class InjectionPointFactory {

    private VariableBuilderRepository variableBuilderRepository;

    public InjectionPointFactory(VariableBuilderRepository variableBuilderRepository) {
        this.variableBuilderRepository = variableBuilderRepository;
    }

    /**
     * Build a Constructor InjectionPoint from the given ASTConstructor
     *
     * @param astConstructor required ASTConstructor
     * @return ConstructorInjectionPoint
     */
    public ConstructorInjectionPoint buildInjectionPoint(ASTConstructor astConstructor, AnalysisContext context) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint();
        for (ASTParameter astParameter : astConstructor.getParameters()) {

            constructorInjectionPoint.addInjectionNode(buildInjectionNode(astParameter.getASTType(), context));
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

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(astMethod.getName());

        for (ASTParameter astField : astMethod.getParameters()) {
            methodInjectionPoint.addInjectionNode(buildInjectionNode(astField.getASTType(), context));
        }

        return methodInjectionPoint;
    }

    /**
     * Build a Field InjectionPoint from the given ASTField
     *
     * @param astField required ASTField
     * @return FieldInjectionPoint
     */
    public FieldInjectionPoint buildInjectionPoint(ASTField astField, AnalysisContext context) {
        return buildFieldInjectionPoint(astField.getName(), astField.getASTType(), context);
    }

    /**
     * Build a Field InjectionPoint directly from the given ASTType
     *
     * @param astType
     * @return
     */
    public FieldInjectionPoint buildInjectionPoint(ASTType astType) {
        return buildFieldInjectionPoint(astType.getName(), astType, new AnalysisContext());
    }

    private FieldInjectionPoint buildFieldInjectionPoint(String name, ASTType astType, AnalysisContext context) {
        return new FieldInjectionPoint(name, buildInjectionNode(astType, context));
    }

    private InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
        VariableBuilder variableBuilder = variableBuilderRepository.get(astType.getName());
        return variableBuilder.buildInjectionNode(astType, context);
    }
}

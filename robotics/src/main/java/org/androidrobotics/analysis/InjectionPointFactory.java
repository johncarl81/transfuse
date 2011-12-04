package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.*;
import org.androidrobotics.gen.VariableBuilderRepository;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * InjectionPoint Factory for building the various InjectionPoints from the AST
 *
 * @author John Ericksen
 */
@Singleton
public class InjectionPointFactory {

    @Inject
    private TypeInjectionAnalyzer typeInjectionAnalyzer;

    /**
     * Build a Constructor InjectionPoint from the given ASTConstructor
     *
     * @param astConstructor required ASTConstructor
     * @return ConstructorInjectionPoint
     */
    public ConstructorInjectionPoint buildInjectionPoint(ASTConstructor astConstructor, AnalysisContext context) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint();
        for (ASTParameter astParameter : astConstructor.getParameters()) {
            constructorInjectionPoint.addInjectionNode(typeInjectionAnalyzer.analyze(astParameter.getASTType(), context, true));
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
            methodInjectionPoint.addInjectionNode(typeInjectionAnalyzer.analyze(astField.getASTType(), context, false));
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

        InjectionNode injectionNode = typeInjectionAnalyzer.analyze(astField.getASTType(), context, false);

        return new FieldInjectionPoint(astField.getName(), injectionNode);
    }

    /**
     * Build a Field InjectionPoint directly from the given ASTType
     *
     * @param astType
     * @param variableBuilderRepository
     * @return
     */
    public FieldInjectionPoint buildInjectionPoint(ASTType astType, VariableBuilderRepository variableBuilderRepository) {

        InjectionNode injectionNode = typeInjectionAnalyzer.analyze(astType, variableBuilderRepository);

        return new FieldInjectionPoint(astType.getName(), injectionNode);
    }
}

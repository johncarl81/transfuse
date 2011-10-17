package org.androidrobotics.analysis;

import org.androidrobotics.analysis.adapter.*;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.MethodInjectionPoint;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class InjectionPointFactory {

    @Inject
    private TypeInjectionAnalyzer typeInjectionAnalyzer;

    public ConstructorInjectionPoint buildInjectionPoint(ASTConstructor astConstructor) {

        ConstructorInjectionPoint constructorInjectionPoint = new ConstructorInjectionPoint(astConstructor.getName());
        for (ASTParameter astParameter : astConstructor.getParameters()) {
            constructorInjectionPoint.addInjectionNode(typeInjectionAnalyzer.analyze(astParameter.getASTType()));
        }

        return constructorInjectionPoint;
    }

    public MethodInjectionPoint buildInjectionPoint(ASTMethod astMethod) {

        MethodInjectionPoint methodInjectionPoint = new MethodInjectionPoint(astMethod.getName());

        for (ASTParameter astField : astMethod.getParameters()) {
            methodInjectionPoint.addInjectionNode(typeInjectionAnalyzer.analyze(astField.getASTType()));
        }

        return methodInjectionPoint;
    }

    public FieldInjectionPoint buildInjectionPoint(ASTField astField) {

        InjectionNode injectionNode = typeInjectionAnalyzer.analyze(astField.getASTType());

        return new FieldInjectionPoint(astField.getName(), injectionNode);
    }

    public FieldInjectionPoint buildInjectionPoint(ASTType astType) {

        InjectionNode injectionNode = typeInjectionAnalyzer.analyze(astType);

        return new FieldInjectionPoint(astType.getName(), injectionNode);
    }
}

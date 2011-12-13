package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.ASTAnalysis;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTConstructor;
import org.androidrobotics.analysis.adapter.ASTField;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionAnalyzer implements ASTAnalysis {

    private InjectionPointFactory injectionPointFactory;

    public InjectionAnalyzer(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeType(InjectionNode injectionNode, ASTType astType, AnalysisContext context) {

        ASTConstructor noArgConstructor = null;
        boolean constructorFound = false;

        for (ASTConstructor astConstructor : astType.getConstructors()) {
            if (astConstructor.isAnnotated(Inject.class)) {
                injectionNode.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astConstructor, context));
                constructorFound = true;
            }
            if (astConstructor.getParameters().size() == 0) {
                noArgConstructor = astConstructor;
            }
        }

        if (!constructorFound) {
            if (noArgConstructor == null) {
                throw new RoboticsAnalysisException("No-Arg Constructor required for injection point: " + injectionNode.getClassName());
            }
            injectionNode.addInjectionPoint(injectionPointFactory.buildInjectionPoint(noArgConstructor, context));
        }
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        if (astMethod.isAnnotated(Inject.class)) {
            injectionNode.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astMethod, context));
        }
    }

    @Override
    public void analyzeField(InjectionNode injectionNode, ASTField astField, AnalysisContext context) {
        if (astField.isAnnotated(Inject.class)) {
            injectionNode.addInjectionPoint(injectionPointFactory.buildInjectionPoint(astField, context));
        }
    }
}

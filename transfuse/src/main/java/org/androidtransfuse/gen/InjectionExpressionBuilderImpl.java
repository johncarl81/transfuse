package org.androidtransfuse.gen;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.gen.variableDecorator.VariableExpressionBuilder;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectionExpressionBuilderImpl implements InjectionExpressionBuilder {

    @Inject
    private VariableExpressionBuilder expressionDecorator;

    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return expressionDecorator.buildVariableExpression(injectionBuilderContext, injectionNode);
    }

    public void setupInjectionRequirements(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ASTInjectionAspect injectionAspect = injectionNode.getAspect(ASTInjectionAspect.class);
        if (injectionAspect != null) {
            //constructor injection
            for (ConstructorInjectionPoint constructorInjectionPoint : injectionAspect.getConstructorInjectionPoints()) {
                for (InjectionNode constructorNode : constructorInjectionPoint.getInjectionNodes()) {
                    buildVariable(injectionBuilderContext, constructorNode);
                }
            }
            //field injection
            for (FieldInjectionPoint fieldInjectionPoint : injectionAspect.getFieldInjectionPoints()) {
                buildVariable(injectionBuilderContext, fieldInjectionPoint.getInjectionNode());
            }
            //method injection
            for (MethodInjectionPoint methodInjectionPoint : injectionAspect.getMethodInjectionPoints()) {
                for (InjectionNode methodNode : methodInjectionPoint.getInjectionNodes()) {
                    buildVariable(injectionBuilderContext, methodNode);
                }
            }
        }
    }
}

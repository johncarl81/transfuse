package org.androidtransfuse.gen.variableDecorator;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class VariableBuilderExpressionDecorator implements VariableExpressionBuilder {

    @Override
    public JExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        VariableBuilder variableBuilder = injectionNode.getAspect(VariableBuilder.class);
        return variableBuilder.buildVariable(injectionBuilderContext, injectionNode);
    }
}

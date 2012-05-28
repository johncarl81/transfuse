package org.androidtransfuse.gen.variableDecorator;

import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class VariableBuilderExpressionDecorator implements VariableExpressionBuilder {

    @Override
    public TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        VariableBuilder variableBuilder = injectionNode.getAspect(VariableBuilder.class);
        return variableBuilder.buildVariable(injectionBuilderContext, injectionNode);
    }
}

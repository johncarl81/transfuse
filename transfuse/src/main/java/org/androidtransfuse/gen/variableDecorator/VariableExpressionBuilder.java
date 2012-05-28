package org.androidtransfuse.gen.variableDecorator;

import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableExpressionBuilder {

    TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}

package org.androidtransfuse.gen.variableDecorator;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableExpressionBuilder {

    JExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}

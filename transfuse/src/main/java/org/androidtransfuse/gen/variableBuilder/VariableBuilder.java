package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}

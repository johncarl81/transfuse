package org.androidtransfuse.gen.variableBuilder;

import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface VariableBuilder {

    TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}

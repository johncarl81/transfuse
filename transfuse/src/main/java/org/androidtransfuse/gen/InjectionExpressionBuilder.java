package org.androidtransfuse.gen;

import org.androidtransfuse.model.InjectionNode;

/**
 * @author John Ericksen
 */
public interface InjectionExpressionBuilder {

    TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);

    void setupInjectionRequirements(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode);
}

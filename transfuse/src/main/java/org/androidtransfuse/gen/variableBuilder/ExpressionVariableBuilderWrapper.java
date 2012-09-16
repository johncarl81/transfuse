package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExpressionVariableBuilderWrapper implements VariableBuilder {

    private final TypedExpression expression;

    @Inject
    public ExpressionVariableBuilderWrapper(@Assisted TypedExpression expression) {
        this.expression = expression;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        return expression;
    }
}

package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class IndependentVariableBuilderWrapper extends ConsistentTypeVariableBuilder {

    private JExpression expression;

    @Inject
    public IndependentVariableBuilderWrapper(@Assisted Class clazz, @Assisted JExpression expression, TypedExpressionFactory typedExpressionFactory) {
        super(clazz, typedExpressionFactory);
        this.expression = expression;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        return expression;
    }
}

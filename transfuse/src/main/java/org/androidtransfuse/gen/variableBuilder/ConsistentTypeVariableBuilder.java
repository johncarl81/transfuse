package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public abstract class ConsistentTypeVariableBuilder implements VariableBuilder {

    private TypedExpressionFactory typedExpressionFactory;
    private Class clazz;

    public ConsistentTypeVariableBuilder(Class clazz) {
        this.clazz = clazz;
    }

    @Inject
    public void setTypedExpressionFactory(TypedExpressionFactory typedExpressionFactory) {
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext context, InjectionNode injectionNode) {
        return typedExpressionFactory.build(clazz, buildExpression(context, injectionNode));
    }

    public abstract JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode);
}

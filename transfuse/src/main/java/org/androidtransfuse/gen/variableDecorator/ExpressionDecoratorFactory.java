package org.androidtransfuse.gen.variableDecorator;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class ExpressionDecoratorFactory implements Provider<VariableExpressionBuilder> {

    private final VariableExpressionBuilderFactory decoratorFactory;

    @Inject
    public ExpressionDecoratorFactory(VariableExpressionBuilderFactory decoratorFactory) {
        this.decoratorFactory = decoratorFactory;
    }

    public VariableExpressionBuilder get() {
        return decoratorFactory.buildCachedExpressionDecorator(
               decoratorFactory.buildScopedExpressionDecorator(
               decoratorFactory.buildVirtualProxyExpressionDecorator(
               decoratorFactory.buildObserverExpressionDecorator(
               decoratorFactory.buildVariableBuilderExpressionDecorator()))));
    }
}

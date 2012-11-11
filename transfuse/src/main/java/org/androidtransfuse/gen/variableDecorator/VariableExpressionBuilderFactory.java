package org.androidtransfuse.gen.variableDecorator;

/**
 * @author John Ericksen
 */
public interface VariableExpressionBuilderFactory {

    CachedExpressionDecorator buildCachedExpressionDecorator(VariableExpressionBuilder decorator);

    ScopedExpressionDecorator buildScopedExpressionDecorator(VariableExpressionBuilder decorator);

    VariableBuilderExpressionDecorator buildVariableBuilderExpressionDecorator();

    VirtualProxyExpressionDecorator buildVirtualProxyExpressionDecorator(VariableExpressionBuilder decorator);
}

package org.androidtransfuse.gen;

import org.androidtransfuse.intentFactory.AbstractIntentFactoryStrategy;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import java.util.List;
import java.util.Map;

/**
 * @author John Ericksen
 */
public interface GeneratorFactory {

    IntentFactoryStrategyGenerator buildStrategyGenerator(Class<? extends AbstractIntentFactoryStrategy> baseClass);

    ExpressionMatchingIterable buildExpressionMatchingIterable(Map<InjectionNode, TypedExpression> variableMap, List<InjectionNode> keys);
}

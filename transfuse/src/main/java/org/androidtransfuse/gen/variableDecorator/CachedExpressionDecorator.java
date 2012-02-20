package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class CachedExpressionDecorator extends VariableExpressionBuilderDecorator {

    @Inject
    public CachedExpressionDecorator(@Assisted VariableExpressionBuilder decorated) {
        super(decorated);
    }

    @Override
    public JExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        Map<InjectionNode, JExpression> variableMap = injectionBuilderContext.getVariableMap();
        if (!variableMap.containsKey(injectionNode)) {
            variableMap.put(injectionNode, getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode));
        }
        return variableMap.get(injectionNode);
    }
}

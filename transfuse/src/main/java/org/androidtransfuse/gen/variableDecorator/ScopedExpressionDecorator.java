package org.androidtransfuse.gen.variableDecorator;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.astAnalyzer.ScopeAspect;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ScopedExpressionDecorator extends VariableExpressionBuilderDecorator {

    @Inject
    public ScopedExpressionDecorator(@Assisted VariableExpressionBuilder decorator) {
        super(decorator);
    }

    @Override
    public TypedExpression buildVariableExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ScopeAspect scopeAspect = injectionNode.getAspect(ScopeAspect.class);
        if (scopeAspect != null) {
            return scopeAspect.getScopeBuilder().buildVariable(injectionBuilderContext, injectionNode);
        } else {
            return getDecorated().buildVariableExpression(injectionBuilderContext, injectionNode);
        }
    }
}

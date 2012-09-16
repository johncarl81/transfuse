package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class DependentVariableBuilderWrapper extends ConsistentTypeVariableBuilder {

    private final InjectionExpressionBuilder injectionExpressionBuilder;
    private final InjectionNode dependency;
    private final DependentVariableBuilder dependentVariableBuilder;

    @Inject
    public DependentVariableBuilderWrapper(@Assisted InjectionNode dependency,
                                           @Assisted DependentVariableBuilder dependentVariableBuilder,
                                           @Assisted Class type,
                                           TypedExpressionFactory typedExpressionFactory,
                                           InjectionExpressionBuilder injectionExpressionBuilder) {
        super(type, typedExpressionFactory);
        this.dependency = dependency;
        this.dependentVariableBuilder = dependentVariableBuilder;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {
        TypedExpression dependentExpression = injectionExpressionBuilder.buildVariable(context, dependency);

        return dependentVariableBuilder.buildVariable(dependentExpression.getExpression());
    }
}

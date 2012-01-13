package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

public class ResourceVariableBuilder implements VariableBuilder {

    private int resourceId;
    private ResourceExpressionBuilder resourceExpressionBuilder;

    @Inject
    public ResourceVariableBuilder(@Assisted int resourceId,
                                   @Assisted ResourceExpressionBuilder resourceExpressionBuilder) {
        this.resourceId = resourceId;
        this.resourceExpressionBuilder = resourceExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression resourceIdExpr = JExpr.lit(resourceId);

        return resourceExpressionBuilder.buildExpression(injectionBuilderContext, resourceIdExpr);
    }
}
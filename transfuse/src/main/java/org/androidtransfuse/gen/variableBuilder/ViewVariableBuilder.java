package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ViewVariableBuilder implements VariableBuilder {

    private static final String FIND_VIEW = "findViewById";

    private JType viewType;
    private int viewId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private RResourceReferenceBuilder rResourceReferenceBuilder;

    @Inject
    public ViewVariableBuilder(@Assisted int viewId,
                               @Assisted InjectionNode activityInjectionNode,
                               @Assisted JType viewType,
                               InjectionExpressionBuilder injectionExpressionBuilder,
                               RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.viewId = viewId;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        ResourceIdentifier viewResourceIdentifier = injectionBuilderContext.getRResource().getResourceIdentifier(viewId);
        JExpression viewIdRef = rResourceReferenceBuilder.buildReference(viewResourceIdentifier);

        return JExpr.cast(viewType, contextVar.invoke(FIND_VIEW).arg(viewIdRef));
    }
}
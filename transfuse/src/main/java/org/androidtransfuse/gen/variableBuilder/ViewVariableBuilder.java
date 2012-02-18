package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.*;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ViewVariableBuilder implements VariableBuilder {

    private static final String FIND_VIEW = "findViewById";

    private JType viewType;
    private int viewId;
    private InjectionNode activityInjectionNode;
    private JCodeModel codeModel;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public ViewVariableBuilder(@Assisted int viewId,
                               @Assisted InjectionNode activityInjectionNode,
                               @Assisted JType viewType,
                               JCodeModel codeModel,
                               InjectionVariableBuilder injectionVariableBuilder) {
        this.viewId = viewId;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
        this.codeModel = codeModel;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionVariableBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        ResourceIdentifier viewResourceIdentifier = injectionBuilderContext.getRResource().getResourceIdentifier(viewId);

        ASTType rInnerType = viewResourceIdentifier.getRInnerType();

        JClass rInnerRef = codeModel.ref(rInnerType.getName());

        JFieldRef viewIdRef = rInnerRef.staticRef(viewResourceIdentifier.getName());

        return JExpr.cast(viewType, contextVar.invoke(FIND_VIEW).arg(viewIdRef));
    }
}
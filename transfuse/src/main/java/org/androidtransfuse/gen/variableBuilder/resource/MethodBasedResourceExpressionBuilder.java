package org.androidtransfuse.gen.variableBuilder.resource;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

public class MethodBasedResourceExpressionBuilder implements ResourceExpressionBuilder {

    private String accessMethod;
    private InjectionNode resourcesInjectionNode;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public MethodBasedResourceExpressionBuilder(@Assisted String accessMethod,
                                                @Assisted InjectionNode resourcesInjectionNode,
                                                InjectionVariableBuilder injectionVariableBuilder) {
        this.accessMethod = accessMethod;
        this.resourcesInjectionNode = resourcesInjectionNode;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        JExpression resourcesVar = injectionVariableBuilder.buildVariable(context, resourcesInjectionNode);

        return resourcesVar.invoke(accessMethod).arg(resourceIdExpr);
    }
}
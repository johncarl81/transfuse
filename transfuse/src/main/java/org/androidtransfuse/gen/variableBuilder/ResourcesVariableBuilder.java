package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

public class ResourcesVariableBuilder implements VariableBuilder {

    private static final String GET_RESOURCES = "getResources";
    private InjectionNode applicationInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ResourcesVariableBuilder(@Assisted InjectionNode applicationInjectionNode,
                                    InjectionExpressionBuilder injectionExpressionBuilder) {
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, applicationInjectionNode);

        return contextVar.invoke(GET_RESOURCES);
    }
}
package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

public class ResourcesVariableBuilder implements VariableBuilder {

    private static final String GET_RESOURCES = "getResources";
    private InjectionNode applicationInjectionNode;

    public ResourcesVariableBuilder(InjectionNode applicationInjectionNode) {
        this.applicationInjectionNode = applicationInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionBuilderContext.buildVariable(applicationInjectionNode);

        return contextVar.invoke(GET_RESOURCES);
    }
}
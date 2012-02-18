package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

public class ResourcesVariableBuilder implements VariableBuilder {

    private static final String GET_RESOURCES = "getResources";
    private InjectionNode applicationInjectionNode;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public ResourcesVariableBuilder(@Assisted InjectionNode applicationInjectionNode,
                                    InjectionVariableBuilder injectionVariableBuilder) {
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionVariableBuilder.buildVariable(injectionBuilderContext, applicationInjectionNode);

        return contextVar.invoke(GET_RESOURCES);
    }
}
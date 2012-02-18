package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";

    private InjectionNode providerInjectionNode;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public ProviderVariableBuilder(@Assisted InjectionNode providerInjectionNode,
                                   InjectionVariableBuilder injectionVariableBuilder) {
        this.providerInjectionNode = providerInjectionNode;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JExpression providerVar = injectionVariableBuilder.buildVariable(injectionBuilderContext, providerInjectionNode);

        return providerVar.invoke(PROVIDER_METHOD);
    }

    public InjectionNode getProviderInjectionNode() {
        return providerInjectionNode;
    }
}

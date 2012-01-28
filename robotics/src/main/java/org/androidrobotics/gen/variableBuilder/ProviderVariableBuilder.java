package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";

    private InjectionNode providerInjectionNode;

    public ProviderVariableBuilder(InjectionNode providerInjectionNode) {
        this.providerInjectionNode = providerInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JExpression providerVar = injectionBuilderContext.buildVariable(providerInjectionNode);

        return providerVar.invoke(PROVIDER_METHOD);
    }

    public InjectionNode getProviderInjectionNode() {
        return providerInjectionNode;
    }
}

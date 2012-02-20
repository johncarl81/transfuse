package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";

    private InjectionNode providerInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ProviderVariableBuilder(@Assisted InjectionNode providerInjectionNode,
                                   InjectionExpressionBuilder injectionExpressionBuilder) {
        this.providerInjectionNode = providerInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        JExpression providerVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, providerInjectionNode);

        return providerVar.invoke(PROVIDER_METHOD);
    }

    public InjectionNode getProviderInjectionNode() {
        return providerInjectionNode;
    }
}

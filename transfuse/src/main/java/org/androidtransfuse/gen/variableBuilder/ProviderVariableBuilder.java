package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ProviderVariableBuilder implements VariableBuilder {

    private static final String PROVIDER_METHOD = "get";

    private InjectionNode providerInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public ProviderVariableBuilder(@Assisted InjectionNode providerInjectionNode,
                                   InjectionExpressionBuilder injectionExpressionBuilder, TypedExpressionFactory typedExpressionFactory) {
        this.providerInjectionNode = providerInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {

        TypedExpression providerVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, providerInjectionNode);

        JExpression expression = providerVar.getExpression().invoke(PROVIDER_METHOD);

        return typedExpressionFactory.build(injectionNode.getASTType(), expression);
    }

    public InjectionNode getProviderInjectionNode() {
        return providerInjectionNode;
    }
}

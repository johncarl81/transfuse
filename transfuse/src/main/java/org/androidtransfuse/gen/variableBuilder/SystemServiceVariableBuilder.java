package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceVariableBuilder implements VariableBuilder {

    private static final String GET_SYSTEM_SERVICE = "getSystemService";

    private String systemService;
    private JType systemServiceType;
    private InjectionNode contextInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public SystemServiceVariableBuilder(@Assisted String systemService,
                                        @Assisted JType systemServiceType,
                                        @Assisted InjectionNode contextInjectionNode,
                                        InjectionExpressionBuilder injectionExpressionBuilder) {
        this.systemService = systemService;
        this.systemServiceType = systemServiceType;
        this.contextInjectionNode = contextInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, contextInjectionNode);

        return JExpr.cast(systemServiceType, contextVar.invoke(GET_SYSTEM_SERVICE).arg(JExpr.lit(systemService)));
    }
}

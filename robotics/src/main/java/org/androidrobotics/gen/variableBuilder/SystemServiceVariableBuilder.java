package org.androidrobotics.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceVariableBuilder implements VariableBuilder {

    private static final String GET_SYSTEM_SERVICE = "getSystemService";

    private String systemService;
    private JType systemServiceType;
    private InjectionNode contextInjectionNode;

    @Inject
    public SystemServiceVariableBuilder(@Assisted String systemService,
                                        @Assisted JType systemServiceType,
                                        @Assisted InjectionNode contextInjectionNode) {
        this.systemService = systemService;
        this.systemServiceType = systemServiceType;
        this.contextInjectionNode = contextInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionBuilderContext.buildVariable(contextInjectionNode);

        return JExpr.cast(systemServiceType, contextVar.invoke(GET_SYSTEM_SERVICE).arg(JExpr.lit(systemService)));
    }
}

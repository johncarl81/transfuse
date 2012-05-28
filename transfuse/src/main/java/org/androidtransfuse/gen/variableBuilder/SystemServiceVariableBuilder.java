package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SystemServiceVariableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_SYSTEM_SERVICE = "getSystemService";

    private String systemService;
    private InjectionNode contextInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public SystemServiceVariableBuilder(@Assisted String systemService,
                                        @Assisted InjectionNode contextInjectionNode,
                                        InjectionExpressionBuilder injectionExpressionBuilder) {
        super(Object.class);
        this.systemService = systemService;
        this.contextInjectionNode = contextInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, contextInjectionNode);

        return contextVar.getExpression().invoke(GET_SYSTEM_SERVICE).arg(JExpr.lit(systemService));
    }
}

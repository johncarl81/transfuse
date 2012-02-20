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
public class ExtraValuableBuilder implements VariableBuilder {

    private static final String GET_INTENT = "getIntent";
    private static final String GET_EXTRAS = "getExtras";
    private static final String GET_VALUE = "get";

    private JType extraType;
    private String extraId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;

    @Inject
    public ExtraValuableBuilder(@Assisted String extraId,
                                @Assisted InjectionNode activityInjectionNode,
                                @Assisted JType extraType,
                                InjectionExpressionBuilder injectionExpressionBuilder) {
        this.extraId = extraId;
        this.extraType = extraType;
        this.activityInjectionNode = activityInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        //todo: nullable?
        return JExpr.cast(extraType, contextVar.invoke(GET_INTENT).invoke(GET_EXTRAS).invoke(GET_VALUE).arg(JExpr.lit(extraId)));
    }
}

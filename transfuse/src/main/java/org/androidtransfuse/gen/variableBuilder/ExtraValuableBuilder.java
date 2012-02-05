package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.model.InjectionNode;

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

    public ExtraValuableBuilder(String extraId, InjectionNode activityInjectionNode, JType extraType) {
        this.extraId = extraId;
        this.extraType = extraType;
        this.activityInjectionNode = activityInjectionNode;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionBuilderContext.buildVariable(activityInjectionNode);

        //todo: nullable?
        return JExpr.cast(extraType, contextVar.invoke(GET_INTENT).invoke(GET_EXTRAS).invoke(GET_VALUE).arg(JExpr.lit(extraId)));
    }
}

package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.ExtraUtil;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraValuableBuilder implements VariableBuilder {

    private static final String GET_INTENT = "getIntent";
    private static final String GET_EXTRAS = "getExtras";

    private JType extraType;
    private String extraId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private boolean nullable;
    private JCodeModel codeModel;

    @Inject
    public ExtraValuableBuilder(@Assisted String extraId,
                                @Assisted InjectionNode activityInjectionNode,
                                @Assisted JType extraType,
                                @Assisted boolean nullable,
                                InjectionExpressionBuilder injectionExpressionBuilder,
                                JCodeModel codeModel) {
        this.extraId = extraId;
        this.extraType = extraType;
        this.activityInjectionNode = activityInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.nullable = nullable;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        return JExpr.cast(extraType, codeModel.ref(ExtraUtil.class)
                .staticInvoke(ExtraUtil.GET_INSTANCE).invoke(ExtraUtil.GET_EXTRA)
                .arg(contextVar.invoke(GET_INTENT).invoke(GET_EXTRAS))
                .arg(JExpr.lit(extraId))
                .arg(JExpr.lit(nullable)));
    }
}

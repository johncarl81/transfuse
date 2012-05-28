package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.ExtraUtil;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraValuableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_INTENT = "getIntent";
    private static final String GET_EXTRAS = "getExtras";

    private String extraId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private boolean nullable;
    private JCodeModel codeModel;

    @Inject
    public ExtraValuableBuilder(@Assisted String extraId,
                                @Assisted InjectionNode activityInjectionNode,
                                @Assisted boolean nullable,
                                InjectionExpressionBuilder injectionExpressionBuilder,
                                JCodeModel codeModel) {
        super(Object.class);
        this.extraId = extraId;
        this.activityInjectionNode = activityInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.nullable = nullable;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        return codeModel.ref(ExtraUtil.class)
                .staticInvoke(ExtraUtil.GET_INSTANCE).invoke(ExtraUtil.GET_EXTRA)
                .arg(contextVar.getExpression().invoke(GET_INTENT).invoke(GET_EXTRAS))
                .arg(JExpr.lit(extraId))
                .arg(JExpr.lit(nullable));
    }
}

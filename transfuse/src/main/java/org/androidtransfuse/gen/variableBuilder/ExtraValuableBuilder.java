package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.util.ExtraUtil;
import org.androidtransfuse.util.ParcelableWrapper;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraValuableBuilder extends ConsistentTypeVariableBuilder {

    private static final String GET_INTENT = "getIntent";
    private static final String GET_EXTRAS = "getExtras";

    private boolean wrapped;
    private String extraId;
    private InjectionNode activityInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private boolean nullable;
    private JCodeModel codeModel;

    @Inject
    public ExtraValuableBuilder(@Assisted String extraId,
                                @Assisted InjectionNode activityInjectionNode,
                                @Assisted("nullable") boolean nullable,
                                @Assisted("wrapped") boolean wrapped,
                                InjectionExpressionBuilder injectionExpressionBuilder,
                                JCodeModel codeModel) {
        super(Object.class);
        this.extraId = extraId;
        this.activityInjectionNode = activityInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.nullable = nullable;
        this.codeModel = codeModel;
        this.wrapped = wrapped;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        TypedExpression contextVar = injectionExpressionBuilder.buildVariable(injectionBuilderContext, activityInjectionNode);

        JInvocation getExtraInvocation = codeModel.ref(ExtraUtil.class)
                .staticInvoke(ExtraUtil.GET_INSTANCE).invoke(ExtraUtil.GET_EXTRA)
                .arg(contextVar.getExpression().invoke(GET_INTENT).invoke(GET_EXTRAS))
                .arg(JExpr.lit(extraId))
                .arg(JExpr.lit(nullable));

        if (wrapped) {
            getExtraInvocation = ((JExpression) JExpr.cast(codeModel.ref(ParcelableWrapper.class),
                    getExtraInvocation)).invoke(ParcelableWrapper.GET_WRAPPED);
        }

        return getExtraInvocation;
    }
}

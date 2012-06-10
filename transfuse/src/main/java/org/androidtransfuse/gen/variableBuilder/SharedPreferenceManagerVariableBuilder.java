package org.androidtransfuse.gen.variableBuilder;

import android.preference.PreferenceManager;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class SharedPreferenceManagerVariableBuilder extends ConsistentTypeVariableBuilder{

    private InjectionNode contextNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private JCodeModel codeModel;

    @Inject
    public SharedPreferenceManagerVariableBuilder(@Assisted InjectionNode contextNode,
                                                  TypedExpressionFactory typedExpressionFactory,
                                                  InjectionExpressionBuilder injectionExpressionBuilder, JCodeModel codeModel) {
        super(PreferenceManager.class, typedExpressionFactory);
        this.contextNode = contextNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, InjectionNode injectionNode) {

        TypedExpression contextExpression = injectionExpressionBuilder.buildVariable(context, contextNode);

        ///PreferenceManager.getDefaultSharedPreferences(this);

        return codeModel.ref(PreferenceManager.class).staticInvoke("getDefaultSharedPreferences").arg(contextExpression.getExpression());
    }
}

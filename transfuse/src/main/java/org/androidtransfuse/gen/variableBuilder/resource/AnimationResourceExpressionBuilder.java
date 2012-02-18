package org.androidtransfuse.gen.variableBuilder.resource;

import android.view.animation.AnimationUtils;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionVariableBuilder;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilder implements ResourceExpressionBuilder {

    private static final String LOAD_ANIMATION = "loadAnimation";
    private JCodeModel codeModel;
    private InjectionNode applicationInjectionNode;
    private InjectionVariableBuilder injectionVariableBuilder;

    @Inject
    public AnimationResourceExpressionBuilder(@Assisted InjectionNode applicationInjectionNode,
                                              JCodeModel codeModel,
                                              InjectionVariableBuilder injectionVariableBuilder) {
        this.codeModel = codeModel;
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionVariableBuilder = injectionVariableBuilder;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        JExpression applicationVar = injectionVariableBuilder.buildVariable(context, applicationInjectionNode);

        //AnimationUtils.loadAnimation(application, id);
        return codeModel.ref(AnimationUtils.class).staticInvoke(LOAD_ANIMATION).arg(applicationVar).arg(resourceIdExpr);
    }
}

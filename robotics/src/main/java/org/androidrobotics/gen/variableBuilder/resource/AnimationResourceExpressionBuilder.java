package org.androidrobotics.gen.variableBuilder.resource;

import android.view.animation.AnimationUtils;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilder implements ResourceExpressionBuilder {

    private static final String LOAD_ANIMATION = "loadAnimation";
    private JCodeModel codeModel;
    private InjectionNode applicationInjectionNode;

    @Inject
    public AnimationResourceExpressionBuilder(@Assisted InjectionNode applicationInjectionNode, JCodeModel codeModel) {
        this.codeModel = codeModel;
        this.applicationInjectionNode = applicationInjectionNode;
    }

    @Override
    public JExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        JExpression applicationVar = context.buildVariable(applicationInjectionNode);

        //AnimationUtils.loadAnimation(application, id);
        return codeModel.ref(AnimationUtils.class).staticInvoke(LOAD_ANIMATION).arg(applicationVar).arg(resourceIdExpr);
    }
}

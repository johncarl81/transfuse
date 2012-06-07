package org.androidtransfuse.gen.variableBuilder.resource;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.InjectionExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.TypedExpressionFactory;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilder implements ResourceExpressionBuilder {

    private static final String LOAD_ANIMATION = "loadAnimation";

    private JCodeModel codeModel;
    private InjectionNode applicationInjectionNode;
    private InjectionExpressionBuilder injectionExpressionBuilder;
    private TypedExpressionFactory typedExpressionFactory;

    @Inject
    public AnimationResourceExpressionBuilder(@Assisted InjectionNode applicationInjectionNode,
                                              JCodeModel codeModel,
                                              InjectionExpressionBuilder injectionExpressionBuilder, TypedExpressionFactory typedExpressionFactory) {
        this.codeModel = codeModel;
        this.applicationInjectionNode = applicationInjectionNode;
        this.injectionExpressionBuilder = injectionExpressionBuilder;
        this.typedExpressionFactory = typedExpressionFactory;
    }

    @Override
    public TypedExpression buildExpression(InjectionBuilderContext context, JExpression resourceIdExpr) {
        TypedExpression applicationVar = injectionExpressionBuilder.buildVariable(context, applicationInjectionNode);

        //AnimationUtils.loadAnimation(application, id);
        JExpression expression = codeModel.ref(AnimationUtils.class).staticInvoke(LOAD_ANIMATION).arg(applicationVar.getExpression()).arg(resourceIdExpr);
        return typedExpressionFactory.build(Animation.class, expression);
    }
}

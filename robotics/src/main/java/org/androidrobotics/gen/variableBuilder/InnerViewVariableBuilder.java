package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.InjectionNode;

import javax.lang.model.element.AnnotationValue;

public class InnerViewVariableBuilder implements VariableBuilder {

    private static final String FIND_VIEW = "findViewById";

    private JType viewType;
    private ASTAnnotation annotation;
    private InjectionNode activityInjectionNode;

    public InnerViewVariableBuilder(ASTAnnotation annotation, InjectionNode activityInjectionNode, JType viewType) {
        this.annotation = annotation;
        this.activityInjectionNode = activityInjectionNode;
        this.viewType = viewType;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        JExpression contextVar = injectionBuilderContext.buildVariable(activityInjectionNode);

        //assuming value is non null
        return JExpr.cast(viewType, contextVar.invoke(FIND_VIEW).arg(JExpr.lit((Integer) ((AnnotationValue) annotation.getProperty("value")).getValue())));
    }
}
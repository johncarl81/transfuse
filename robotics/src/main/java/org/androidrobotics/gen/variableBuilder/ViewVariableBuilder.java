package org.androidrobotics.gen.variableBuilder;

import android.app.Activity;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JType;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.InjectionBuilderContext;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationValue;

/**
 * @author John Ericksen
 */
public class ViewVariableBuilder implements AnnotatedVariableBuilder {

    private static final String FIND_VIEW = "findViewById";

    @Inject
    private JCodeModel codeModel;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private Analyzer analyzer;
    @Inject
    private InjectionPointFactory injectionPointFactory;

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
        //InjectionNode activityInjectionNode = analyzer.analyze(activityType, activityType, context);
        FieldInjectionPoint activityInjectionNode = injectionPointFactory.buildInjectionPoint(activityType, context);

        try {
            injectionNode.addAspect(VariableBuilder.class, new InnerViewVariableBuilder(annotation, activityInjectionNode.getInjectionNode(), codeModel.parseType(astType.getName())));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }

    public class InnerViewVariableBuilder implements VariableBuilder {

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

        @Override
        public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context) {
            return null;
        }
    }
}

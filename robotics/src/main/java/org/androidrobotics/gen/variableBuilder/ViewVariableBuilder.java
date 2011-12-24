package org.androidrobotics.gen.variableBuilder;

import android.app.Activity;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.Analyzer;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ViewVariableBuilder implements InjectionNodeBuilder {


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
}

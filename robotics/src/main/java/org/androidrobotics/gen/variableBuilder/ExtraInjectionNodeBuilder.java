package org.androidrobotics.gen.variableBuilder;

import android.app.Activity;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ExtraInjectionNodeBuilder implements InjectionNodeBuilder {

    private JCodeModel codeModel;
    private ASTClassFactory astClassFactory;
    private InjectionPointFactory injectionPointFactory;

    @Inject
    public ExtraInjectionNodeBuilder(JCodeModel codeModel, ASTClassFactory astClassFactory, InjectionPointFactory injectionPointFactory) {
        this.codeModel = codeModel;
        this.astClassFactory = astClassFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        String extraId = annotation.getProperty("value", String.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        ASTType activityType = astClassFactory.buildASTClassType(Activity.class);
        InjectionNode activityInjectionNode = injectionPointFactory.buildInjectionNode(activityType, context);

        try {
            injectionNode.addAspect(VariableBuilder.class, new ExtraValuableBuilder(extraId, activityInjectionNode, codeModel.parseType(astType.getName())));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}

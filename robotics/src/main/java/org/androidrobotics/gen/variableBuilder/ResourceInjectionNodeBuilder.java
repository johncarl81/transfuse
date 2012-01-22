package org.androidrobotics.gen.variableBuilder;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.RoboticsAnalysisException;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidrobotics.gen.variableBuilder.resource.ResourceExpressionBuilderFactory;
import org.androidrobotics.model.IdentifiedInjectionNode;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ResourceInjectionNodeBuilder implements InjectionNodeBuilder {

    private JCodeModel codeModel;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ResourceExpressionBuilderFactory resourceExpressionBuilderFactory;

    @Inject
    public ResourceInjectionNodeBuilder(JCodeModel codeModel, VariableInjectionBuilderFactory variableInjectionBuilderFactory, ResourceExpressionBuilderFactory resourceExpressionBuilderFactory) {
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.resourceExpressionBuilderFactory = resourceExpressionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        Integer resourceId = annotation.getProperty("value", Integer.class);

        InjectionNode injectionNode = new IdentifiedInjectionNode(astType, resourceId);

        try {
            JType resourceType = codeModel.parseType(astType.getName());

            ResourceExpressionBuilder resourceExpressionBuilder =
                    resourceExpressionBuilderFactory.buildResourceExpressionBuilder(resourceType, context);

            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildResourceVariableBuilder(resourceId, resourceExpressionBuilder));
        } catch (ClassNotFoundException e) {
            throw new RoboticsAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }
}

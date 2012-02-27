package org.androidtransfuse.gen.variableBuilder;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.TransfuseAnalysisException;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.annotations.Resource;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilderFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class ResourceInjectionNodeBuilder extends InjectionNodeBuilderSingleAnnotationAdapter<Resource> {

    private JCodeModel codeModel;
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;
    private ResourceExpressionBuilderFactory resourceExpressionBuilderFactory;

    @Inject
    public ResourceInjectionNodeBuilder(JCodeModel codeModel, VariableInjectionBuilderFactory variableInjectionBuilderFactory, ResourceExpressionBuilderFactory resourceExpressionBuilderFactory) {
        super(Resource.class);
        this.codeModel = codeModel;
        this.variableInjectionBuilderFactory = variableInjectionBuilderFactory;
        this.resourceExpressionBuilderFactory = resourceExpressionBuilderFactory;
    }

    @Override
    public InjectionNode buildInjectionNode(ASTType astType, AnalysisContext context, ASTAnnotation annotation) {
        Integer resourceId = annotation.getProperty("value", Integer.class);

        InjectionNode injectionNode = new InjectionNode(astType);

        try {
            JType resourceType = codeModel.parseType(astType.getName());

            ResourceExpressionBuilder resourceExpressionBuilder =
                    resourceExpressionBuilderFactory.buildResourceExpressionBuilder(resourceType, context);

            injectionNode.addAspect(VariableBuilder.class,
                    variableInjectionBuilderFactory.buildResourceVariableBuilder(resourceId, resourceExpressionBuilder));
        } catch (ClassNotFoundException e) {
            throw new TransfuseAnalysisException("Unable to parse type " + astType.getName(), e);
        }

        return injectionNode;
    }


}

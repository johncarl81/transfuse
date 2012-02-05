package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ResourceVariableBuilder implements VariableBuilder {

    private int resourceId;
    private ResourceExpressionBuilder resourceExpressionBuilder;
    private JCodeModel codeModel;

    @Inject
    public ResourceVariableBuilder(@Assisted int resourceId,
                                   @Assisted ResourceExpressionBuilder resourceExpressionBuilder,
                                   JCodeModel codeModel) {
        this.resourceId = resourceId;
        this.resourceExpressionBuilder = resourceExpressionBuilder;
        this.codeModel = codeModel;
    }

    @Override
    public JExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ResourceIdentifier viewResourceIdentifier = injectionBuilderContext.getRResource().getResourceIdentifier(resourceId);

        ASTType rInnerType = viewResourceIdentifier.getRInnerType();

        JClass rInnerRef = codeModel.ref(rInnerType.getName());

        JFieldRef resoureceIdRef = rInnerRef.staticRef(viewResourceIdentifier.getName());

        return resourceExpressionBuilder.buildExpression(injectionBuilderContext, resoureceIdRef);
    }
}
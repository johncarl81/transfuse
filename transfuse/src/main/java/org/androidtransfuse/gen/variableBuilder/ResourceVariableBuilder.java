package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.TypedExpression;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ResourceVariableBuilder implements VariableBuilder {

    private int resourceId;
    private ResourceExpressionBuilder resourceExpressionBuilder;
    private RResourceReferenceBuilder rResourceReferenceBuilder;

    @Inject
    public ResourceVariableBuilder(@Assisted int resourceId,
                                   @Assisted ResourceExpressionBuilder resourceExpressionBuilder,
                                   RResourceReferenceBuilder rResourceReferenceBuilder) {
        this.resourceId = resourceId;
        this.resourceExpressionBuilder = resourceExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ResourceIdentifier resourceIdentifier = injectionBuilderContext.getRResource().getResourceIdentifier(resourceId);

        return resourceExpressionBuilder.buildExpression(injectionBuilderContext, rResourceReferenceBuilder.buildReference(resourceIdentifier));
    }
}
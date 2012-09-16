package org.androidtransfuse.gen.variableBuilder;

import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.gen.InjectionBuilderContext;
import org.androidtransfuse.gen.variableBuilder.resource.ResourceExpressionBuilder;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.TypedExpression;
import org.androidtransfuse.model.r.RResource;
import org.androidtransfuse.model.r.RResourceReferenceBuilder;
import org.androidtransfuse.model.r.ResourceIdentifier;

import javax.inject.Inject;

public class ResourceVariableBuilder implements VariableBuilder {

    private final int resourceId;
    private final ResourceExpressionBuilder resourceExpressionBuilder;
    private final RResourceReferenceBuilder rResourceReferenceBuilder;
    private final RResource rResource;

    @Inject
    public ResourceVariableBuilder(@Assisted int resourceId,
                                   @Assisted ResourceExpressionBuilder resourceExpressionBuilder,
                                   RResourceReferenceBuilder rResourceReferenceBuilder, RResource rResource) {
        this.resourceId = resourceId;
        this.resourceExpressionBuilder = resourceExpressionBuilder;
        this.rResourceReferenceBuilder = rResourceReferenceBuilder;
        this.rResource = rResource;
    }

    @Override
    public TypedExpression buildVariable(InjectionBuilderContext injectionBuilderContext, InjectionNode injectionNode) {
        ResourceIdentifier resourceIdentifier = rResource.getResourceIdentifier(resourceId);

        return resourceExpressionBuilder.buildExpression(injectionBuilderContext, rResourceReferenceBuilder.buildReference(resourceIdentifier));
    }
}
/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
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
package org.androidtransfuse.gen.variableBuilder.resource;

import android.content.res.Resources;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MethodBasedResourceExpressionBuilderAdaptor implements ResourceExpressionBuilderAdaptor {

    private final Class returnType;
    private final String accessMethod;
    private final MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public MethodBasedResourceExpressionBuilderAdaptor(@Assisted Class returnType,
                                                       @Assisted String accessMethod,
                                                       MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory,
                                                       InjectionPointFactory injectionPointFactory) {
        this.accessMethod = accessMethod;
        this.returnType = returnType;
        this.methodBasedResourceExpressionBuilderFactory = methodBasedResourceExpressionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(AnalysisContext context) {

        InjectionNode resourceInjectionNode = injectionPointFactory.buildInjectionNode(Resources.class, context);

        return methodBasedResourceExpressionBuilderFactory.buildMethodBasedResourceExpressionBuilder(returnType, accessMethod, resourceInjectionNode);
    }
}

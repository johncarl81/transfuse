package org.androidtransfuse.gen.variableBuilder.resource;

import android.app.Application;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilderAdaptor implements ResourceExpressionBuilderAdaptor {

    private final MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory;
    private final InjectionPointFactory injectionPointFactory;

    @Inject
    public AnimationResourceExpressionBuilderAdaptor(MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory,
                                                     InjectionPointFactory injectionPointFactory) {
        this.methodBasedResourceExpressionBuilderFactory = methodBasedResourceExpressionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(AnalysisContext context) {

        InjectionNode applicationInjectionNode = injectionPointFactory.buildInjectionNode(Application.class, context);

        return methodBasedResourceExpressionBuilderFactory.buildAnimationResourceExpressionBuilder(applicationInjectionNode);
    }
}

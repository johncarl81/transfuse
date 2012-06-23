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

    private Class returnType;
    private String accessMethod;
    private MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;

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

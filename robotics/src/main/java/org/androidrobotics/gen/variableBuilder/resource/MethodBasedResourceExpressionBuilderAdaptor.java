package org.androidrobotics.gen.variableBuilder.resource;

import android.content.res.Resources;
import com.google.inject.assistedinject.Assisted;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class MethodBasedResourceExpressionBuilderAdaptor implements ResourceExpressionBuilderAdaptor {

    private String accessMethod;
    private MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;
    private ASTClassFactory astClassFactory;

    @Inject
    public MethodBasedResourceExpressionBuilderAdaptor(@Assisted String accessMethod,
                                                       MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory,
                                                       InjectionPointFactory injectionPointFactory,
                                                       ASTClassFactory astClassFactory) {
        this.accessMethod = accessMethod;
        this.methodBasedResourceExpressionBuilderFactory = methodBasedResourceExpressionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.astClassFactory = astClassFactory;
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(AnalysisContext context) {

        ASTType resourceAstType = astClassFactory.buildASTClassType(Resources.class);
        InjectionNode resourceInjectionNode = injectionPointFactory.buildInjectionNode(resourceAstType, context);

        return methodBasedResourceExpressionBuilderFactory.buildMethodBasedResourceExpressionBuilder(accessMethod, resourceInjectionNode);
    }
}

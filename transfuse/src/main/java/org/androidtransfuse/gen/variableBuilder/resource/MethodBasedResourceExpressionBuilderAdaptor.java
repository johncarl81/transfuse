package org.androidtransfuse.gen.variableBuilder.resource;

import android.content.res.Resources;
import com.google.inject.assistedinject.Assisted;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
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
    private ASTClassFactory astClassFactory;

    @Inject
    public MethodBasedResourceExpressionBuilderAdaptor(@Assisted Class returnType,
                                                       @Assisted String accessMethod,
                                                       MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory,
                                                       InjectionPointFactory injectionPointFactory,
                                                       ASTClassFactory astClassFactory) {
        this.accessMethod = accessMethod;
        this.returnType = returnType;
        this.methodBasedResourceExpressionBuilderFactory = methodBasedResourceExpressionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.astClassFactory = astClassFactory;
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(AnalysisContext context) {

        ASTType resourceAstType = astClassFactory.buildASTClassType(Resources.class);
        InjectionNode resourceInjectionNode = injectionPointFactory.buildInjectionNode(resourceAstType, context);

        return methodBasedResourceExpressionBuilderFactory.buildMethodBasedResourceExpressionBuilder(returnType, accessMethod, resourceInjectionNode);
    }
}

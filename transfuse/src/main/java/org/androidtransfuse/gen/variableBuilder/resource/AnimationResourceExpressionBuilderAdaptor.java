package org.androidtransfuse.gen.variableBuilder.resource;

import android.app.Application;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AnimationResourceExpressionBuilderAdaptor implements ResourceExpressionBuilderAdaptor {

    private MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory;
    private InjectionPointFactory injectionPointFactory;
    private ASTClassFactory astClassFactory;

    @Inject
    public AnimationResourceExpressionBuilderAdaptor(MethodBasedResourceExpressionBuilderFactory methodBasedResourceExpressionBuilderFactory, InjectionPointFactory injectionPointFactory, ASTClassFactory astClassFactory) {
        this.methodBasedResourceExpressionBuilderFactory = methodBasedResourceExpressionBuilderFactory;
        this.injectionPointFactory = injectionPointFactory;
        this.astClassFactory = astClassFactory;
    }

    public ResourceExpressionBuilder buildResourceExpressionBuilder(AnalysisContext context) {

        ASTType applicationAstType = astClassFactory.buildASTClassType(Application.class);
        InjectionNode applicationInjectionNode = injectionPointFactory.buildInjectionNode(applicationAstType, context);

        return methodBasedResourceExpressionBuilderFactory.buildAnimationResourceExpressionBuilder(applicationInjectionNode);
    }
}

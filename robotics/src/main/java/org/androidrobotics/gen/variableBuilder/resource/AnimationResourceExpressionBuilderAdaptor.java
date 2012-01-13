package org.androidrobotics.gen.variableBuilder.resource;

import android.app.Application;
import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

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

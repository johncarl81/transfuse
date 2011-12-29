package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InjectionPointFactory;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class AOPProxyAnalyzer extends ASTAnalysisAdaptor {

    private InjectionPointFactory injectionPointFactory;

    @Inject
    public AOPProxyAnalyzer(InjectionPointFactory injectionPointFactory) {
        this.injectionPointFactory = injectionPointFactory;
    }

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        for (ASTAnnotation methodAnnotation : astMethod.getAnnotations()) {
            if (context.getAopRepository().isInterceptor(methodAnnotation.getName())) {
                addInterceptor(injectionNode, astMethod, getInterceptorInjectionNode(methodAnnotation.getName(), context));
            }
        }
    }

    private InjectionNode getInterceptorInjectionNode(String methodAnnotation, AnalysisContext context) {
        ASTType interceptorType = context.getAopRepository().getInterceptor(methodAnnotation);

        return injectionPointFactory.buildInjectionPoint(interceptorType, context).getInjectionNode();
    }

    private void addInterceptor(InjectionNode injectionNode, ASTMethod astMethod, InjectionNode interceptor) {
        if (!injectionNode.containsAspect(AOPProxyAspect.class)) {
            injectionNode.addAspect(new AOPProxyAspect());
        }

        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);

        aopProxyAspect.addInterceptor(astMethod, interceptor);
    }
}

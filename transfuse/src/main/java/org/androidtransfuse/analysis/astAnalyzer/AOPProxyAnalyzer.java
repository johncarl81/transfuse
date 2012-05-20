package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.adapter.ASTAnnotation;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;

import javax.inject.Inject;

/**
 * Analyzer to add Aspect Oriented Programming method interceptors from the AOP Repository to the appropriately
 * annotated methods.
 *
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
        //AOP is only available on top level
        if (context.getSuperClassLevel() == 0) {
            for (ASTAnnotation methodAnnotation : astMethod.getAnnotations()) {
                if (context.getAOPRepository().isInterceptor(methodAnnotation)) {
                    addInterceptor(injectionNode, astMethod, getInterceptorInjectionNode(methodAnnotation.getName(), context));
                }
            }
        }
    }

    private InjectionNode getInterceptorInjectionNode(String methodAnnotation, AnalysisContext context) {
        ASTType interceptorType = context.getAOPRepository().getInterceptor(methodAnnotation);

        return injectionPointFactory.buildInjectionNode(interceptorType, context);
    }

    private void addInterceptor(InjectionNode injectionNode, ASTMethod astMethod, InjectionNode interceptor) {
        if (!injectionNode.containsAspect(AOPProxyAspect.class)) {
            injectionNode.addAspect(new AOPProxyAspect());
        }

        AOPProxyAspect aopProxyAspect = injectionNode.getAspect(AOPProxyAspect.class);

        aopProxyAspect.addInterceptor(astMethod, interceptor);
    }
}

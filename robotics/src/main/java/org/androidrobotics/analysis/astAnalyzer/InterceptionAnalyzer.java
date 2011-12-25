package org.androidrobotics.analysis.astAnalyzer;

import org.androidrobotics.analysis.AnalysisContext;
import org.androidrobotics.analysis.InterceptorRepository;
import org.androidrobotics.analysis.adapter.ASTAnnotation;
import org.androidrobotics.analysis.adapter.ASTMethod;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.model.InjectionNode;

import java.util.Set;

/**
 * @author John Ericksen
 */
public class InterceptionAnalyzer extends ASTAnalysisAdaptor {

    @Override
    public void analyzeMethod(InjectionNode injectionNode, ASTMethod astMethod, AnalysisContext context) {
        InterceptorRepository interceptorRepository = context.getInterceptorRepository();
        Set<String> interceptorAnnotationTypes = interceptorRepository.getInterceptorAnnotationTypes();

        for (String interceptorAnnotationType : interceptorAnnotationTypes) {
            for (ASTAnnotation astAnnotation : astMethod.getAnnotations()) {
                if (interceptorAnnotationType.equals(astAnnotation.getName())) {
                    addInterception(injectionNode, astMethod, interceptorRepository.getInterceptorType(interceptorAnnotationType));
                }
            }
        }
    }

    private void addInterception(InjectionNode injectionNode, ASTMethod astMethod, ASTType interceptorType) {
        if (!injectionNode.containsAspect(ProxyAspect.class)) {
            injectionNode.addAspect(new ProxyAspect());
        }
        ProxyAspect proxyAspect = injectionNode.getAspect(ProxyAspect.class);

        proxyAspect.addInterceptor(astMethod, interceptorType);

    }
}

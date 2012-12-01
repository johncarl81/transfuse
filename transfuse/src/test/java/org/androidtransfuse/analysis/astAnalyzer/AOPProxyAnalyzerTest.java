package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTMethod;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.model.InjectionNode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class AOPProxyAnalyzerTest {

    @Inject
    private AOPProxyAnalyzer proxyAnalyzer;
    @Inject
    private InjectionPointFactory injectionPointFactory;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private SimpleAnalysisContextFactory simpleAnalysisContextFactory;
    private InjectionNode proxyTargetInjectionNode;
    private ASTType methodInterceptorASTType;
    private ASTType proxyTargetASTType;
    private ASTType aopAnnotationASTType;


    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AOPAnnotation {
    }

    public class AOPAnnotationMethodInterceptor implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) {
            return null;
        }
    }

    public class AOPProxyTarget {
        @AOPAnnotation
        public void annotatedMethod() {
        }
    }

    @Before
    public void setup() {
        TransfuseTestInjector.inject(this);

        methodInterceptorASTType = astClassFactory.getType(AOPAnnotationMethodInterceptor.class);
        aopAnnotationASTType = astClassFactory.getType(AOPAnnotation.class);
        proxyTargetASTType = astClassFactory.getType(AOPProxyTarget.class);
        proxyTargetInjectionNode = injectionPointFactory.buildInjectionNode(proxyTargetASTType, simpleAnalysisContextFactory.buildContext());
    }

    @Test
    public void testAnalysis() {
        AnalysisContext analysisContext = simpleAnalysisContextFactory.buildContext();
        analysisContext.getAOPRepository().put(aopAnnotationASTType, methodInterceptorASTType);

        for (ASTMethod astMethod : proxyTargetASTType.getMethods()) {
            proxyAnalyzer.analyzeMethod(proxyTargetInjectionNode, proxyTargetASTType, astMethod, analysisContext);
        }

        assertTrue(proxyTargetInjectionNode.containsAspect(AOPProxyAspect.class));
        AOPProxyAspect aspect = proxyTargetInjectionNode.getAspect(AOPProxyAspect.class);
        for (ASTMethod astMethod : proxyTargetASTType.getMethods()) {
            assertTrue(aspect.getMethodInterceptors().containsKey(astMethod));
            Set<InjectionNode> interceptorInjectionNodes = aspect.getMethodInterceptors().get(astMethod);
            for (InjectionNode interceptorInjectionNode : interceptorInjectionNodes) {
                assertEquals(methodInterceptorASTType, interceptorInjectionNode.getASTType());
            }
        }
    }
}

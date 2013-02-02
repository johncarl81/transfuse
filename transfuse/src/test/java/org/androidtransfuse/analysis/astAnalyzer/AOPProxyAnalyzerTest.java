/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.analysis.astAnalyzer;

import org.androidtransfuse.adapter.ASTMethod;
import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.AnalysisContext;
import org.androidtransfuse.analysis.InjectionPointFactory;
import org.androidtransfuse.analysis.SimpleAnalysisContextFactory;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
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
@Bootstrap(test = true)
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
        Bootstraps.injectTest(this);

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

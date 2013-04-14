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
package org.androidtransfuse.analysis;

import org.androidtransfuse.adapter.ASTType;
import org.androidtransfuse.adapter.classes.ASTClassFactory;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.androidtransfuse.gen.variableBuilder.VariableBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilder;
import org.androidtransfuse.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidtransfuse.model.ConstructorInjectionPoint;
import org.androidtransfuse.model.FieldInjectionPoint;
import org.androidtransfuse.model.InjectionNode;
import org.androidtransfuse.model.MethodInjectionPoint;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@Bootstrap
public class AnalyzerTest {

    //A -> E -> F
    // |
    // `-> B -> C -> D
    //      |
    //      `-> F

    public static interface B {
    }

    public static class A {
        private B b;
        private E e;

        @Inject
        public void setMultiVariables(B b, E e) {
            this.b = b;
            this.e = e;
        }
    }

    public static class BImpl implements B {
        @Inject
        private C c;
        private F f;

        @Inject
        public BImpl(F f) {
            this.f = f;
        }

    }

    public static class C {
        @Inject
        private D d;
    }

    public static class D {
        //back link
        private B b;

        @Inject
        public D(B b) {
            this.b = b;
        }
    }

    public static class E {
        private F f;

        @Inject
        public E(F f) {
            this.f = f;
        }
    }

    public static class F {
        //empty
    }

    @Inject
    private Analyzer analyzer;
    @Inject
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    @Inject
    private ASTClassFactory astClassFactory;
    @Inject
    private SimpleAnalysisContextFactory analysisContextFactory;
    private AnalysisContext analysisContext;
    @Inject
    private VariableInjectionBuilderFactory variableInjectionBuilderFactory;

    @Before
    public void setup() {
        Bootstraps.inject(this);

        analysisContext = analysisContextFactory.buildContext();


        analysisContext.getInjectionNodeBuilders().putType(B.class,
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(astClassFactory.getType(BImpl.class)));
    }

    @Test
    public void testBackLinkAnalysis() {
        ASTType astType = astClassFactory.getType(A.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);
        injectionNode.addAspect(VariableBuilder.class, variableInjectionBuilderProvider.get());

        //A -> B && A -> E
        assertEquals(1, injectionNode.getAspect(ASTInjectionAspect.class).getMethodInjectionPoints().size());
        MethodInjectionPoint bInjectionPoint = injectionNode.getAspect(ASTInjectionAspect.class).getMethodInjectionPoints().iterator().next();

        assertEquals(2, bInjectionPoint.getInjectionNodes().size());
        //A -> B
        InjectionNode bInjectionNode = bInjectionPoint.getInjectionNodes().get(0);
        assertTrue(isProxyRequired(bInjectionNode));
        assertEquals(BImpl.class.getCanonicalName(), bInjectionNode.getClassName());

        //A -> E
        InjectionNode eInjectionNode = bInjectionPoint.getInjectionNodes().get(1);
        assertFalse(isProxyRequired(eInjectionNode));
        assertEquals(E.class.getCanonicalName(), eInjectionNode.getClassName());

        //B -> C
        assertEquals(1, bInjectionNode.getAspect(ASTInjectionAspect.class).getFieldInjectionPoints().size());
        FieldInjectionPoint cInjectionPoint = bInjectionNode.getAspect(ASTInjectionAspect.class).getFieldInjectionPoints().iterator().next();
        InjectionNode cInjectionNode = cInjectionPoint.getInjectionNode();
        assertFalse(isProxyRequired(cInjectionNode));
        assertEquals(C.class.getCanonicalName(), cInjectionNode.getClassName());

        //B -> F
        ConstructorInjectionPoint fNonBackLinkInjectionPoint = bInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, fNonBackLinkInjectionPoint.getInjectionNodes().size());
        InjectionNode fInjectionNode = fNonBackLinkInjectionPoint.getInjectionNodes().get(0);
        assertFalse(isProxyRequired(fInjectionNode));
        assertEquals(F.class.getCanonicalName(), fInjectionNode.getClassName());

        //E -> F
        ConstructorInjectionPoint fNonBackLinkInjectionPoint2 = eInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, fNonBackLinkInjectionPoint2.getInjectionNodes().size());
        InjectionNode fInjectionNode2 = fNonBackLinkInjectionPoint2.getInjectionNodes().get(0);
        assertFalse(isProxyRequired(fInjectionNode2));

        //C -> D
        assertEquals(1, cInjectionNode.getAspect(ASTInjectionAspect.class).getFieldInjectionPoints().size());
        FieldInjectionPoint dInjectionPoint = cInjectionNode.getAspect(ASTInjectionAspect.class).getFieldInjectionPoints().iterator().next();
        InjectionNode dInjectionNode = dInjectionPoint.getInjectionNode();
        assertFalse(isProxyRequired(dInjectionNode));
        assertEquals(D.class.getCanonicalName(), dInjectionNode.getClassName());

        //D -> B back link
        ConstructorInjectionPoint bBackLinkInjectionPoint = dInjectionNode.getAspect(ASTInjectionAspect.class).getConstructorInjectionPoint();
        assertEquals(1, bBackLinkInjectionPoint.getInjectionNodes().size());
        InjectionNode bBackLinkInjectionNode = bBackLinkInjectionPoint.getInjectionNodes().get(0);
        assertEquals(BImpl.class.getCanonicalName(), bBackLinkInjectionNode.getClassName());
        assertTrue(isProxyRequired(bBackLinkInjectionNode));

        //B -> F and E -> F difference
        assertNotSame(fInjectionNode, fInjectionNode2);
        assertFalse(fInjectionNode.equals(fInjectionNode2));
    }

    private boolean isProxyRequired(InjectionNode injectionNode) {
        VirtualProxyAspect proxyAspect = injectionNode.getAspect(VirtualProxyAspect.class);

        return proxyAspect != null && proxyAspect.isProxyRequired();
    }
}

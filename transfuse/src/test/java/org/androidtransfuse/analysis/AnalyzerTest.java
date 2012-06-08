package org.androidtransfuse.analysis;

import com.google.inject.Injector;
import org.androidtransfuse.TransfuseTestInjector;
import org.androidtransfuse.analysis.adapter.ASTClassFactory;
import org.androidtransfuse.analysis.adapter.ASTType;
import org.androidtransfuse.analysis.astAnalyzer.ASTInjectionAspect;
import org.androidtransfuse.analysis.astAnalyzer.VirtualProxyAspect;
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

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
public class AnalyzerTest {

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

    private Analyzer analyzer;
    private Provider<VariableInjectionBuilder> variableInjectionBuilderProvider;
    private ASTClassFactory astClassFactory;
    private AnalysisContext analysisContext;

    @Before
    public void setup() {
        Injector injector = TransfuseTestInjector.getInjector(this);

        VariableInjectionBuilderFactory variableInjectionBuilderFactory = injector.getInstance(VariableInjectionBuilderFactory.class);

        analyzer = injector.getInstance(Analyzer.class);
        astClassFactory = injector.getInstance(ASTClassFactory.class);

        analysisContext = injector.getInstance(SimpleAnalysisContextFactory.class).buildContext();

        variableInjectionBuilderProvider = injector.getProvider(VariableInjectionBuilder.class);

        analysisContext.getInjectionNodeBuilders().put(B.class.getCanonicalName(),
                variableInjectionBuilderFactory.buildVariableInjectionNodeBuilder(astClassFactory.buildASTClassType(BImpl.class)));
    }

    @Test
    public void testBackLinkAnalysis() {
        ASTType astType = astClassFactory.buildASTClassType(A.class);

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

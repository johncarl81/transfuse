package org.androidrobotics.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.gen.variableBuilder.VariableInjectionBuilderFactory;
import org.androidrobotics.model.ConstructorInjectionPoint;
import org.androidrobotics.model.FieldInjectionPoint;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.model.MethodInjectionPoint;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
    private ASTClassFactory astClassFactory;
    private AnalysisContext analysisContext;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));

        VariableInjectionBuilderFactory variableInjectionBuilderFactory = injector.getInstance(VariableInjectionBuilderFactory.class);

        analyzer = injector.getInstance(Analyzer.class);
        astClassFactory = injector.getInstance(ASTClassFactory.class);

        analysisContext = injector.getInstance(SimpleAnalysisContextFactory.class).buildContext();

        analysisContext.getVariableBuilders().put(B.class.getName(), variableInjectionBuilderFactory.buildVariableInjectionBuilder(BImpl.class));
    }

    @Test
    public void testBackLinkAnalysis() {
        ASTType astType = astClassFactory.buildASTClassType(A.class);

        InjectionNode injectionNode = analyzer.analyze(astType, astType, analysisContext);

        //A -> B && A -> E
        assertEquals(1, injectionNode.getMethodInjectionPoints().size());
        MethodInjectionPoint bInjectionPoint = injectionNode.getMethodInjectionPoints().iterator().next();

        assertEquals(2, bInjectionPoint.getInjectionNodes().size());
        //A -> B
        InjectionNode bInjectionNode = bInjectionPoint.getInjectionNodes().get(0);
        assertTrue(bInjectionNode.isProxyRequired());
        assertEquals(BImpl.class.getName(), bInjectionNode.getClassName());

        //A -> E
        InjectionNode eInjectionNode = bInjectionPoint.getInjectionNodes().get(1);
        assertFalse(eInjectionNode.isProxyRequired());
        assertEquals(E.class.getName(), eInjectionNode.getClassName());

        //B -> C
        assertEquals(1, bInjectionNode.getFieldInjectionPoints().size());
        FieldInjectionPoint cInjectionPoint = bInjectionNode.getFieldInjectionPoints().iterator().next();
        InjectionNode cInjectionNode = cInjectionPoint.getInjectionNode();
        assertFalse(cInjectionNode.isProxyRequired());
        assertEquals(C.class.getName(), cInjectionNode.getClassName());

        //B -> F
        ConstructorInjectionPoint fNonBackLinkInjectionPoint = bInjectionNode.getConstructorInjectionPoint();
        assertEquals(1, fNonBackLinkInjectionPoint.getInjectionNodes().size());
        InjectionNode fInjectionNode = fNonBackLinkInjectionPoint.getInjectionNodes().get(0);
        assertFalse(fInjectionNode.isProxyRequired());
        assertEquals(F.class.getName(), fInjectionNode.getClassName());

        //E -> F
        ConstructorInjectionPoint fNonBackLinkInjectionPoint2 = eInjectionNode.getConstructorInjectionPoint();
        assertEquals(1, fNonBackLinkInjectionPoint2.getInjectionNodes().size());
        InjectionNode fInjectionNode2 = fNonBackLinkInjectionPoint2.getInjectionNodes().get(0);
        assertFalse(fInjectionNode2.isProxyRequired());

        //C -> D
        assertEquals(1, cInjectionNode.getFieldInjectionPoints().size());
        FieldInjectionPoint dInjectionPoint = cInjectionNode.getFieldInjectionPoints().iterator().next();
        InjectionNode dInjectionNode = dInjectionPoint.getInjectionNode();
        assertFalse(dInjectionNode.isProxyRequired());
        assertEquals(D.class.getName(), dInjectionNode.getClassName());

        //D -> B back link
        ConstructorInjectionPoint bBackLinkInjectionPoint = dInjectionNode.getConstructorInjectionPoint();
        assertEquals(1, bBackLinkInjectionPoint.getInjectionNodes().size());
        InjectionNode bBackLinkInjectionNode = bBackLinkInjectionPoint.getInjectionNodes().get(0);
        assertEquals(BImpl.class.getName(), bBackLinkInjectionNode.getClassName());
        assertTrue(bBackLinkInjectionNode.isProxyRequired());
    }
}

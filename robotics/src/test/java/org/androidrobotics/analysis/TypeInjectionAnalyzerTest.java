package org.androidrobotics.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
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

/**
 * @author John Ericksen
 */
public class TypeInjectionAnalyzerTest {

    public static class A {
        private B b;

        @Inject
        public void setB(B b) {
            this.b = b;
        }
    }

    public static class B {
        @Inject
        private C c;

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

    private TypeInjectionAnalyzer typeInjectionAnalyzer;
    private ASTClassFactory astClassFactory;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new RoboticsGenerationGuiceModule(new JavaUtilLogger(this)));

        typeInjectionAnalyzer = injector.getInstance(TypeInjectionAnalyzer.class);
        astClassFactory = injector.getInstance(ASTClassFactory.class);
    }

    @Test
    public void testBackLinkAnalysis() {
        ASTType astType = astClassFactory.buildASTClassType(A.class);

        InjectionNode injectionNode = typeInjectionAnalyzer.analyze(astType);

        MethodInjectionPoint bInjectionPoint = injectionNode.getMethodInjectionPoints().iterator().next();

        InjectionNode bInjectionNode = bInjectionPoint.getInjectionNodes().get(0);
        assertEquals(B.class.getName(), bInjectionNode.getClassName());

        FieldInjectionPoint cInjectionPoint = bInjectionNode.getFieldInjectionPoints().iterator().next();

        InjectionNode cInjectionNode = cInjectionPoint.getInjectionNode();
        assertEquals(C.class.getName(), cInjectionNode.getClassName());

        FieldInjectionPoint dInjectionPoint = cInjectionNode.getFieldInjectionPoints().iterator().next();

        InjectionNode dInjectionNode = dInjectionPoint.getInjectionNode();
        assertEquals(D.class.getName(), dInjectionNode.getClassName());

        ConstructorInjectionPoint bBackLinkInjectionPoint = dInjectionNode.getConstructorInjectionPoint();

        InjectionNode bBackLinkInjectionNode = bBackLinkInjectionPoint.getInjectionNodes().get(0);
        assertEquals(B.class.getName(), bBackLinkInjectionNode.getClassName());
        assertTrue(bBackLinkInjectionNode.isProxyRequired());
    }
}

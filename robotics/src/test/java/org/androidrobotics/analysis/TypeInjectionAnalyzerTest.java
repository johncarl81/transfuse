package org.androidrobotics.analysis;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.androidrobotics.analysis.adapter.ASTClassFactory;
import org.androidrobotics.analysis.adapter.ASTType;
import org.androidrobotics.config.RoboticsGenerationGuiceModule;
import org.androidrobotics.model.InjectionNode;
import org.androidrobotics.util.JavaUtilLogger;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class TypeInjectionAnalyzerTest {

    private class A {
        @Inject
        private B b;
    }

    private class B {
        @Inject
        private C c;
    }

    private class C {
        @Inject
        private D d;
    }

    private class D {
        //back link
        @Inject
        private B b;
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
    }
}

package org.androidrobotics.gen;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ClassAnalysisBridge;
import org.androidrobotics.config.RoboticsModule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityGeneratorTest {

    private static final String TEST_NAME = "TestName";
    private static final String DELEGATE_TEST_NAME = DelegateTestClass.class.getName();
    private static final int TEST_LAYOUT_ID = 123456;

    private ActivityGenerator generator;
    private JCodeModel codeModel;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new RoboticsModule());

        codeModel = injector.getInstance(JCodeModel.class);
        generator = injector.getInstance(ActivityGenerator.class);
    }

    @Test
    public void testActivityGeneration() throws NoSuchFieldException {
        ActivityDescriptor descriptor = new ActivityDescriptor();
        descriptor.setName(TEST_NAME);
        descriptor.setLayout(TEST_LAYOUT_ID);
        descriptor.setDelegateClass(DELEGATE_TEST_NAME);
        descriptor.setDelegateAnalysis(new ClassAnalysisBridge(DelegateTestClass.class));

        try {
            generator.generate(descriptor);

            assertEquals(3, codeModel.countArtifacts());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class DelegateTestClass {
        private String test;
    }
}

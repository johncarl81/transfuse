package org.androidrobotics.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.androidrobotics.analysis.ParameterAnalysisBridge;
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
        codeModel = new JCodeModel();
        InjectionGenerator injectionGenerator = new InjectionGenerator(codeModel);
        generator = new ActivityGenerator(codeModel, injectionGenerator);

    }

    @Test
    public void testActivityGeneration() throws NoSuchFieldException {
        ActivityDescriptor descriptor = new ActivityDescriptor();
        descriptor.setName(TEST_NAME);
        descriptor.setLayout(TEST_LAYOUT_ID);
        descriptor.setDelegateClass(DELEGATE_TEST_NAME);
        descriptor.addInjectionPoint(new ParameterAnalysisBridge(DelegateTestClass.class.getDeclaredField("test")));

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
    }
}

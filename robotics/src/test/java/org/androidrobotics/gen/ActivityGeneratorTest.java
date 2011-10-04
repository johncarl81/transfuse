package org.androidrobotics.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
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
    private InjectionGenerator injector;

    @Before
    public void setup() {
        codeModel = new JCodeModel();
        generator = new ActivityGenerator(codeModel);
        injector = new InjectionGenerator(codeModel);
    }

    @Test
    public void testActivityGeneration() {
        ActivityDescriptor descriptor = new ActivityDescriptor();
        descriptor.setName(TEST_NAME);
        descriptor.setLayout(TEST_LAYOUT_ID);
        descriptor.setDelegateClass(DELEGATE_TEST_NAME);

        try {
            JDefinedClass injectorDefinedClass = injector.buildInjector(descriptor);
            generator.generate(descriptor, injectorDefinedClass);

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

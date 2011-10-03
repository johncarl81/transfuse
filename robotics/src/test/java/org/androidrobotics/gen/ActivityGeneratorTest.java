package org.androidrobotics.gen;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityGeneratorTest {

    private static final String TEST_NAME = "TestName";
    private static final int TEST_LAYOUT_ID = 123456;

    private ActivityGenerator generator;
    private JCodeModel codeModel;

    @Before
    public void setup() {
        codeModel = new JCodeModel();
        generator = new ActivityGenerator(codeModel);
    }

    @Test
    public void testActivityGeneration() {
        ActivityDescriptor descriptor = new ActivityDescriptor();
        descriptor.setName(TEST_NAME);
        descriptor.setLayout(TEST_LAYOUT_ID);

        try {
            generator.generate(descriptor);

            assertEquals(1, codeModel.countArtifacts());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JClassAlreadyExistsException e) {
            e.printStackTrace();
        }


    }
}

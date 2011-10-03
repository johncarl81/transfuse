package org.androidrobotics.analysis;

import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.gen.ActivityDescriptor;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class ActivityAnalysisTest {

    public static final String TEST_NAME = "TestName";
    public static final int TEST_LAYOUT_ID = 123456;

    private ActivityAnalysis activityAnalysis;

    @Before
    public void setup() {
        activityAnalysis = new ActivityAnalysis();
    }

    @Test
    public void testActivityAnnotation() {
        ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(TestActivity.class);

        assertEquals(TEST_NAME, activityDescriptor.getName());
    }

    @Test
    public void testLayoutAnnotation() {
        ActivityDescriptor activityDescriptor = activityAnalysis.analyzeElement(TestActivity.class);

        assertEquals(TEST_LAYOUT_ID, activityDescriptor.getLayout());
    }

    @Activity(ActivityAnalysisTest.TEST_NAME)
    @Layout(ActivityAnalysisTest.TEST_LAYOUT_ID)
    public class TestActivity {
    }
}

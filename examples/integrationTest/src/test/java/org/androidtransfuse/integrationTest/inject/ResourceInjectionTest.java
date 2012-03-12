package org.androidtransfuse.integrationTest.inject;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ResourceInjectionTest {

    private static final String APP_NAME = "Integration Test";

    private ResourceInjection resourceInjection;

    @Before
    public void setup() {
        ResourceInjectionActivity resourceInjectionActivity = new ResourceInjectionActivity();
        resourceInjectionActivity.onCreate(null);

        resourceInjection = DelegateUtil.getDelegate(resourceInjectionActivity, ResourceInjection.class);
    }

    @Test
    public void testString() {
        assertNotNull(resourceInjection.getAppName());
        assertEquals(APP_NAME, resourceInjection.getAppName());
    }
}

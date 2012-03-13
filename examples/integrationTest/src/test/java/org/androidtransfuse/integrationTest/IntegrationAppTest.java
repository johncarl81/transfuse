package org.androidtransfuse.integrationTest;

import android.content.res.Configuration;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class IntegrationAppTest {

    private IntegrationApp integrationApp;
    private IntegrationAppApplication application;

    @Before
    public void setup() {
        application = new IntegrationAppApplication();
        application.onCreate();

        integrationApp = DelegateUtil.getDelegate(application, IntegrationApp.class);
    }

    @Test
    public void testOnCreate() {
        assertTrue(integrationApp.isOnCreate());
    }

    @Test
    public void testOnLowMemory() {
        application.onLowMemory();
        assertTrue(integrationApp.isOnLowMemory());
    }

    @Test
    public void testOnTerminate() {
        application.onTerminate();
        assertTrue(integrationApp.isOnTerminate());
    }

    @Test
    public void testOnConfigurationChanged() {
        Configuration config = new Configuration();
        application.onConfigurationChanged(config);
        assertEquals(config, integrationApp.getOnConfigurationChanged());
    }
}

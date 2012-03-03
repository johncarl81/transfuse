package org.androidtransfuse.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SimpleApplicationTest {

    private SimpleApplication simpleApplication;
    private SimpleApp simpleApp;

    @Before
    public void setup() {

        simpleApp = new SimpleApp();
        simpleApp.onCreate();

        simpleApplication = DelegateUtil.getDelegate(simpleApp, SimpleApplication.class);
    }

    @Test
    public void testOnCreate() {
        assertNotNull(simpleApplication);
        assertTrue(simpleApplication.isOnCreateCalled());
    }

    @Test
    public void testOnLowMemory() {
        assertFalse(simpleApplication.isOnLowMemoryCalled());
        simpleApp.onLowMemory();
        assertTrue(simpleApplication.isOnLowMemoryCalled());
    }

    @Test
    public void testOnTerminate() {
        assertFalse(simpleApplication.isOnTerminateCalled());
        simpleApp.onTerminate();
        assertTrue(simpleApplication.isOnTerminateCalled());
    }
}

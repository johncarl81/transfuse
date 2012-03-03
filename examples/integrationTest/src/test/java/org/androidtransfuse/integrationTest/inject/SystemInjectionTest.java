package org.androidtransfuse.integrationTest.inject;

import android.app.NotificationManager;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SystemInjectionTest {

    private SystemInjection systemInjection;

    @Before
    public void setup() {
        SystemInjectionActivity systemInjectionActivity = new SystemInjectionActivity();
        systemInjectionActivity.onCreate(null);

        systemInjection = DelegateUtil.getDelegate(systemInjectionActivity, SystemInjection.class);
    }

    @Test
    public void testLocationManagerInjection() {
        assertNotNull(systemInjection.getLocationManager());
    }

    @Test
    public void testVibratorInjection() {
        assertNotNull(systemInjection.getVibrator());
    }

    @Test
    public void testNotificationServiceInjection() {
        assertNotNull(systemInjection.getNotificationService());
        assertEquals(NotificationManager.class, systemInjection.getNotificationService().getClass());
    }
}

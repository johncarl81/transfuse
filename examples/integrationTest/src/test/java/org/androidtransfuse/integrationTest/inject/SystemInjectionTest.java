/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

    @Test
    public void testContextInjection() {
        assertNotNull(systemInjection.getContext());
    }

    @Test
    public void testApplicationInjection() {
        assertNotNull(systemInjection.getApplication());
    }
}

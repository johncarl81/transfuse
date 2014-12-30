/**
 * Copyright 2011-2015 John Ericksen
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
package org.androidtransfuse.integrationTest;

import android.content.res.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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

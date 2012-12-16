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

    private static final String APP_NAME = "Transfuse Integration Test";

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

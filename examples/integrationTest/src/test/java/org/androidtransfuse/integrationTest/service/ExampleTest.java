/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.integrationTest.service;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleTest {

    private Example example;
    private ExampleService exampleService;

    @Before
    public void setup() {
        exampleService = new ExampleService();
        exampleService.onCreate();

        example = DelegateUtil.getDelegate(exampleService, Example.class);
    }

    @Test
    public void testOnCreate() {
        Assert.assertTrue(example.isOnCreateCalled());
    }

    @Test
    public void testOnStartCommand() {
        Intent intent = new Intent();
        assertFalse(example.isOnStartCommandCalled());
        exampleService.onStartCommand(intent, 0, 1);
        assertTrue(example.isOnStartCommandCalled());
    }

    @Test
    public void testOnDestroy() {
        assertFalse(example.isOnDestroyCalled());
        exampleService.onDestroy();
        assertTrue(example.isOnDestroyCalled());
    }

    @Test
    public void testOnLowMemory() {
        assertFalse(example.isOnLowMemoryCalled());
        exampleService.onLowMemory();
        assertTrue(example.isOnLowMemoryCalled());
    }

    @Test
    public void testOnRebind() {
        Intent intent = new Intent();
        assertFalse(example.isOnRebindCalled());
        exampleService.onRebind(intent);
        assertTrue(example.isOnRebindCalled());
    }

    @Test
    @Ignore
    public void testOnTrimMemory(){
        assertFalse(example.isOnTrimMemoryCalled());
        //todo:exampleService.onTrimMemory();
        assertTrue(example.isOnTrimMemoryCalled());
    }

    @Test
    @Ignore
    public void testOnTaskRemoved(){
        assertFalse(example.isOnTaskRemoved());
        //todo:exampleService.onTaskRemoved();
        assertTrue(example.isOnTaskRemoved());
    }


    @Test
    public void testOnConfigurationChanged(){
        assertFalse(example.isOnConfigurationChangedCalled());
        exampleService.onConfigurationChanged(null);
        assertTrue(example.isOnConfigurationChangedCalled());
    }

    @Test
    public void testOnUnbind(){
        assertFalse(example.isOnUnbindCalled());
        exampleService.onUnbind(null);
        assertTrue(example.isOnUnbindCalled());
    }
}

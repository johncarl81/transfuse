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
package org.androidtransfuse.integrationTest.lifecycle;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ActivityLifecycleTest {

    private ActivityLifecycle activityLifecycle;
    private ActivityLifecycleActivity lifecycleActivity;
    private Bundle bundle;

    @Before
    public void setup() {
        bundle = new Bundle();
        lifecycleActivity = new ActivityLifecycleActivity();
        lifecycleActivity.onCreate(bundle);

        activityLifecycle = DelegateUtil.getDelegate(lifecycleActivity, ActivityLifecycle.class);
    }

    @Test
    public void onCreate() {
        assertEquals(bundle, activityLifecycle.getOnCreateBundle());
    }

    @Test
    public void onDestroy() {
        assertFalse(activityLifecycle.isOnDestroyCalled());
        lifecycleActivity.onDestroy();
        assertTrue(activityLifecycle.isOnDestroyCalled());
    }

    @Test
    public void onStop() {
        assertFalse(activityLifecycle.isOnStopCalled());
        lifecycleActivity.onStop();
        assertTrue(activityLifecycle.isOnStopCalled());
    }

    @Test
    public void onPause() {
        assertFalse(activityLifecycle.isOnPauseCalled());
        lifecycleActivity.onPause();
        assertTrue(activityLifecycle.isOnPauseCalled());
    }

    @Test
    public void onResume() {
        assertFalse(activityLifecycle.isOnResumeCalled());
        lifecycleActivity.onResume();
        assertTrue(activityLifecycle.isOnResumeCalled());
    }

    @Test
    public void onStart() {
        assertFalse(activityLifecycle.isOnStartCalled());
        lifecycleActivity.onStart();
        assertTrue(activityLifecycle.isOnStartCalled());
    }

    @Test
    public void onRestart() {
        assertFalse(activityLifecycle.isOnRestartCalled());
        lifecycleActivity.onRestart();
        assertTrue(activityLifecycle.isOnRestartCalled());
    }
}

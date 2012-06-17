package org.androidtransfuse.integrationTest.lifecycle;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

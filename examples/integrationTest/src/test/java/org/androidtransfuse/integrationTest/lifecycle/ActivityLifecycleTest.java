package org.androidtransfuse.integrationTest.lifecycle;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ActivityLifecycleTest {

    private ActivityLifecycle activityLifecycle;
    private ActivityLifecycleActivity lifecycleActivity;

    @Before
    public void setup() throws IllegalAccessException {
        lifecycleActivity = new ActivityLifecycleActivity();
        lifecycleActivity.onCreate(null);

        activityLifecycle = DelegateUtil.getDelegate(lifecycleActivity, ActivityLifecycle.class);
    }

    @Test
    public void onCreate() {
        assertTrue(activityLifecycle.isOnCreate());
    }

    @Test
    public void onDestroy() {
        assertFalse(activityLifecycle.isOnDestroy());
        lifecycleActivity.onDestroy();
        assertTrue(activityLifecycle.isOnDestroy());
    }

    @Test
    public void onStop() {
        assertFalse(activityLifecycle.isOnStop());
        lifecycleActivity.onStop();
        assertTrue(activityLifecycle.isOnStop());
    }

    @Test
    public void onPause() {
        assertFalse(activityLifecycle.isOnPause());
        lifecycleActivity.onPause();
        assertTrue(activityLifecycle.isOnPause());
    }

    @Test
    public void onResume() {
        assertFalse(activityLifecycle.isOnResume());
        lifecycleActivity.onResume();
        assertTrue(activityLifecycle.isOnResume());
    }

    @Test
    public void onStart() {
        assertFalse(activityLifecycle.isOnStart());
        lifecycleActivity.onStart();
        assertTrue(activityLifecycle.isOnStart());
    }

    @Test
    public void onRestart() {
        assertFalse(activityLifecycle.isOnRestart());
        lifecycleActivity.onRestart();
        assertTrue(activityLifecycle.isOnRestart());
    }
}

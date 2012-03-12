package org.androidtransfuse.integrationTest.activity;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.integrationTest.aop.InterceptorRecorder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class PreferencesTest {

    private Preferences preferences;

    @Before
    public void setup() {
        InterceptorRecorder.reset();
        PreferencesActivity preferencesActivity = new PreferencesActivity();
        preferencesActivity.onCreate(null);

        preferences = DelegateUtil.getDelegate(preferencesActivity, Preferences.class);
    }

    @Test
    public void test() {
        assertNotNull(preferences.getActivity());
    }

}

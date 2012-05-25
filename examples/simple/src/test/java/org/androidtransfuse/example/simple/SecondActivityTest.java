package org.androidtransfuse.example.simple;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SecondActivityTest {

    private static final String TEST_EXTRA_ID = "testExtra";
    private static final String TEST_EXTRA_VALUE = "hello";
    private SecondActivityDelegate secondActivityDelegate;

    @Before
    public void setup() {
        Intent callingIntent = new Intent("test");
        callingIntent.putExtra(TEST_EXTRA_ID, TEST_EXTRA_VALUE);

        SecondActivity secondActivity = new SecondActivity();
        secondActivity.setIntent(callingIntent);
        secondActivity.onCreate(null);

        secondActivityDelegate = DelegateUtil.getDelegate(secondActivity, SecondActivityDelegate.class);
    }

    @Test
    public void testPrivateInject() {
        assertNotNull(secondActivityDelegate.getTextView());
    }

    @Test
    public void testExtraInjection() {
        assertEquals(TEST_EXTRA_VALUE, secondActivityDelegate.getTestExtra());
    }

    @Test
    public void testStringArray() {
        assertNotNull(secondActivityDelegate.getSimpleStringArray());
        assertEquals(2, secondActivityDelegate.getSimpleStringArray().length);
    }
}

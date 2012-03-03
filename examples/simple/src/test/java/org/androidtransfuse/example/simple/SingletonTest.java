package org.androidtransfuse.example.simple;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SingletonTest {

    private static final String TEST_EXTRA_ID = "testExtra";
    private static final String TEST_EXTRA_VALUE = "hello";

    private SimpleActivityDelegate testActivityDelegate;
    private SecondActivityDelegate secondActivityDelegate;

    @Before
    public void setup() {
        SimpleActivity simpleActivity = new SimpleActivity();
        simpleActivity.onCreate(null);

        testActivityDelegate = DelegateUtil.getDelegate(simpleActivity, SimpleActivityDelegate.class);

        Intent callingIntent = new Intent("test");
        callingIntent.putExtra(TEST_EXTRA_ID, TEST_EXTRA_VALUE);

        SecondActivity secondActivity = new SecondActivity();
        secondActivity.setIntent(callingIntent);
        secondActivity.onCreate(null);

        secondActivityDelegate = DelegateUtil.getDelegate(secondActivity, SecondActivityDelegate.class);
    }

    @Test
    public void testSingletonAcrossActivities() {
        SingletonTarget singletonTargetOne = testActivityDelegate.getController().getSingletonTarget();
        SingletonTarget singletonTargetTwo = testActivityDelegate.getLateReturnListener().getSingletonTarget();
        SingletonTarget singletonTargetThree = secondActivityDelegate.getSingletonTarget();
        assertEquals(singletonTargetOne, singletonTargetTwo);
        assertEquals(singletonTargetTwo, singletonTargetThree);
    }
}

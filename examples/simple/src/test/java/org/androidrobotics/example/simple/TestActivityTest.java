package org.androidrobotics.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class TestActivityTest {

    private TestActivity testActivity;
    private TestActivityDelegate testActivityDelegate;

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        testActivity = new TestActivity();
        testActivity.onCreate(null);

        Field delegateField = TestActivity.class.getDeclaredField("delegate");

        delegateField.setAccessible(true);
        testActivityDelegate = (TestActivityDelegate) delegateField.get(testActivity);
        delegateField.setAccessible(false);
    }

    @Test
    public void testPrivateInject() {
        assertNotNull(testActivityDelegate.getController());
    }

    @Test
    public void testContructorInject() {
        assertTrue(testActivityDelegate.isConstructorInjected());
    }
}

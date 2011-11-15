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

        Field delegateField = findDelegateField(TestActivity.class, TestActivityDelegate.class);

        delegateField.setAccessible(true);
        testActivityDelegate = (TestActivityDelegate) delegateField.get(testActivity);
        delegateField.setAccessible(false);
    }

    @Test
    public void testPrivateInject() {
        assertNotNull(testActivityDelegate.getController());
        assertTrue(testActivityDelegate.getController().validate());
    }

    @Test
    public void testContructorInject() {
        assertTrue(testActivityDelegate.isConstructorInjected());
    }

    private Field findDelegateField(Class<TestActivity> target, Class<TestActivityDelegate> type) {
        Field delegateField = null;

        for (Field field : target.getDeclaredFields()) {
            if (field.getType() == type) {
                if (delegateField != null) {
                    throw new RoboticsTestException("Type found more than once");
                }
                delegateField = field;
            }
        }

        return delegateField;
    }
}

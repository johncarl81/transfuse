package org.androidrobotics.example.simple;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SecondActivityTest {

    private static final String TEST_EXTRA_ID = "testExtra";
    private static final String TEST_EXTRA_VALUE = "hello";
    private SecondActivity simpleActivity;
    private SecondActivityDelegate secondActivityDelegate;

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        Intent callingIntent = new Intent("test");
        callingIntent.putExtra(TEST_EXTRA_ID, TEST_EXTRA_VALUE);

        simpleActivity = new SecondActivity();

        simpleActivity.setIntent(callingIntent);

        simpleActivity.onCreate(null);

        Field delegateField = findDelegateField(SecondActivity.class, SecondActivityDelegate.class);

        delegateField.setAccessible(true);
        secondActivityDelegate = (SecondActivityDelegate) delegateField.get(simpleActivity);
        delegateField.setAccessible(false);
    }

    @Test
    public void testPrivateInject() {
        assertNotNull(secondActivityDelegate.getTextView());
    }

    @Test
    public void testExtraInjection() {
        assertEquals(TEST_EXTRA_VALUE, secondActivityDelegate.getTestExtra());
    }

    private Field findDelegateField(Class target, Class type) {
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

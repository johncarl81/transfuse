package org.androidrobotics.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SecondActivityTest {

    private SecondActivity simpleActivity;
    private SecondActivityDelegate secondActivityDelegate;

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        simpleActivity = new SecondActivity();
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

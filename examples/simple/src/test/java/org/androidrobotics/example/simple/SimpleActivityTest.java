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
public class SimpleActivityTest {

    private SimpleActivity simpleActivity;
    private SimpleActivityDelegate testActivityDelegate;

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        simpleActivity = new SimpleActivity();
        simpleActivity.onCreate(null);

        Field delegateField = findDelegateField(SimpleActivity.class, SimpleActivityDelegate.class);

        delegateField.setAccessible(true);
        testActivityDelegate = (SimpleActivityDelegate) delegateField.get(simpleActivity);
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

    private Field findDelegateField(Class<SimpleActivity> target, Class<SimpleActivityDelegate> type) {
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

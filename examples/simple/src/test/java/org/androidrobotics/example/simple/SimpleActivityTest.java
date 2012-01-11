package org.androidrobotics.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static junit.framework.Assert.*;

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

    @Test
    public void testSystemServiceInject() {
        assertTrue(testActivityDelegate.isSystemServiceInjected());
    }

    @Test
    public void testOnCreateCalled() {
        assertTrue(testActivityDelegate.isOnCreateCalled());
        assertTrue(testActivityDelegate.isSecondOnCreatCalled());
    }

    @Test
    public void testProvider() {
        assertNotNull(testActivityDelegate.getProvidedValue());
        assertEquals(ValueProvider.PROVIDED_VALUE, testActivityDelegate.getProvidedValue().getValue());
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

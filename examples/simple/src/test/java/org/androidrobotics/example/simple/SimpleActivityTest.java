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

    private SimpleActivityDelegate testActivityDelegate;

    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        SimpleActivity simpleActivity = new SimpleActivity();
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
        assertTrue(testActivityDelegate.isSecondOnCreateCalled());
    }

    @Test
    public void testProvider() {
        assertNotNull(testActivityDelegate.getProvidedValue());
        assertEquals(ValueProvider.PROVIDED_VALUE, testActivityDelegate.getProvidedValue().getValue());
    }

    @Test
    public void testResourceInjection() {
        assertNotNull(testActivityDelegate.getResources());
    }

    @Test
    public void testStringResource() {
        assertEquals("hello world", testActivityDelegate.getTestHello());
    }

    @Test
    public void testSuperClassFieldInjection() {
        assertNotNull(testActivityDelegate.getVibrateButton());
    }

    @Test
    public void testSuperPrivateMethodInjection() {
        assertNotNull(testActivityDelegate.getContext());
    }

    private Field findDelegateField(Class target, Class type) {
        Field delegateField = null;

        for (Field field : target.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                if (delegateField != null) {
                    throw new RoboticsTestException("Type found more than once");
                }
                delegateField = field;
            }
        }

        return delegateField;
    }
}

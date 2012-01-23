package org.androidrobotics.example.simple;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        testActivityDelegate = DelegateUtil.getDelegate(simpleActivity, SimpleActivityDelegate.class);
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
        assertEquals(R.id.vibrateButton, testActivityDelegate.getVibrateButton().getId());
    }

    @Test
    public void testSuperPrivateMethodInjection() {
        assertNotNull(testActivityDelegate.getContext());
    }

    @Test
    public void testMultipleResourceReferences() {
        SimpleController controller = testActivityDelegate.getController();

        assertEquals(R.id.vibrateButton, controller.getVibrateButton().getId());
        assertEquals(R.id.notifyButton, controller.getNotifyButton().getId());
    }

    @Test
    public void testSingletonScope() {
        SingletonTarget singletonTargetOne = testActivityDelegate.getController().getSingletonTarget();
        SingletonTarget singletonTargetTwo = testActivityDelegate.getLateReturnListener().getSingletonTarget();
        assertEquals(singletonTargetOne, singletonTargetTwo);
    }
}

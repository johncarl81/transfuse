package org.androidtransfuse.util;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
public class InjectionUtilTest {

    private static final String TEST_VALUE = "hello";
    private InjectionUtil injectionUtil;

    @Before
    public void setup() {
        injectionUtil = InjectionUtil.getInstance();
    }

    @Test
    public void testPrivateMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.callMethod(Void.class, Target.class, target, "setPrivateValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateMethodGet() {
        Target target = new Target();

        assertNull(target.getValue());
        target.setValue(TEST_VALUE);

        assertEquals(TEST_VALUE, injectionUtil.callMethod(String.class, Target.class, target, "getPrivateValue", new Class[]{}, new Object[]{}));
    }

    @Test
    public void testPrivateSuperMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.callMethod(Void.class, TargetSuper.class, target, "setSuperValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getSuperValue());
    }

    @Test
    public void testPrivateConstructorInjection() {
        Target target = injectionUtil.callConstructor(Target.class, new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(target.getValue(), TEST_VALUE);
    }

    @Test
    public void testPrivateFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.setField(Target.class, target, "value", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.setField(TargetSuper.class, target, "superValue", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getSuperValue());
    }

    @Test
    public void verifyMethodNames() throws NoSuchMethodException {

        Method getInstanceMethod = InjectionUtil.class.getMethod(InjectionUtil.GET_INSTANCE_METHOD);
        assertNotNull(getInstanceMethod);
        Method callConstructorMethod = InjectionUtil.class.getMethod(InjectionUtil.CALL_CONSTRUCTOR_METHOD, Class.class, Class[].class, Object[].class);
        assertNotNull(callConstructorMethod);
        Method callMethodMethod = InjectionUtil.class.getMethod(InjectionUtil.CALL_METHOD_METHOD, Class.class, Class.class, Object.class, String.class, Class[].class, Object[].class);
        assertNotNull(callMethodMethod);
        Method getFieldMethod = InjectionUtil.class.getMethod(InjectionUtil.GET_FIELD_METHOD, Class.class, Class.class, Object.class, String.class);
        assertNotNull(getFieldMethod);
        Method setFieldMethod = InjectionUtil.class.getMethod(InjectionUtil.SET_FIELD_METHOD, Class.class, Object.class, String.class, Object.class);
        assertNotNull(setFieldMethod);
    }
}

package org.androidtransfuse.util;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

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

        injectionUtil.callMethod(Void.class, target, 0, "setValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.callMethod(Void.class, target, 1, "setSuperValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

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

        injectionUtil.setField(target, 0, "value", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        injectionUtil.setField(target, 1, "superValue", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getSuperValue());
    }
}

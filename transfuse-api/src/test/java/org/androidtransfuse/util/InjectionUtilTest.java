package org.androidtransfuse.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
public class InjectionUtilTest {

    private static final String TEST_VALUE = "hello";

    @Test
    public void testPrivateMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        InjectionUtil.setMethod(target, 0, "setValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperMethodInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        InjectionUtil.setMethod(target, 1, "setSuperValue", new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(TEST_VALUE, target.getSuperValue());
    }

    @Test
    public void testPrivateConstructorInjection() {
        Target target = InjectionUtil.setConstructor(Target.class, new Class[]{String.class}, new Object[]{TEST_VALUE});

        assertEquals(target.getValue(), TEST_VALUE);
    }

    @Test
    public void testPrivateFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        InjectionUtil.setField(target, 0, "value", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getValue());
    }

    @Test
    public void testPrivateSuperFieldInjection() {
        Target target = new Target();

        assertNull(target.getValue());

        InjectionUtil.setField(target, 1, "superValue", TEST_VALUE);

        assertEquals(TEST_VALUE, target.getSuperValue());
    }
}

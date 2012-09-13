package org.androidtransfuse.util;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class DelayedLoadTest {

    @Test
    public void validateMethodName() throws NoSuchMethodException {
        Method method = DelayedLoad.class.getMethod(DelayedLoad.LOAD_METHOD, Object.class);
        assertNotNull(method);
    }
}

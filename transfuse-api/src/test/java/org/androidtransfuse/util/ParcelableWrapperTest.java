package org.androidtransfuse.util;

import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ParcelableWrapperTest {

    @Test
    public void verifyMethodNames() throws NoSuchMethodException {

        Method getWrappedMethod = ParcelableWrapper.class.getMethod(ParcelableWrapper.GET_WRAPPED);
        assertNotNull(getWrappedMethod);
    }
}

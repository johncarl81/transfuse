package org.androidtransfuse.util;

import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ParcelableFactoryTest {

    @Test
    public void testMethodName() throws NoSuchMethodException {
        Method buildParcelableMethod = ParcelableFactory.class.getMethod(ParcelableFactory.BUILD_PARCELABLE, Object.class);
        assertNotNull(buildParcelableMethod);
    }
}

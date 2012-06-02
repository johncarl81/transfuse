package org.androidtransfuse.annotations;

import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ParcelConverterTest {

    @Test
    public void verifyMethodNames() throws NoSuchMethodException {
        Method translateToMethod = ParcelConverter.class.getMethod(ParcelConverter.TRANSLATE_METHOD, new Class[]{Object.class, android.os.Parcel.class});
        assertNotNull(translateToMethod);
        Method translateFromMethod = ParcelConverter.class.getMethod(ParcelConverter.TRANSLATE_METHOD, new Class[]{android.os.Parcel.class});
        assertNotNull(translateFromMethod);
    }
}

package org.androidtransfuse.util;

import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ProvidersTest {

    @Test
    public void testOf(){
        String value = "test";

        Provider<String> valueProvider = Providers.of(value);

        assertEquals(value, valueProvider.get());
    }

    @Test
    public void testMethods() throws NoSuchMethodException {
        Method ofMethod = Providers.class.getDeclaredMethod(Providers.OF_METHOD, Object.class);
        assertNotNull(ofMethod);
    }
}

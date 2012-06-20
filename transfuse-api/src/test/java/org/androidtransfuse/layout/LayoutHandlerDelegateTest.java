package org.androidtransfuse.layout;

import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class LayoutHandlerDelegateTest {

    @Test
    public void verityMethod() throws NoSuchMethodException {
        Method invokeLayoutMethod = LayoutHandlerDelegate.class.getMethod(LayoutHandlerDelegate.INVOKE_LAYOUT_METHOD);
        assertNotNull(invokeLayoutMethod);
    }
}

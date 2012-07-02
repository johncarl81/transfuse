package org.androidtransfuse.scope;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ContextScopeHolderTest {

    @Test
    public void validateMethod() throws NoSuchMethodException {
        Method getScopeMethod = ContextScopeHolder.class.getMethod(ContextScopeHolder.GET_SCOPE);
        assertNotNull(getScopeMethod);
    }
}

package org.androidtransfuse.scope;

import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ScopeTest {

    @Test
    public void verifyScopeMethod() throws NoSuchMethodException {
        Method getScopedObjectMethod = Scope.class.getMethod(Scope.GET_SCOPED_OBJECT, Class.class, Provider.class);
        assertNotNull(getScopedObjectMethod);
    }
}

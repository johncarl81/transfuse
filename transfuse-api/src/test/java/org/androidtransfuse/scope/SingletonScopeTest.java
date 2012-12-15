package org.androidtransfuse.scope;

import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class SingletonScopeTest {

    private interface ScopeTargetBuilder extends Provider<ScopeTarget> {
    }

    @Test
    public void testSingleton(){
        assertEquals(SingletonScope.getInstance(), SingletonScope.getInstance());
    }

    @Test
    public void testScopePerformance() {

        ScopeTargetBuilder perfBuilder = new ScopeTargetBuilder() {
            @Override
            public ScopeTarget get() {
                return new ScopeTarget();
            }
        };

        Scope perfSource = SingletonScope.getInstance();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            perfSource.getScopedObject(ScopeTarget.class, perfBuilder);
        }
        System.out.println("Total time: " + (System.currentTimeMillis() - start));
    }

    @Test
    public void verifyMethod() throws NoSuchMethodException {
        Method getInstanceMethod = SingletonScope.class.getMethod(SingletonScope.GET_INSTANCE);
        assertNotNull(getInstanceMethod);
    }
}

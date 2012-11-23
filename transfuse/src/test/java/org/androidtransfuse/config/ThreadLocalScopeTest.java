package org.androidtransfuse.config;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
public class ThreadLocalScopeTest {

    private static class ScopedTarget {
    }

    private ThreadLocalScope scope;
    private ScopedTarget target;
    private Provider<ScopedTarget> provider;

    @Before
    public void setup() {
        scope = new ThreadLocalScope();
        target = new ScopedTarget();

        provider = scope.scope(Key.get(ScopedTarget.class), new Provider<ScopedTarget>() {
            @Override
            public ScopedTarget get() {
                return target;
            }
        });

    }

    @Test
    public void testScope() {

        scope.enter();

        assertEquals(target, provider.get());

        scope.exit();
    }

    @Test
    public void testOutOfScope() {

        scope.enter();

        scope.exit();

        try {
            provider.get();
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testBeforeOutOfScope() {
        try {
            provider.get();
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }
    }
}

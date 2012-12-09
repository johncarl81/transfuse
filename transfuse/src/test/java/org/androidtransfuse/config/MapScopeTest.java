package org.androidtransfuse.config;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class MapScopeTest {

    private static class ScopedTarget {
    }

    private static class SeedTarget {
    }

    private MapScope scope;
    private ScopedTarget target;
    private Provider<ScopedTarget> provider;

    @Before
    public void setup() {
        scope = new MapScope();
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

    @Test
    public void testSeed() {
        scope.enter();

        try {
            scope.scope(Key.get(SeedTarget.class), new ThrowingProvider<SeedTarget>()).get();
            //should throw exception
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

        scope.seed(SeedTarget.class, new SeedTarget());

        assertNotNull(scope.scope(Key.get(SeedTarget.class), new ThrowingProvider<SeedTarget>()).get());

        scope.exit();
    }

    @Test
    public void testKeySeed() {
        scope.enter();

        try {
            scope.scope(Key.get(SeedTarget.class), new ThrowingProvider<SeedTarget>()).get();
            //should throw exception
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

        scope.seed(Key.get(SeedTarget.class), new SeedTarget());

        assertNotNull(scope.scope(Key.get(SeedTarget.class), new ThrowingProvider<SeedTarget>()).get());

        scope.exit();
    }
}

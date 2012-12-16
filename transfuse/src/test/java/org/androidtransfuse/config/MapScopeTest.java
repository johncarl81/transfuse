/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

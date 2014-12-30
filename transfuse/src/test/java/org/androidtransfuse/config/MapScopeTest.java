/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.scope.ScopeKey;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Provider;

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
    private ScopedTarget scopedTarget;

    @Before
    public void setup() {
        scope = new MapScope();
        target = new ScopedTarget();


    }

    private ScopedTarget getScopedTarget(){
        return scope.getScopedObject(ScopeKey.of(ScopedTarget.class), new Provider<ScopedTarget>() {
            @Override
            public ScopedTarget get() {
                return target;
            }
        });
    }

    @Test
    public void testScope() {

        scope.enter();

        assertEquals(target, getScopedTarget());

        scope.exit();
    }

    @Test
    public void testOutOfScope() {

        scope.enter();

        scope.exit();

        try {
            getScopedTarget();
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

    }

    @Test
    public void testBeforeOutOfScope() {
        try {
            getScopedTarget();
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSeed() {
        scope.enter();

        try {
            scope.getScopedObject(ScopeKey.of(SeedTarget.class), new ThrowingProvider<SeedTarget>());
            //should throw exception
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

        scope.seed(ScopeKey.of(SeedTarget.class), new SeedTarget());

        assertNotNull(scope.getScopedObject(ScopeKey.of(SeedTarget.class), new ThrowingProvider<SeedTarget>()));

        scope.exit();
    }

    @Test
    public void testKeySeed() {
        scope.enter();

        try {
            scope.getScopedObject(ScopeKey.of(SeedTarget.class), new ThrowingProvider<SeedTarget>());
            //should throw exception
            assertTrue(false);
        } catch (OutOfScopeException e) {
            assertTrue(true);
        }

        scope.seed(ScopeKey.of(SeedTarget.class), new SeedTarget());

        assertNotNull(scope.getScopedObject(ScopeKey.of(SeedTarget.class), new ThrowingProvider<SeedTarget>()));

        scope.exit();
    }
}

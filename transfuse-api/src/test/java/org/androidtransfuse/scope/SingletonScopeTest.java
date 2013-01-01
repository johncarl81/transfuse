/**
 * Copyright 2013 John Ericksen
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

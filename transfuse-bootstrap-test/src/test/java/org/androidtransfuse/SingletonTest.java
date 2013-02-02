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
package org.androidtransfuse;

import org.androidtransfuse.bootstrap.Bootstrap;
import org.androidtransfuse.bootstrap.Bootstraps;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Provider;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
@Bootstrap(test = true)
public class SingletonTest {

    private static final String TEST_VALUE = "test";

    @Inject
    private SingletonDependency singletonDependency;
    @Inject
    private Provider<Dependency> dependencyProvider;

    @Before
    public void setUp() throws Exception {
        Bootstraps.injectTest(this);
    }

    @Test
    public void testOne() throws Exception {
        assertNull(singletonDependency.getValue());
        singletonDependency.setValue(TEST_VALUE);
    }

    @Test
    public void testTwo() throws Exception {
        assertNull(singletonDependency.getValue());
        singletonDependency.setValue(TEST_VALUE);
    }

    @Test
    public void testSingletonEquality() throws Exception {
        assertEquals(dependencyProvider.get().getSingletonDependency(), singletonDependency);
    }
}

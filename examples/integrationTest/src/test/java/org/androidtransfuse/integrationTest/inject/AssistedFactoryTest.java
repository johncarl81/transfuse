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
package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.Factories;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
public class AssistedFactoryTest {

    private AssistedFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = Factories.get(AssistedFactory.class);
    }

    @Test
    public void testAssistedInjection(){
        AssistedDependency dependency = new AssistedDependency();
        AssistedTarget assistedTarget = factory.buildTarget(dependency);

        assertEquals(dependency, assistedTarget.getDependency());
    }

    @Test
    public void testAssistedDoubleInjection(){
        AssistedDependency one = new AssistedDependency();
        AssistedDependency two = new AssistedDependency();
        AssistedDoubleTarget assistedTarget = factory.buildTarget(one, two);

        assertEquals(one, assistedTarget.getDependencyOne());
        assertEquals(two, assistedTarget.getDependencyTwo());
    }
}

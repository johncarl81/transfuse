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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@Bootstrap
public class ExerciseTest {

    @Inject
    private Exercise exercise;
    @Inject
    private SingletonDependency singletonDependency;

    @Before
    public void setUp() {
        Bootstraps.inject(this);
    }

    @Test
    public void testThisInjection(){
        assertNotNull(exercise);
    }

    @Test
    public void testInjections(){
        assertNotNull(exercise.getDependency());
        assertNotNull(exercise.getDependencyProvider());
        assertNotNull(exercise.getDependencyBase());
        assertNotNull(exercise.getFactory());
        assertNotNull(exercise.getFactory().getDependency());
        assertEquals(singletonDependency, exercise.getSingleton());
    }
}

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
package org.androidtransfuse.scope;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class ScopeKeyTest {

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        Method getMethod = ScopeKey.class.getMethod(ScopeKey.GET_METHOD, Class.class, String.class);
        assertNotNull(getMethod);
    }

    @Test
    public void testClassEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class);
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class);

        assertEquals(one, two);
    }

    @Test
    public void testClassNotEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class);
        ScopeKey two = ScopeKey.of(Method.class);

        assertNotEquals(one, two);
    }

    @Test
    public void testAnnotationEqual(){
        String annotation = "@Test";
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class).annotatedBy(annotation);
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class).annotatedBy(annotation);

        assertEquals(one, two);
    }

    @Test
    public void testAnnotationNotEqual(){
        ScopeKey one = ScopeKey.of(ScopeKeyTest.class).annotatedBy("@One");
        ScopeKey two = ScopeKey.of(ScopeKeyTest.class).annotatedBy("@Two");

        assertNotEquals(one, two);
    }
}

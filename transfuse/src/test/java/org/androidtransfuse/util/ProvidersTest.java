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
package org.androidtransfuse.util;

import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ProvidersTest {

    @Test
    public void testOf(){
        String value = "test";

        Provider<String> valueProvider = Providers.of(value);

        assertEquals(value, valueProvider.get());
    }

    @Test
    public void testMethods() throws NoSuchMethodException {
        Method ofMethod = Providers.class.getDeclaredMethod(Providers.OF_METHOD, Object.class);
        assertNotNull(ofMethod);
    }
}

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

import javax.inject.Provider;

/**
 * Toxic Provider that throws an exception if get() is called.
 *
 * Should be used as a placeholder to ensure that a scoped object is not created by this provider, but instead seeded.
 *
 * @author John Ericksen
 */
public class ThrowingProvider<T> implements Provider<T> {

    @Override
    public T get() {
        throw new OutOfScopeException("Expected seeded object, unable to construct directly.");
    }
}

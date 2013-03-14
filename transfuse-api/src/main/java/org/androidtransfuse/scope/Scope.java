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

import javax.inject.Provider;

/**
 * Defines a scope in which to lookup instances by type.  When looking up instances, a provider for the given type
 * must be also provided in case the instance does not exist.
 *
 * @author John Ericksen
 */
public interface Scope {

    String GET_SCOPED_OBJECT = "getScopedObject";

    /**
     * Lookup of the given instance by type.
     * @param key scoping key
     * @param provider required to build the instance if applicable
     * @param <T> relating type
     * @return scoped instance
     */
    <T> T getScopedObject(ScopeKey<T> key, Provider<T> provider);
}

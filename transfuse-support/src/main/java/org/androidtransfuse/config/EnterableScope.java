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

import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.ScopeKey;

/**
 * Scope with enter and exit functionality.
 *
 * @author John Ericksen
 */
public interface EnterableScope extends Scope {

    /**
     * Begins the current scope.
     */
    void enter();

    /**
     * Stops the current scope.
     */
    void exit();

    /**
     * Specifies the given value to be used with in this scoping block
     *
     * @param key   representing the value
     * @param value of the object
     * @param <T>   generic parameter binding key and value
     */
    <T> void seed(ScopeKey<T> key, T value);
}

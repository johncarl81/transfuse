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
package org.androidtransfuse.util;

/**
 * Declares the functionality to load a Virtual Proxy.  This class allows Transfuse to break injection loops by delaying
 * the construction of a key instance.  Transfuse will, once the injection loop is established, construct and load the
 * 'breaking' instance via the functionality defined in this interface.
 *
 * @author John Ericksen
 */
public interface DelayedLoad<T> {

    String LOAD_METHOD = "load";

    /**
     * Loads the given target as a proxy.
     *
     * @param target proxy
     */
    void load(T target);
}

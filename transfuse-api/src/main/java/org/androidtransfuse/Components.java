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

import org.androidtransfuse.util.GeneratedRepositoryProxy;

/**
 * Static utility class which maps Transfuse Components to the generated Android Components.
 *
 * @author John Ericksen
 */
public final class Components {

    public static final String COMPONENTS_REPOSITORY_NAME = "Transfuse$Components";
    public static final String COMPONENTS_PACKAGE = "org.androidtransfuse";

    private static final GeneratedRepositoryProxy<ComponentsRepository> PROXY =
            new GeneratedRepositoryProxy<ComponentsRepository>(COMPONENTS_PACKAGE, COMPONENTS_REPOSITORY_NAME);

    private Components(){
        // private utility class constructor
    }

    /**
     * Provides the corresponding generated Android Component class for the input Transfuse Component class.
     *
     * @throws org.androidtransfuse.util.TransfuseRuntimeException if there was an error looking up the wrapped
     * Transfuse$Components class.
     * @param type Transfuse Component class
     * @param <T>
     * @return class Android Component class
     */
    public static<T> Class<T> get(Class<?> type) {
        return (Class<T>) PROXY.get().get(type);
    }

    /**
     * Proxy Interface to be implemented by code generation.
     */
    public static interface ComponentsRepository {

        /**
         * Provides the corresponding generated Android Component class for the input Transfuse Component class.
         *
         * Transfuse$Components class.
         * @param type Transfuse Component class
         * @param <T>
         * @return class Android Component class
         */
        <T> Class<T> get(Class<?> type);
    }
}

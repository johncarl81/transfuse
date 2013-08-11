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

import org.androidtransfuse.util.GeneratedCodeRepository;

/**
 * Static utility class which maps Transfuse Components to the generated Android Components.
 *
 * @author John Ericksen
 */
public final class Components {

    public static final String COMPONENTS_REPOSITORY_NAME = "Transfuse" + GeneratedCodeRepository.SEPARATOR + "Components";
    public static final String COMPONENTS_PACKAGE = "org.androidtransfuse";

    private static final GeneratedCodeRepository<Class> REPOSITORY =
            new GeneratedCodeRepository<Class>(COMPONENTS_PACKAGE, COMPONENTS_REPOSITORY_NAME) {
                @Override
                public Class findClass(Class clazz) {
                    return null; //cannot find class
                }
            };

    private Components(){
        // private utility class constructor
    }

    /**
     * Provides the corresponding generated Android Component class for the input Transfuse Component class.
     *
     * @throws `org.androidtransfuse.util.TransfuseRuntimeException` if there was an error looking up the wrapped
     * Transfuse$$Components class.
     * @param type Transfuse Component class
     * @param <T>
     * @return class Android Component class
     */
    public static<T> Class<T> get(Class<?> type) {
        return (Class<T>) REPOSITORY.get(type);
    }
}

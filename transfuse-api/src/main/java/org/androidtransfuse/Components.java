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

import org.androidtransfuse.util.ComponentsRepository;
import org.androidtransfuse.util.GeneratedRepositoryProxy;

/**
 * @author John Ericksen
 */
public final class Components {

    public static final String COMPONENTS_NAME = "Components";
    public static final String COMPONENTS_REPOSITORY_NAME = "Transfuse$Components";
    public static final String COMPONENTS_PACKAGE = "org.androidtransfuse";

    private Components(){
        // private utility class constructor
    }

    private static final GeneratedRepositoryProxy<ComponentsRepository> PROXY =
            new GeneratedRepositoryProxy<ComponentsRepository>(COMPONENTS_PACKAGE, COMPONENTS_REPOSITORY_NAME);

    public static<T> Class<T> get(Class<?> type) {
        ComponentsRepository componentsRepository = PROXY.get();
        return componentsRepository == null ? null : (Class<T>) componentsRepository.get(type);
    }
}

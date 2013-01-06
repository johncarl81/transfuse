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
import org.androidtransfuse.util.InjectorRepository;

/**
 * @author John Ericksen
 */
public final class Injectors {

    public static final String INJECTORS_NAME = "Injectors";
    public static final String INJECTORS_REPOSITORY_NAME = "Transfuse$Injectors";
    public static final String INJECTORS_PACKAGE = "org.androidtransfuse";

    private Injectors(){
        // private utility class constructor
    }

    private static final GeneratedRepositoryProxy<InjectorRepository> PROXY =
            new GeneratedRepositoryProxy<InjectorRepository>(INJECTORS_PACKAGE, INJECTORS_REPOSITORY_NAME);

    public static<T> T get(Class<T> type) {
        InjectorRepository injectorRepository = PROXY.get();
        return injectorRepository == null ? null : injectorRepository.get(type);
    }
}

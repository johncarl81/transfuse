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

import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.FactoryBuilderReflectionProxy;
import org.androidtransfuse.util.GeneratedCodeRepository;
import org.androidtransfuse.util.Namer;

/**
 * Static utility class which maps the `@Factory` annotated interface to the generated implementation.
 *
 * @author John Ericksen
 */
public final class Factories {

    public static final String FACTORIES_REPOSITORY_NAME = Namer.name("Transfuse").append("Factories").build();
    public static final String FACTORIES_PACKAGE = "org.androidtransfuse";
    public static final String IMPL_EXT = "Factory";

    private static final GeneratedCodeRepository<FactoryBuilder> REPOSITORY =
            new GeneratedCodeRepository<FactoryBuilder>(FACTORIES_PACKAGE, FACTORIES_REPOSITORY_NAME) {

                @Override
                public FactoryBuilder findClass(Class clazz) {

                    try {
                        Class factoryClass = Class.forName(Namer.name(clazz.getName()).append(IMPL_EXT).build());
                        return new FactoryBuilderReflectionProxy(factoryClass);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }

                }
            };

    private Factories() {
        // private utility class constructor.
    }

    /**
     * Returns an instance of the provided `@Factory` interface.
     *
     * @param type Factory type
     * @param <T>
     * @return Generated Factory instance
     * @throws org.androidtransfuse.util.TransfuseRuntimeException
     *          if there was an error looking up the wrapped
     *          Factory class
     */
    public static <T> T get(Class<T> type) {
        FactoryBuilder factoryBuilder = REPOSITORY.get(type);
        return (T) factoryBuilder.get();
    }

    public static <T> T get(Class<T> type, Scopes scopes) {
        FactoryBuilder factoryBuilder = REPOSITORY.get(type);
        return (T) factoryBuilder.get(scopes);
    }

    public interface FactoryBuilder<T> {

        T get();

        T get(Scopes scopes);
    }
}

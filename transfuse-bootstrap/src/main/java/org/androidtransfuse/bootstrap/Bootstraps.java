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
package org.androidtransfuse.bootstrap;

import org.androidtransfuse.scope.Scope;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.GeneratedRepositoryProxy;
import org.androidtransfuse.util.Providers;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public class Bootstraps {

    public static final String BOOTSTRAPS_INJECTOR_PACKAGE = "org.androidtransfuse.bootstrap";
    public static final String BOOTSTRAPS_INJECTOR_NAME = "Bootstraps$Injector";
    public static final String BOOTSTRAPS_TEST_INJECTOR_NAME = "Bootstraps$TestInjector";
    public static final String BOOTSTRAPS_INJECTOR_METHOD = "inject";
    public static final String BOOTSTRAPS_INJECTOR_GET = "getMappings";

    private static final GeneratedRepositoryProxy<BootstrapRepository> PROXY =
            new GeneratedRepositoryProxy<BootstrapRepository>(BOOTSTRAPS_INJECTOR_PACKAGE, BOOTSTRAPS_INJECTOR_NAME);

    private static final GeneratedRepositoryProxy<BootstrapRepository> TEST_PROXY =
            new GeneratedRepositoryProxy<BootstrapRepository>(BOOTSTRAPS_INJECTOR_PACKAGE, BOOTSTRAPS_TEST_INJECTOR_NAME);




    @SuppressWarnings("unchecked")
    public static <T> void inject(T input){
        PROXY.get().getMappings().get(input.getClass()).inject(input.getClass(), input);
    }

    @SuppressWarnings("unchecked")
    public static <T> void injectTest(T input){
        TEST_PROXY.get().getMappings().get(input.getClass()).inject(input.getClass(), input);
    }

    @SuppressWarnings("unchecked")
    public static <T> BootstrapInjector<T> getInjector(Class<T> clazz){
        return PROXY.get().getMappings().get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> BootstrapInjector<T> getTestInjector(Class<T> clazz){
        return TEST_PROXY.get().getMappings().get(clazz);
    }

    public static abstract class BootstrapInjector<T>{
        private final Map<Class, Object> singletons = new HashMap<Class, Object>();

        public abstract void inject(Class<T> key, T input);

        public <S> BootstrapInjector<T> addSingleton(Class<S> singletonClass, S singleton){
            singletons.put(singletonClass, singleton);
            return this;
        }

        protected void scopeSingletons(Scopes scopes){
            Scope singletonScope = scopes.getScope(Singleton.class);
            for (Map.Entry<Class, Object> singletonEntry : singletons.entrySet()) {
                singletonScope.getScopedObject(singletonEntry.getKey(), Providers.of(singletonEntry.getValue()));
            }
        }
    }

    public interface BootstrapRepository{
        Map<Class, BootstrapInjector> getMappings();
    }
}

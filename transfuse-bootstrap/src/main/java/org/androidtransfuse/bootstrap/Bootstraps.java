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
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.scope.Scopes;
import org.androidtransfuse.util.GeneratedCodeRepository;
import org.androidtransfuse.util.Namer;
import org.androidtransfuse.util.Providers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John Ericksen
 */
public final class Bootstraps {

    public static final String BOOTSTRAPS_INJECTOR_PACKAGE = "org.androidtransfuse.bootstrap";
    public static final String BOOTSTRAPS_INJECTOR_NAME = Namer.name("Bootstraps").append("Factory").build();
    public static final String BOOTSTRAPS_INJECTOR_METHOD = "inject";
    public static final String BOOTSTRAPS_INJECTOR_GET = "get";
    public static final String IMPL_EXT = "Bootstrap";

    private static final GeneratedCodeRepository<BootstrapInjector> REPOSITORY =
            new GeneratedCodeRepository<BootstrapInjector>(BOOTSTRAPS_INJECTOR_PACKAGE, BOOTSTRAPS_INJECTOR_NAME) {
                @Override
                public BootstrapInjector findClass(Class clazz) {

                    try {
                        Class bootstrapClass = Class.forName(Namer.name(clazz.getName()).append(IMPL_EXT).build());
                        return new BootstrapInjectorReflectionProxy(bootstrapClass);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }

                }
            };

    private Bootstraps(){
        //private utility constructor
    }

    @SuppressWarnings("unchecked")
    public static <T> void inject(T input){
        REPOSITORY.get(input.getClass()).inject(input);
    }

    @SuppressWarnings("unchecked")
    public static <T> BootstrapInjector<T> getInjector(Class<T> clazz){
        return REPOSITORY.get(clazz);
    }

    public interface BootstrapInjector<T>{

        void inject(T input);

        <S> BootstrapInjector<T> add(Class<? extends Annotation> scope, ScopeKey<S> bindType, S instance);
    }

    public abstract static class BootstrapsInjectorAdapter<T> implements BootstrapInjector<T>{
        private final Map<Class<? extends Annotation> , Map<ScopeKey, Object>> scoped = new HashMap<Class<? extends Annotation> , Map<ScopeKey, Object>>();

        public <S> BootstrapInjector<T> add(Class<? extends Annotation> scope, ScopeKey<S> bindType, S instance){
            if(!scoped.containsKey(scope)){
                scoped.put(scope, new HashMap<ScopeKey, Object>());
            }
            scoped.get(scope).put(bindType, instance);
            return this;
        }

        @SuppressWarnings("unchecked")
        protected void scopeSingletons(Scopes scopes){
            for (Map.Entry<Class<? extends Annotation>, Map<ScopeKey, Object>> scopedEntry : scoped.entrySet()) {
                Scope scope = scopes.getScope(scopedEntry.getKey());

                if(scope != null){
                    for (Map.Entry<ScopeKey, Object> scopingEntry : scopedEntry.getValue().entrySet()) {
                        scope.getScopedObject(scopingEntry.getKey(), Providers.of(scopingEntry.getValue()));
                    }
                }
            }
        }
    }
}

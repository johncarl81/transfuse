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

import android.app.Application;
import org.androidtransfuse.util.TransfuseRuntimeException;

import javax.inject.Provider;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public class ApplicationScope implements Scope {

    public static final String SEED_METHOD = "seed";

    private final ConcurrentMap<ScopeKey, Object> scopeMap = new ConcurrentHashMap<ScopeKey, Object>();

    @Override
    public <T> T getScopedObject(ScopeKey<T> key, Provider<T> provider) {
        Object result = scopeMap.get(key);
        if (result == null) {
            Object value = provider.get();
            result = scopeMap.putIfAbsent(key, value);
            if (result == null) {
                result = value;
            }
        }

        return (T) result;
    }

    public <T> void seed(ScopeKey<T> key, Object object){
        scopeMap.put(key, object);
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @javax.inject.Scope
    public @interface ApplicationScopeQualifier {}

    public static final class ApplicationProvider implements Provider<Application>{
        @Override
        public Application get() {
            throw new TransfuseRuntimeException("Trying to inject Application before seeded.");
        }
    }
}

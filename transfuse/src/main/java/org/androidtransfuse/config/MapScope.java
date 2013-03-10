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
package org.androidtransfuse.config;

import org.androidtransfuse.scope.ScopeKey;

import javax.inject.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public class MapScope implements EnterableScope {

    private ConcurrentMap<ScopeKey<?>, Object> values;

    public void enter() {
        values = new ConcurrentHashMap<ScopeKey<?>, Object>();
    }

    public void exit() {
        values = null;
    }

    public <T> void seed(ScopeKey<T> key, T value) {
        Map<ScopeKey<?>, Object> scopedObjects = getScopedObjectMap(key);
        scopedObjects.put(key, value);
    }

    public <T> void seed(Class<T> clazz, T value) {
        seed(new ScopeKey<T>(clazz), value);
    }

    public <T> T getScopedObject(final ScopeKey<T> key, final Provider<T> provider) {
        ConcurrentMap<ScopeKey<?>, Object> scopedObjects = getScopedObjectMap(key);

        @SuppressWarnings("unchecked")
        Object current = scopedObjects.get(key);
        if (current == null) {
            Object value = provider.get();
            current = scopedObjects.putIfAbsent(key, value);
            if(current == null){
                current = value;
            }
        }
        return (T) current;
    }

    @Override
    public <T> T getScopedObject(Class<T> clazz, javax.inject.Provider<T> provider) {
        return getScopedObject(new ScopeKey<T>(clazz), provider);
    }

    private <T> ConcurrentMap<ScopeKey<?>, Object> getScopedObjectMap(ScopeKey<T> key) {
        if (values == null) {
            throw new OutOfScopeException("Cannot access " + key + " outside of a scoping block");
        }
        return values;
    }
}

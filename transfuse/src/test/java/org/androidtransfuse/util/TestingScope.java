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
package org.androidtransfuse.util;

import org.androidtransfuse.config.EnterableScope;
import org.androidtransfuse.scope.ScopeKey;

import javax.inject.Provider;
import java.util.HashMap;

public class TestingScope implements EnterableScope {

    private HashMap<ScopeKey<?>, Object> values = new HashMap<ScopeKey<?>, Object>();

    public void enter() {//noop
    }

    public void exit() {//noop
    }

    public <T> void seed(ScopeKey<T> key, T value) {
        values.put(key, value);
    }

    public <T> void seed(Class<T> clazz, T value) {
        seed(new ScopeKey<T>(clazz), value);
    }

    @Override
    public <T> T getScopedObject(Class<T> clazz, javax.inject.Provider<T> provider) {
        return getScopedObject(new ScopeKey<T>(clazz), provider);
    }

    @Override
    public <T> T getScopedObject(ScopeKey<T> key, Provider<T> unscoped) {
        // if values is null return null otherwise return the stored value of one exists
        if (!values.containsKey(key)) {
            T object = unscoped.get();
            values.put(key, object);
        }
        return (T) values.get(key);
    }
}
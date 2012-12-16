/**
 * Copyright 2012 John Ericksen
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

import com.google.inject.Key;
import com.google.inject.Provider;
import org.androidtransfuse.config.EnterableScope;

import java.util.HashMap;

public class TestingScope implements EnterableScope {

    private HashMap<Key<?>, Object> values = new HashMap<Key<?>, Object>();

    public void enter() {//noop
    }

    public void exit() {//noop
    }

    public <T> void seed(Key<T> key, T value) {
        values.put(key, value);
    }

    public <T> void seed(Class<T> clazz, T value) {
        seed(Key.get(clazz), value);
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T get() {
                // if values is null return null otherwise return the stored value of one exists
                if (!values.containsKey(key)) {
                    T object = unscoped.get();
                    values.put(key, object);
                }
                return (T) values.get(key);
            }
        };
    }
}
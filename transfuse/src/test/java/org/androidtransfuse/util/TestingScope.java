/**
 * Copyright 2011-2015 John Ericksen
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

    @Override
    public void enter() {//noop
    }

    @Override
    public void exit() {//noop
    }

    @Override
    public <T> void seed(ScopeKey<T> key, T value) {
        values.put(key, value);
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
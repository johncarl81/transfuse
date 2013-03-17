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

import javax.inject.Provider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Encapsulates a scope map utilizing the double-check locking algorithm.
 *
 * @author John Ericksen
 */
public class ConcurrentDoubleLockingScope implements Scope {

    private final ConcurrentMap<ScopeKey, Object> singletonMap = new ConcurrentHashMap<ScopeKey, Object>();

    @Override
    public <T> T getScopedObject(ScopeKey<T> key, Provider<T> provider) {
        Object result = singletonMap.get(key);
        if (result == null) {
            Object value = provider.get();
            result = singletonMap.putIfAbsent(key, value);
            if (result == null) {
                result = value;
            }
        }

        return (T) result;
    }
}

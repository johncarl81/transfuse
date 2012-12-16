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
package org.androidtransfuse.scope;

import javax.inject.Provider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author John Ericksen
 */
public class ConcurrentDoubleLockingScope implements Scope {

    private final ConcurrentMap<Class, Object> singletonMap = new ConcurrentHashMap<Class, Object>();

    @Override
    public <T> T getScopedObject(Class<T> clazz, Provider<T> provider) {
        Object result = singletonMap.get(clazz);
        if (result == null) {
            Object value = provider.get();
            result = singletonMap.putIfAbsent(clazz, value);
            if (result == null) {
                result = value;
            }
        }

        return (T) result;
    }
}

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

import org.androidtransfuse.Factories;
import org.androidtransfuse.scope.Scopes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author John Ericksen
 */
public class FactoryBuilderReflectionProxy<T> implements Factories.FactoryBuilder<T> {

    private final T instance;
    private final Constructor<T> scopesConstructor;

    public FactoryBuilderReflectionProxy(Class<T> factoryClass) {
        try {
            this.instance = factoryClass.newInstance();
            this.scopesConstructor = factoryClass.getConstructor(Scopes.class);
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create Factory Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create Factory Type", e);
        } catch (NoSuchMethodException e) {
            throw new TransfuseRuntimeException("Unable to get Factory constructor", e);
        }
    }

    @Override
    public T get() {
        return instance;
    }

    @Override
    public T get(Scopes scopes) {
        try {
            return scopesConstructor.newInstance(scopes);
        } catch (InstantiationException e) {
            throw new TransfuseRuntimeException("Unable to create Factory Type", e);
        } catch (IllegalAccessException e) {
            throw new TransfuseRuntimeException("Unable to create Factory Type", e);
        } catch (InvocationTargetException e) {
            throw new TransfuseRuntimeException("Unable to create Factory Type", e);
        }
    }
}

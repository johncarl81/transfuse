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

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public final class Providers {

    public static final String OF_METHOD = "of";

    private Providers(){
        // private utility constructor
    }

    public static <T> Provider<T> of(T instance){
        return new InstanceProvider<T>(instance);
    }

    private static final class InstanceProvider<T> implements Provider<T>{

        private T instance;

        private InstanceProvider(T instance) {
            this.instance = instance;
        }

        @Override
        public T get() {
            return instance;
        }
    }
}

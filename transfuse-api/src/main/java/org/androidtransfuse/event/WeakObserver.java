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
package org.androidtransfuse.event;

import java.lang.ref.WeakReference;

/**
 * Weak referencing Event Observer. Will trigger an event on the given reference only if the referenced
 * object exists.
 *
 * @author John Ericksen
 */
public abstract class WeakObserver<E, T> implements EventObserver<E>{

    private final WeakReference<T> reference;

    public WeakObserver(T target){
        reference = new WeakReference<T>(target);
    }

    @Override
    public void trigger(E event) {
        T handle = reference.get();
        if(handle != null){
            trigger(event, handle);
        }
    }

    public abstract void trigger(E event, T handle);
}

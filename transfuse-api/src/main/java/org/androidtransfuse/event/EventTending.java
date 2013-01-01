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
package org.androidtransfuse.event;

import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.OnRestart;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Coordinates registration/unregistration of observers to events based on Android lifecycle
 *
 * @author John Ericksen
 */
public class EventTending {

    public static final String REGISTER_METHOD = "register";

    private final Map<Class, EventObserver> eventObservers = new HashMap<Class, EventObserver>();
    private final EventManager eventManager;

    @Inject
    public EventTending(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public final <T> void register(Class<T> event, EventObserver<T> observer){
        eventObservers.put(event, observer);
    }

    @OnRestart
    @OnCreate
    public final void register(){
        for (Map.Entry<Class, EventObserver> observerEntry : eventObservers.entrySet()) {
            eventManager.register(observerEntry.getKey(), observerEntry.getValue());
        }
    }

    @OnPause
    public final void unregister(){
        for (Map.Entry<Class, EventObserver> observerEntry : eventObservers.entrySet()) {
            eventManager.unregister(observerEntry.getValue());
        }
    }
}


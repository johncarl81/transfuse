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

import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.OnResume;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Coordinates registration/unregistration of observers to events based on Android lifecycle using Transfuse
 * event lifecycle annotations.
 *
 * @author John Ericksen
 */
@Deprecated
public final class EventTending {

    public static final String ADD_OBSERVER_METHOD = "addObserver";

    private final Map<Class, EventObserver> eventObservers = new HashMap<Class, EventObserver>();
    private final EventManager eventManager;

    @Inject
    public EventTending(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * Associate an EventObserver with an Event by Class.
     *
     * @param event class
     * @param observer EventObserver
     * @param <T> relating Type
     */
    public <T> void addObserver(Class<T> event, EventObserver<T> observer){
        eventObservers.put(event, observer);
    }

    /**
     * Register the observers defined in the addObserver() method with the given EventManager.
     */
    @OnResume
    public void register(){
        for (Map.Entry<Class, EventObserver> observerEntry : eventObservers.entrySet()) {
            eventManager.register(observerEntry.getKey(), observerEntry.getValue());
        }
    }

    /**
     * Unregister the observers defined in teh addObserver() method with the given EventManager.
     */
    @OnPause
    public void unregister(){
        for (Map.Entry<Class, EventObserver> observerEntry : eventObservers.entrySet()) {
            eventManager.unregister(observerEntry.getValue());
        }
    }
}


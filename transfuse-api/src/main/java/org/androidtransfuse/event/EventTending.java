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


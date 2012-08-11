package org.androidtransfuse.event;

import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.OnRestart;

/**
 * Coordinates registration/unregistration of observers to events based on Android lifecycle
 *
 * @author John Ericksen
 */
public class EventTending<T> {

    private final Class<T> event;
    private final EventObserver<T> observer;
    private final EventManager eventManager;

    public EventTending(Class<T> event, EventObserver<T> observer, EventManager eventManager) {
        this.event = event;
        this.observer = observer;
        this.eventManager = eventManager;
        register();
    }

    @OnPause
    public void unregister(){
        eventManager.unregister(observer);
    }

    @OnRestart
    public final void register(){
        eventManager.register(event, observer);
    }
}


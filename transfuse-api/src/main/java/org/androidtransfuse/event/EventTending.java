package org.androidtransfuse.event;

import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.OnRestart;

/**
 * @author John Ericksen
 */
public class EventTending<T> {

    private Class<T> event;
    private EventObserver<T> observer;
    private EventManager eventManager;

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


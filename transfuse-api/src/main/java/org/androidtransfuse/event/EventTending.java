package org.androidtransfuse.event;

import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.annotations.OnRestart;

/**
 * Coordinates registration/unregistration of observers to events based on Android lifecycle
 *
 * @author John Ericksen
 */
public class EventTending {

    private final EventObserverTuple[] observerTuples;
    private final EventManager eventManager;

    public EventTending(EventObserverTuple[] observerTuples, EventManager eventManager) {
        this.observerTuples = observerTuples;
        this.eventManager = eventManager;
        register();
    }

    @OnPause
    public void unregister(){
        for (EventObserverTuple observerTuple : observerTuples) {
            eventManager.unregister(observerTuple.getObserver());
        }
    }

    @OnRestart
    public final void register(){
        for (EventObserverTuple observerTuple : observerTuples) {
            eventManager.register(observerTuple.getEvent(), observerTuple.getObserver());
        }
    }
}


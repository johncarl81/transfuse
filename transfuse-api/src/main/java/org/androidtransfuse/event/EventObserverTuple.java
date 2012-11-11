package org.androidtransfuse.event;

/**
 * Tuple to associate an event with an event observer
 *
 * @author John Ericksen
 */
public class EventObserverTuple<T>{
    private final Class<T> event;
    private final EventObserver<T> observer;

    public EventObserverTuple(Class<T> event, EventObserver<T> observer) {
        this.event = event;
        this.observer = observer;
    }

    public Class<T> getEvent() {
        return event;
    }

    public EventObserver<T> getObserver() {
        return observer;
    }
}
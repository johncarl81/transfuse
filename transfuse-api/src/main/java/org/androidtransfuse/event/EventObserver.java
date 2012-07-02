package org.androidtransfuse.event;

/**
 * @author John Ericksen
 */
public interface EventObserver<T> {

    String TRIGGER = "trigger";

    void trigger(T object);
}

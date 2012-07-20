package org.androidtransfuse.event;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class EventManagerProvider implements Provider<EventManager> {

    private static final EventManager eventManager = new EventManager();

    @Override
    public EventManager get() {
        return eventManager;
    }
}

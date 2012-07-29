package org.androidtransfuse.event;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class EventManagerProvider implements Provider<EventManager> {

    private static final EventManager EVENT_MANAGER = new EventManager();

    @Override
    public EventManager get() {
        return EVENT_MANAGER;
    }
}

package org.androidtransfuse.event;

import javax.inject.Provider;

/**
 * Shared common provider for default singleton scoped EventManager
 *
 * @author John Ericksen
 */
public class EventManagerProvider implements Provider<EventManager> {

    private static final EventManager EVENT_MANAGER = new EventManager();

    @Override
    public EventManager get() {
        return EVENT_MANAGER;
    }
}

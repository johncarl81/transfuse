package org.androidtransfuse.event;

import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class EventManagerProvider implements Provider<EventManager> {
    @Override
    public EventManager get() {
        return new EventManager();
    }
}

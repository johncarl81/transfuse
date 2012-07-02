package org.androidtransfuse.event;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class EventObserverTest {

    @Test
    public void verifyMethodName() throws NoSuchMethodException {
        Method triggerMethod = EventObserver.class.getMethod(EventObserver.TRIGGER, Object.class);
        assertNotNull(triggerMethod);
    }
}

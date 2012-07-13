package org.androidtransfuse.event;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class EventTendingTest {

    private EventTending<Object> eventTending;
    private EventObserver<Object> mockEventObserver;
    private EventManager mockEventManager;

    @Before
    public void setup(){
        mockEventManager = mock(EventManager.class);
        mockEventObserver = mock(EventObserver.class);

        eventTending = new EventTending<Object>(Object.class, mockEventObserver, mockEventManager);
    }

    @Test
    public void testRegister(){
        eventTending.register();

        verify(mockEventManager, times(2)).register(any(Class.class), eq(mockEventObserver));
    }

    @Test
    public void testUnregister(){
        eventTending.unregister();

        verify(mockEventManager).register(any(Class.class), eq(mockEventObserver));
        verify(mockEventManager).unregister(mockEventObserver);
    }
}

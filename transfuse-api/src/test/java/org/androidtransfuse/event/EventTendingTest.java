package org.androidtransfuse.event;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class EventTendingTest {

    private EventTending eventTending;
    private EventObserver<Object> mockEventObserver;
    private EventManager mockEventManager;

    @Before
    public void setup(){
        mockEventManager = mock(EventManager.class);
        mockEventObserver = mock(EventObserver.class);

        eventTending = new EventTending(mockEventManager);

        eventTending.register(Object.class, mockEventObserver);

    }

    @Test
    public void testRegister(){
        eventTending.register();

        verify(mockEventManager).register(any(Class.class), eq(mockEventObserver));
    }

    @Test
    public void testUnregister(){
        eventTending.unregister();

        verify(mockEventManager).unregister(mockEventObserver);
    }

    @Test
    public void testRegisterMethod() throws NoSuchMethodException {

        Method registerMethod = EventTending.class.getMethod(EventTending.REGISTER_METHOD, Class.class, EventObserver.class);
        assertNotNull(registerMethod);
    }
}

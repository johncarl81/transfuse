/**
 * Copyright 2013 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.event;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
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

        eventTending.addObserver(Object.class, mockEventObserver);

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

        Method registerMethod = EventTending.class.getMethod(EventTending.ADD_OBSERVER_METHOD, Class.class, EventObserver.class);
        assertNotNull(registerMethod);
    }
}

/**
 * Copyright 2011-2015 John Ericksen
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

import org.androidtransfuse.annotations.Observes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
public class EventManagerTest {

    private EventManager eventManager;
    private ObserverTarget target;

    private class ObservesEvent{}

    private class ObserverTarget{

        private ObservesEvent event;
        private Object objectEvent;

        public void observes(@Observes ObservesEvent event){
            this.event = event;
        }

        public void observes(@Observes Object event){
            this.objectEvent = event;
        }

        public ObservesEvent getEvent() {
            return event;
        }

        public Object getObjectEvent() {
            return objectEvent;
        }
    }

    private class TargetEventTriggered implements EventObserver<ObservesEvent>{
        private boolean triggered = false;
        @Override
        public void trigger(ObservesEvent event) {
            triggered = true;
        }

        public boolean isTriggered() {
            return triggered;
        }
    }

    @Before
    public void setup(){
        eventManager = new EventManager();
        target = new ObserverTarget();
    }

    @Test
    public void testRegistration(){
        registerEvents();

        ObservesEvent event = new ObservesEvent();
        eventManager.trigger(event);

        assertEquals(event, target.getEvent());
        assertEquals(event, target.getObjectEvent());
    }

    @Test
    public void testEventHierarchyTrigger(){
        registerEvents();

        Object event = new Object();
        eventManager.trigger(event);

        assertNull(target.getEvent());
        assertEquals(event, target.getObjectEvent());
    }

    @Test
    public void testUnregister(){
        TargetEventTriggered trigger = new TargetEventTriggered();
        eventManager.register(ObservesEvent.class, trigger);
        eventManager.unregister(trigger);

        eventManager.trigger(new ObservesEvent());

        assertFalse(trigger.isTriggered());
    }

    private void registerEvents(){
        eventManager.register(ObservesEvent.class, new EventObserver<ObservesEvent>() {
            @Override
            public void trigger(ObservesEvent event) {
                target.observes(event);
            }
        });
        eventManager.register(Object.class, new EventObserver<Object>() {
            @Override
            public void trigger(Object event) {
                target.observes(event);
            }
        });
    }
}

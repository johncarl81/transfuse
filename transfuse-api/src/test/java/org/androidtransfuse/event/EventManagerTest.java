package org.androidtransfuse.event;

import org.androidtransfuse.annotations.Observes;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

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

        public void observes(@Observes ObservesEvent event){
            this.event = event;
        }

        public ObservesEvent getEvent() {
            return event;
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
        eventManager.register(ObservesEvent.class, new EventObserver<ObservesEvent>() {
            @Override
            public void trigger(ObservesEvent event) {
                target.observes(event);
            }
        });

        ObservesEvent event = new ObservesEvent();

        eventManager.trigger(event);

        assertEquals(event, target.getEvent());
    }

    @Test
    public void testUnregister(){
        TargetEventTriggered trigger = new TargetEventTriggered();
        eventManager.register(ObservesEvent.class, trigger);
        eventManager.unregister(trigger);

        eventManager.trigger(new ObservesEvent());

        assertFalse(trigger.isTriggered());
    }

    @Test
    public void verifyMethods() throws NoSuchMethodException {
        Method triggerMethod = EventManager.class.getMethod(EventManager.TRIGGER_METHOD, Object.class);
        assertNotNull(triggerMethod);
        Method registerMethod = EventManager.class.getMethod(EventManager.REGISTER_METHOD, Class.class, EventObserver.class);
        assertNotNull(registerMethod);
    }
}

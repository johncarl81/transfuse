package org.androidtransfuse.integrationTest.observes;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.event.EventManagerProvider;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.scope.SingletonScope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class EventObserverTest {

    private EventObserver eventObserver;
    private EventManager eventManager;
    private EventObserverActivity eventObserverActivity;

    @Before
    public void setup() {
        eventObserverActivity = new EventObserverActivity();
        eventObserverActivity.onCreate(null);

        eventManager = SingletonScope.getInstance().getScopedObject(EventManager.class, new EventManagerProvider());

        eventObserver = DelegateUtil.getDelegate(eventObserverActivity, EventObserver.class);
    }

    @Test
    public void testEventOne(){
        assertFalse(eventObserver.isEventOneTriggered());
        eventManager.trigger(new EventOne("test"));
        assertTrue(eventObserver.isEventOneTriggered());
    }

    @Test
    public void testEventTwo(){
        assertFalse(eventObserver.isEventTwoTriggered());
        eventManager.trigger(new EventTwo());
        assertTrue(eventObserver.isEventTwoTriggered());
    }

    @Test
    public void testSingletonObserver(){
        SingletonObserver singletonObserver = SingletonScope.getInstance().getScopedObject(SingletonObserver.class, new SingletonObserver_Provider());
        assertFalse(singletonObserver.isObservedEventThree());
        eventManager.trigger(new EventThree());
        assertTrue(singletonObserver.isObservedEventThree());
    }

    @Test
    public void testReregister(){
        eventObserverActivity.onPause();
        assertFalse(eventObserver.isEventTwoTriggered());
        eventManager.trigger(new EventTwo());
        assertFalse(eventObserver.isEventTwoTriggered());
        eventObserverActivity.onRestart();
        assertFalse(eventObserver.isEventTwoTriggered());
        eventManager.trigger(new EventTwo());
        assertTrue(eventObserver.isEventTwoTriggered());
    }


}

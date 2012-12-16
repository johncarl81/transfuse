/**
 * Copyright 2012 John Ericksen
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

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class WeakObserverTest {

    private EventManager eventManager;
    private class Event{}

    private class WeakObservable{
        private boolean called = false;

        public boolean isCalled() {
            return called;
        }

        public void setCalled(boolean called) {
            this.called = called;
        }
    }

    private class WeakObserverTarget extends WeakObserver<Event, WeakObservable>{

        public WeakObserverTarget(WeakObservable target) {
            super(target);
        }

        @Override
        public void trigger(Event event, WeakObservable handle) {

            handle.setCalled(true);
        }
    }

    @Before
    public void setup(){
        eventManager = new EventManager();
    }

    @Test
    public void deferenceTest(){

        WeakReference<WeakObservable> reference;
        WeakObserverTarget weakObserverTarget;

        WeakObservable observable = new WeakObservable();
        weakObserverTarget = new WeakObserverTarget(observable);
        reference = new WeakReference<WeakObservable>(observable);

        eventManager.register(Event.class, weakObserverTarget);

        assertFalse(observable.isCalled());

        eventManager.trigger(new Event());

        assertTrue(observable.isCalled());

        observable.setCalled(false);

        assertFalse(observable.isCalled());

        observable = null;

        for(int i = 0; i < 10 && reference.get() != null; i++){
            System.gc();
        }

        eventManager.trigger(new Event());

    }

    @Test
    public void verifyMethod() throws NoSuchMethodException {
        Method triggerMethod = WeakObserver.class.getMethod(EventObserver.TRIGGER, Object.class, Object.class);
        assertNotNull(triggerMethod);
    }
}

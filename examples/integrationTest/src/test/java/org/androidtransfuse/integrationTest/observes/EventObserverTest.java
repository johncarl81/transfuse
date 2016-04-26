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
package org.androidtransfuse.integrationTest.observes;

import org.androidtransfuse.event.EventManager;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.scope.ScopeKey;
import org.androidtransfuse.scope.ScopesUtil;
import org.androidtransfuse.util.Providers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.inject.Singleton;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class EventObserverTest {

    private EventObserver eventObserver;
    private EventManager eventManager;
    private EventObserverActivity eventObserverActivity;
    private SingletonObserver singletonObserver;

    @Before
    public void setup() {
        eventObserverActivity = Robolectric.buildActivity(EventObserverActivity.class).create().resume().get();

        eventManager = ScopesUtil.getInstance().getScope(Singleton.class).getScopedObject(ScopeKey.of(EventManager.class), Providers.of(new EventManager()));

        eventObserver = DelegateUtil.getDelegate(eventObserverActivity, EventObserver.class);

        singletonObserver = ScopesUtil.getInstance().getScope(Singleton.class).getScopedObject(ScopeKey.of(SingletonObserver.class), Providers.of(new SingletonObserver()));
        singletonObserver.reset();
    }

    @Test
    public void testEventOne() {
        assertFalse(eventObserver.isEventOneTriggered());
        assertFalse(singletonObserver.isObservedAll());
        eventManager.trigger(new EventOne("test"));
        assertTrue(eventObserver.isEventOneTriggered());
        assertTrue(singletonObserver.isObservedAll());
    }

    @Test
    public void testEventTwo() {
        assertFalse(eventObserver.isEventTwoTriggered());
        assertFalse(singletonObserver.isObservedAll());
        eventManager.trigger(new EventTwo());
        assertTrue(eventObserver.isEventTwoTriggered());
        assertTrue(singletonObserver.isObservedAll());
    }

    @Test
    public void testSingletonObserver() {
        assertFalse(singletonObserver.isObservedEventThree());
        assertFalse(singletonObserver.isObservedAll());
        eventManager.trigger(new EventThree());
        assertTrue(singletonObserver.isObservedEventThree());
        assertTrue(singletonObserver.isObservedAll());
    }

    @Test
    public void testReregister() {
        eventObserverActivity.onPause();
        assertFalse(singletonObserver.isObservedAll());
        assertFalse(eventObserver.isEventTwoTriggered());
        eventManager.trigger(new EventTwo());
        assertFalse(eventObserver.isEventTwoTriggered());
        eventObserverActivity.onResume();
        assertFalse(eventObserver.isEventTwoTriggered());
        eventManager.trigger(new EventTwo());
        assertTrue(eventObserver.isEventTwoTriggered());
        assertTrue(singletonObserver.isObservedAll());
    }


}

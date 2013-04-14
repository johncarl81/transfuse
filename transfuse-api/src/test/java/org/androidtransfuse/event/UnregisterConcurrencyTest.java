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

import org.androidtransfuse.annotations.Observes;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases highlighting concurrency when registering, unregistering and triggering on events.
 *
 * @author John Ericksen
 */
public class UnregisterConcurrencyTest {

    private static final String EVENT = "event";
    private static final int INPUT_COUNT = 100;
    private static final int WAIT_PERIOD = 1000;

    private EventManager eventManager;
    private ExecutorService executorService;

    public abstract class RegisterableBase {
        private boolean registered = false;
        private boolean unregistered = false;

        public synchronized void register() {
            registered = true;
            eventManager.register(String.class, getEventObserver());
        }

        public synchronized void unregister() {
            if (registered) {
                eventManager.unregister(getEventObserver());
                unregistered = true;
            }
        }

        public boolean isUnregistered() {
            return unregistered;
        }

        public abstract EventObserver<String> getEventObserver();
    }

    public class DeadlockTrigger extends RegisterableBase {
        private int count = 0;
        private EventObserver<String> eventObserver = new WeakObserver<String, DeadlockTrigger>(this) {
            @Override
            public void trigger(String event, DeadlockTrigger handle) {
                handle.deadlock(event);
            }
        };

        @Observes
        public void deadlock(String event) {
            if (count < 2) {
                sleep();
                eventManager.trigger(event);
            }
            count++;
        }

        @Override
        public EventObserver<String> getEventObserver() {
            return eventObserver;
        }
    }

    public class EventWatcher extends RegisterableBase {

        private boolean calledAfterUnregister = false;

        private EventObserver<String> eventObserver = new WeakObserver<String, EventWatcher>(this) {
            @Override
            public void trigger(String event, EventWatcher handle) {
                handle.event(event);
            }
        };


        public void event(@Observes String event) {
            sleep();
            calledAfterUnregister = isUnregistered();
        }

        public boolean isCalledAfterUnregister() {
            return calledAfterUnregister;
        }

        @Override
        public EventObserver<String> getEventObserver() {
            return eventObserver;
        }
    }

    public class EventRegistration implements Runnable {

        private RegisterableBase watcher;

        public EventRegistration(RegisterableBase watcher) {
            this.watcher = watcher;
        }

        @Override
        public void run() {
            watcher.register();
        }
    }

    public class EventUnregistration implements Runnable {

        private RegisterableBase watcher;

        public EventUnregistration(RegisterableBase watcher) {
            this.watcher = watcher;
        }

        @Override
        public void run() {
            watcher.unregister();
        }
    }

    public class EventTrigger implements Runnable {
        @Override
        public void run() {
            eventManager.trigger(EVENT);
        }
    }

    @Before
    public void setup() {
        eventManager = new EventManager();
        executorService = Executors.newFixedThreadPool(4);
    }

    @Test
    public void hammerLifecycleTest() throws InterruptedException {

        List<EventWatcher> registrations = new ArrayList<EventWatcher>();

        for (int i = 0; i < INPUT_COUNT; i++) {
            EventWatcher eventWatcher = new EventWatcher();
            registrations.add(eventWatcher);
            executorService.execute(new EventRegistration(eventWatcher));
            executorService.execute(new EventUnregistration(eventWatcher));
            executorService.execute(new EventTrigger());
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        for (EventWatcher registration : registrations) {
            assertFalse(registration.isCalledAfterUnregister());
        }
    }

    /**
     * Exercises possible deadlock conditions where an event triggers itself, both during registration and during
     * event posting.
     *
     * @throws InterruptedException
     */
    @Test
    public void deadlockTest() throws InterruptedException {

        for (int i = 0; i < INPUT_COUNT; i++) {
            final DeadlockTrigger deadlockTrigger = new DeadlockTrigger();
            executorService.execute(new EventRegistration(deadlockTrigger));
            executorService.execute(new EventUnregistration(deadlockTrigger));
            executorService.execute(new EventTrigger());
        }

        // Wait 100ms for threads to finish.  If not finished, assume deadlock.
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(WAIT_PERIOD, TimeUnit.MILLISECONDS));
    }

    public class DeadlockPair extends RegisterableBase {

        private DeadlockPair deadlockPair = null;

        private EventObserver<String> eventObserver = new WeakObserver<String, DeadlockPair>(this) {
            @Override
            public void trigger(String event, DeadlockPair handle) {
                handle.event(event);
            }
        };

        @Observes
        public void event(String event) {
            if (deadlockPair != null) {
                sleep();
                deadlockPair.unregister();
            }
        }

        public void setDeadlockPair(DeadlockPair deadlockPair) {
            this.deadlockPair = deadlockPair;
        }

        @Override
        public EventObserver<String> getEventObserver() {
            return eventObserver;
        }
    }

    @Test
    @Ignore
    public void deadlockPairTest() throws InterruptedException {

        for (int i = 0; i < INPUT_COUNT; i++) {
            DeadlockPair pair1 = new DeadlockPair();
            DeadlockPair pair2 = new DeadlockPair();

            pair1.setDeadlockPair(pair2);
            pair2.setDeadlockPair(pair1);

            executorService.execute(new EventRegistration(pair1));
            executorService.execute(new EventTrigger());
        }

        // Wait 100ms for threads to finish.  If not finished, assume deadlock.
        executorService.shutdown();
        assertTrue(executorService.awaitTermination(WAIT_PERIOD, TimeUnit.MILLISECONDS));
    }

    private static void sleep() {
        // Adding some time to allow unregisters to happen often.
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package org.androidtransfuse.event;

import org.androidtransfuse.annotations.Observes;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertFalse;

/**
 * Test cases highlighting concurrency when registering, unregistering and triggering on events.
 *
 * @author John Ericksen
 */
public class UnregisterConcurrencyTest {

  private static final String EVENT = "event";

  private EventManager eventManager;

  public class EventWatcher{
    private boolean registered = false;
    private boolean unregistered = false;
    private boolean calledAfterUnregister = false;
    private EventObserver eventObserver = new WeakObserver<String, EventWatcher>(this) {
      @Override
      public void trigger(String event, EventWatcher handle) {
        handle.event(event);
      }
    };

    public void event(@Observes String event) {
      try {
        // Adding some time to allow unregisters to happen often.
        Thread.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      calledAfterUnregister = unregistered;
    }

    public synchronized void register() {
        registered = true;
        eventManager.register(String.class, eventObserver);
    }

    public synchronized void unregister() {
      if(registered) {
        eventManager.unregister(eventObserver);
        unregistered = true;
      }
    }

    public boolean isCalledAfterUnregister() {
      return calledAfterUnregister;
    }
  }

  public class EventRegistration implements Runnable {

    private EventWatcher watcher;

    public EventRegistration(EventWatcher watcher) {
      this.watcher = watcher;
    }

    @Override public void run() {
      watcher.register();
    }
  }

  public class EventUnregistration implements Runnable {

    private EventWatcher watcher;

    public EventUnregistration(EventWatcher watcher) {
      this.watcher = watcher;
    }

    @Override public void run() {
      watcher.unregister();
    }
  }

  public class EventTrigger implements Runnable {
    @Override public void run() {
      eventManager.trigger(EVENT);
    }
  }

  @Before
  public void setup() {
    eventManager = new EventManager();
  }

  @Test
  public void hammerLifecycleTest() throws InterruptedException {

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    List<EventWatcher> registrations = new ArrayList<EventWatcher>();

    for(int i = 0; i < 100; i++) {
      EventWatcher eventWatcher = new EventWatcher();
      registrations.add(eventWatcher);
      executorService.execute(new EventRegistration(eventWatcher));
      executorService.execute(new EventUnregistration(eventWatcher));
      executorService.execute(new EventTrigger());
    }

    executorService.shutdown();
    while (!executorService.awaitTermination(10, TimeUnit.MILLISECONDS)) {}

    for (EventWatcher registration : registrations) {
      assertFalse(registration.isCalledAfterUnregister());
    }
  }
}

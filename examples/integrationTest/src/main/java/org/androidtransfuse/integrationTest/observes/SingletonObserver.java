package org.androidtransfuse.integrationTest.observes;

import android.util.Log;
import org.androidtransfuse.annotations.Observes;

import javax.inject.Singleton;

/**
 * @author John Ericksen
 */
@Singleton
public class SingletonObserver {

    private boolean observedEventThree = false;
    private boolean observedAll = false;

    public void observeEventThree(@Observes EventThree event) {
        Log.i("Event Test", "event three");
        observedEventThree = true;
    }

    public void observeEverything(@Observes Object event) {
        observedAll = true;
    }

    public boolean isObservedEventThree() {
        return observedEventThree;
    }

    public boolean isObservedAll() {
        return observedAll;
    }

    public void reset() {
        observedAll = false;
    }
}

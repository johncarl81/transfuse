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

    public void observeEventThree(@Observes EventThree event){
        Log.i("Event Test", "event three");
        observedEventThree = true;
    }

    public boolean isObservedEventThree() {
        return observedEventThree;
    }
}

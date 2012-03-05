package org.androidtransfuse.example.simple;

import android.util.Log;
import org.androidtransfuse.annotations.Application;
import org.androidtransfuse.annotations.OnCreate;
import org.androidtransfuse.annotations.OnLowMemory;
import org.androidtransfuse.annotations.OnTerminate;

/**
 * @author John Ericksen
 */
@Application(name = "SimpleApp", label = "Simple")
public class SimpleApplication {

    private boolean onCreateCalled = false;
    private boolean onTerminateCalled = false;
    private boolean onLowMemoryCalled = false;

    @OnCreate
    public void onCreate() {
        onCreateCalled = true;
    }

    @OnTerminate
    public void onTerminate() {
        onTerminateCalled = true;
    }

    @OnTerminate
    public void logTermination() {
        Log.i("termination", "terminate called");
    }

    @OnLowMemory
    public void onLowMemory() {
        onLowMemoryCalled = true;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }

    public boolean isOnTerminateCalled() {
        return onTerminateCalled;
    }

    public boolean isOnLowMemoryCalled() {
        return onLowMemoryCalled;
    }
}

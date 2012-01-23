package org.androidrobotics.example.simple;

import android.util.Log;
import org.androidrobotics.annotations.Application;
import org.androidrobotics.annotations.OnCreate;
import org.androidrobotics.annotations.OnLowMemory;
import org.androidrobotics.annotations.OnTerminate;

/**
 * @author John Ericksen
 */
@Application(name = "SimpleApp")
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

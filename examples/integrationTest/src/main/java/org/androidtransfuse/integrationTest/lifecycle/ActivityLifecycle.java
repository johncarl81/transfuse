package org.androidtransfuse.integrationTest.lifecycle;

import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
@Activity(name = "ActivityLifecycleActivity")
@Layout(R.layout.main)
public class ActivityLifecycle {

    private boolean onCreate;
    private boolean onDestroy;
    private boolean onStop;
    private boolean onPause;
    private boolean onResume;
    private boolean onStart;
    private boolean onRestart;

    @OnCreate
    public void onCreate() {
        onCreate = true;
    }

    @OnDestroy
    public void onDestroy() {
        onDestroy = true;
    }

    @OnStop
    public void onStop() {
        onStop = true;
    }

    @OnPause
    public void onPause() {
        onPause = true;
    }

    @OnResume
    public void onResume() {
        onResume = true;
    }

    @OnStart
    public void onStart() {
        onStart = true;
    }

    @OnRestart
    public void onRestart() {
        onRestart = true;
    }

    public boolean isOnCreate() {
        return onCreate;
    }

    public boolean isOnDestroy() {
        return onDestroy;
    }

    public boolean isOnStop() {
        return onStop;
    }

    public boolean isOnPause() {
        return onPause;
    }

    public boolean isOnResume() {
        return onResume;
    }

    public boolean isOnStart() {
        return onStart;
    }

    public boolean isOnRestart() {
        return onRestart;
    }
}

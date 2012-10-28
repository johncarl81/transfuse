package org.androidtransfuse.integrationTest.lifecycle;

import android.os.Bundle;
import android.util.Log;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.integrationTest.R;

/**
 * @author John Ericksen
 */
@Activity(name = "ActivityLifecycleActivity", label = "Activity Lifecycle")
@Layout(R.layout.main)
public class ActivityLifecycle {

    private Bundle onCreateBundle;
    private boolean onDestroyCalled = false;
    private boolean onStopCalled = false;
    private boolean onPauseCalled = false;
    private boolean onResumeCalled = false;
    private boolean onStartCalled = false;
    private boolean onRestartCalled = false;

    @OnCreate
    public void onCreate(Bundle bundle) {
        Log.i("Lifecycle", "onCreate");
        onCreateBundle = bundle;
    }

    @OnDestroy
    protected void onDestroy() {
        Log.i("Lifecycle", "onDestroy");
        onDestroyCalled = true;
    }

    @OnStop
    private void onStop() {
        Log.i("Lifecycle", "onStop");
        onStopCalled = true;
    }

    @OnPause
    void onPause() {
        Log.i("Lifecycle", "onPause");
        onPauseCalled = true;
    }

    @OnResume
    public void onResume() {
        Log.i("Lifecycle", "onResume");
        onResumeCalled = true;
    }

    @OnStart
    public void onStart() {
        Log.i("Lifecycle", "onStart");
        onStartCalled = true;
    }

    @OnRestart
    public void onRestart() {
        Log.i("Lifecycle", "onRestart");
        onRestartCalled = true;
    }

    public Bundle getOnCreateBundle() {
        return onCreateBundle;
    }

    public boolean isOnDestroyCalled() {
        return onDestroyCalled;
    }

    public boolean isOnStopCalled() {
        return onStopCalled;
    }

    public boolean isOnPauseCalled() {
        return onPauseCalled;
    }

    public boolean isOnResumeCalled() {
        return onResumeCalled;
    }

    public boolean isOnStartCalled() {
        return onStartCalled;
    }

    public boolean isOnRestartCalled() {
        return onRestartCalled;
    }
}

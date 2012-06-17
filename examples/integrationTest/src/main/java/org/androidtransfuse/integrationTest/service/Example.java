package org.androidtransfuse.integrationTest.service;

import org.androidtransfuse.annotations.*;

/**
 * @author John Ericksen
 */
@Service
public class Example {

    private boolean onStartCalled = false;
    private boolean onDestroyCalled = false;
    private boolean onLowMemoryCalled = false;
    private boolean onRebindCalled = false;
    private boolean onCreateCalled = false;

    @OnCreate
    public void onCreate() {
        onCreateCalled = true;
    }

    @OnStart
    public void onStart() {
        onStartCalled = true;
    }

    @OnDestroy
    public void onDestroy() {
        onDestroyCalled = true;
    }

    @OnLowMemory
    public void onLowMemory() {
        onLowMemoryCalled = true;
    }

    @OnRebind
    public void onRebind() {
        onRebindCalled = true;
    }

    public boolean isOnStartCalled() {
        return onStartCalled;
    }

    public boolean isOnDestroyCalled() {
        return onDestroyCalled;
    }

    public boolean isOnLowMemoryCalled() {
        return onLowMemoryCalled;
    }

    public boolean isOnRebindCalled() {
        return onRebindCalled;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }
}

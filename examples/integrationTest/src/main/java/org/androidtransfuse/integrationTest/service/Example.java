package org.androidtransfuse.integrationTest.service;

import android.content.Intent;
import org.androidtransfuse.annotations.*;
import org.androidtransfuse.listeners.ServiceOnStartCommand;
import org.androidtransfuse.listeners.ServiceOnUnbind;

/**
 * @author John Ericksen
 */
@Service
@RegisterListener
public class Example implements ServiceOnStartCommand, ServiceOnUnbind {

    private boolean onStartCommandCalled = false;
    private boolean onDestroyCalled = false;
    private boolean onLowMemoryCalled = false;
    private boolean onRebindCalled = false;
    private boolean onCreateCalled = false;
    private boolean onConfigurationChangedCalled = false;
    private boolean onTrimMemoryCalled = false;
    private boolean onTaskRemoved = false;
    private boolean onUnbindCalled = false;

    @OnCreate
    public void onCreate() {
        onCreateCalled = true;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStartCommandCalled = true;
        return 0;
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

    @OnConfigurationChanged
    public void onConfigurationChanged(){
        onConfigurationChangedCalled = true;
    }

    @OnTrimMemory
    public void onTrimMemory(){
        onTrimMemoryCalled = true;
    }

    @OnTaskRemoved
    public void onTaskRemoved(){
        onTaskRemoved = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        onUnbindCalled = true;
        return false;
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

    public boolean isOnStartCommandCalled() {
        return onStartCommandCalled;
    }

    public boolean isOnConfigurationChangedCalled() {
        return onConfigurationChangedCalled;
    }

    public boolean isOnTrimMemoryCalled() {
        return onTrimMemoryCalled;
    }

    public boolean isOnTaskRemoved() {
        return onTaskRemoved;
    }

    public boolean isOnUnbindCalled() {
        return onUnbindCalled;
    }
}

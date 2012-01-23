package org.androidrobotics.example.simple;

import android.app.Application;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author John Ericksen
 */
public class SingletonProvider implements Provider<SingletonTarget> {

    @Inject
    private Application application;

    public SingletonTarget get() {
        return ((SimpleApp) application).getSingletonTarget();
    }

    /*private volatile SingletonTarget singletonTarget = null;

    @Override
    public SingletonTarget get() {
        SingletonTarget result = singletonTarget;
        if (result == null) {
            synchronized(this) {
                result = singletonTarget;
                if (result == null) {
                    singletonTarget = result = new SingletonTarget();
                }
            }
        }
        return result;
    }*/
}

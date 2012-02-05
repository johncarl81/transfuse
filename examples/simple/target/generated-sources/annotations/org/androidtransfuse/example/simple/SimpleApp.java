
package org.androidtransfuse.example.simple;

import android.app.Application;

public class SimpleApp
    extends Application
{

    private SimpleApplication simpleApplication_0;
    private SingletonTarget singleton;

    public void onCreate() {
        super.onCreate();
        simpleApplication_0 = new SimpleApplication();
        simpleApplication_0 .onCreate();
    }

    public void onLowMemory() {
        super.onLowMemory();
        simpleApplication_0 .onLowMemory();
    }

    public void onTerminate() {
        super.onTerminate();
        simpleApplication_0 .logTermination();
        simpleApplication_0 .onTerminate();
    }

    SingletonTarget getSingletonTarget() {
        if (singleton == null) {
            singleton = new SingletonTarget();
        }
        return singleton;
    }

}

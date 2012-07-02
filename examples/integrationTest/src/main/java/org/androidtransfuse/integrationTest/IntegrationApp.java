package org.androidtransfuse.integrationTest;

import android.content.res.Configuration;
import org.androidtransfuse.annotations.*;

/**
 * @author John Ericksen
 */
@Application(label = "Transfuse Integration Test")
public class IntegrationApp {

    private boolean onCreate;
    private boolean onLowMemory;
    private boolean onTerminate;
    private Configuration config;

    @OnCreate
    public void onCreate() {
        onCreate = true;
    }

    @OnLowMemory
    public void onLowMemroy() {
        onLowMemory = true;
    }

    @OnTerminate
    public void onTerminate() {
        onTerminate = true;
    }

    @OnConfigurationChanged
    public void onConfigurationChanged(Configuration config) {
        this.config = config;
    }

    public boolean isOnCreate() {
        return onCreate;
    }

    public boolean isOnLowMemory() {
        return onLowMemory;
    }

    public boolean isOnTerminate() {
        return onTerminate;
    }

    public Configuration getOnConfigurationChanged() {
        return config;
    }
}

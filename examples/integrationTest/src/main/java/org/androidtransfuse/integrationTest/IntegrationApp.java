/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.integrationTest;

import android.content.res.Configuration;
import org.androidtransfuse.annotations.*;

/**
 * @author John Ericksen
 */
@Application(label = "Transfuse Integration Test", icon = "@drawable/icon")
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
    public void onLowMemory() {
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

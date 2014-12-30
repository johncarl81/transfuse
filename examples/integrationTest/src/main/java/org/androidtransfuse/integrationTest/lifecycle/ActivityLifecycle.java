/**
 * Copyright 2011-2015 John Ericksen
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

    private static final String LIFECYCLE_LOG = "Lifecycle";

    private Bundle onCreateBundle;
    private boolean onDestroyCalled = false;
    private boolean onStopCalled = false;
    private boolean onPauseCalled = false;
    private boolean onResumeCalled = false;
    private boolean onStartCalled = false;
    private boolean onRestartCalled = false;

    @OnCreate
    public void onCreate(Bundle bundle) {
        Log.i(LIFECYCLE_LOG, "onCreate");
        onCreateBundle = bundle;
    }

    @OnDestroy
    protected void onDestroy() {
        Log.i(LIFECYCLE_LOG, "onDestroy");
        onDestroyCalled = true;
    }

    @OnStop
    private void onStop() {
        Log.i(LIFECYCLE_LOG, "onStop");
        onStopCalled = true;
    }

    @OnPause
    void onPause() {
        Log.i(LIFECYCLE_LOG, "onPause");
        onPauseCalled = true;
    }

    @OnResume
    public void onResume() {
        Log.i(LIFECYCLE_LOG, "onResume");
        onResumeCalled = true;
    }

    @OnStart
    public void onStart() {
        Log.i(LIFECYCLE_LOG, "onStart");
        onStartCalled = true;
    }

    @OnRestart
    public void onRestart() {
        Log.i(LIFECYCLE_LOG, "onRestart");
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

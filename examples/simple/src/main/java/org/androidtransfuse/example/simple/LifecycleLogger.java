package org.androidtransfuse.example.simple;

import android.util.Log;
import org.androidtransfuse.annotations.*;

/**
 * @author John Ericksen
 */
public class LifecycleLogger {

    private static final String TAG = "lifecycle";

    @OnCreate
    public void onCreate() {
        Log.i(TAG, "onCreate Called");
    }

    @OnDestroy
    public void onDestroy() {
        Log.i(TAG, "onDestory Called");
    }

    @OnStop
    public void onStop() {
        Log.i(TAG, "onStop Called");
    }

    @OnPause
    public void onPause() {
        Log.i(TAG, "onPause Called");
    }

    @OnResume
    public void onResume() {
        Log.i(TAG, "onResume Called");
    }

    @OnStart
    public void onStart() {
        Log.i(TAG, "onStart Called");
    }

    @OnRestart
    public void onRestart() {
        Log.i(TAG, "onRestart Called");
    }
}

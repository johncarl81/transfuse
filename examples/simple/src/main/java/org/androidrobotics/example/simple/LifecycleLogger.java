package org.androidrobotics.example.simple;

import android.util.Log;
import org.androidrobotics.annotations.*;

/**
 * @author John Ericksen
 */
public class LifecycleLogger {

    @OnCreate
    @LogInterception
    public void onCreate() {
        Log.i("lifecycle", "onCreate Called");
    }

    @OnDestroy
    public void onDestroy() {
        Log.i("lifecycle", "onDestory Called");
    }

    @OnStop
    public void onStop() {
        Log.i("lifecycle", "onStop Called");
    }

    @OnPause
    public void onPause() {
        Log.i("lifecycle", "onPause Called");
    }

    @OnResume
    public void onResume() {
        Log.i("lifecycle", "onResume Called");
    }

    @OnStart
    public void onStart() {
        Log.i("lifecycle", "onStart Called");
    }

    @OnRestart
    public void onRestart() {
        Log.i("lifecycle", "onRestart Called");
    }
}

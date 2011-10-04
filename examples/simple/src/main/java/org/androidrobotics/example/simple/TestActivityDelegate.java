package org.androidrobotics.example.simple;

import android.util.Log;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;

@Activity("TestActivity")
@Layout(R.layout.main)
public class TestActivityDelegate {

    private boolean onCreateCalled = false;

    @OnCreate
    public void callMe() {
        Log.i("info", "Called");
        onCreateCalled = true;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }
}

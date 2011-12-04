package org.androidrobotics.example.simple;

import android.util.Log;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;

import javax.inject.Inject;

@Activity("SimpleActivity")
@Layout(R.layout.main)
public class SimpleActivityDelegate {

    private boolean onCreateCalled = false;
    private boolean secondOnCreatCalled = false;

    @Inject
    private SimpleController controller;
    private ConstructorValue value;

    @Inject
    public SimpleActivityDelegate(ConstructorValue value) {
        this.value = value;
    }

    @OnCreate
    public void callMe() {
        Log.i("info", "Called");
        onCreateCalled = true;
    }

    @OnCreate
    public void anotherCall() {
        Log.i("test", "test");
        secondOnCreatCalled = true;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }

    public boolean isSecondOnCreatCalled() {
        return secondOnCreatCalled;
    }

    public SimpleController getController() {
        return controller;
    }

    public boolean isConstructorInjected() {
        return value != null;
    }
}

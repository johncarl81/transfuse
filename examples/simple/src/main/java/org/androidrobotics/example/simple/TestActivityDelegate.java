package org.androidrobotics.example.simple;

import android.util.Log;
import org.androidrobotics.annotations.Activity;
import org.androidrobotics.annotations.Layout;
import org.androidrobotics.annotations.OnCreate;

import javax.inject.Inject;

@Activity("TestActivity")
@Layout(R.layout.main)
public class TestActivityDelegate {

    private boolean onCreateCalled = false;
    private boolean secondOnCreatCalled = false;

    @Inject
    private TestController controller;
    private ContructorValue value;

    @Inject
    public TestActivityDelegate(ContructorValue value) {
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

    public TestController getController() {
        return controller;
    }

    public boolean isConstructorInjected() {
        return value != null;
    }
}

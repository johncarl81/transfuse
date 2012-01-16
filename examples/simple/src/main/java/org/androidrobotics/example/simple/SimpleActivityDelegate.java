package org.androidrobotics.example.simple;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Button;
import org.androidrobotics.annotations.*;

import javax.inject.Inject;

@Activity("SimpleActivity")
@Layout(R.layout.main)
public class SimpleActivityDelegate extends SimpleSuperClass {

    private boolean onCreateCalled = false;
    private boolean secondOnCreateCalled = false;

    @Inject
    private ProvidedValue providedValue;
    @Inject
    @Resource(R.integer.testInt)
    public int testInt;
    @Inject
    @Resource(R.string.hello)
    private String testHello;
    @Inject
    public SimpleController controller;
    private LifecycleLogger value;
    @Inject
    @SystemService(Context.AUDIO_SERVICE)
    private Object systemService;
    private LateReturnListener lateReturnListener;
    @Inject
    @View(R.id.asyncActivity)
    private Button button;
    @Inject
    private Resources resources;

    @Inject
    public SimpleActivityDelegate(LifecycleLogger value) {
        this.value = value;
    }

    @OnCreate
    public void registerLateReturnListener() {
        button.setOnClickListener(lateReturnListener);
    }

    @OnCreate
    public void callMe() {
        Log.i("info", "Called");
        onCreateCalled = true;
    }

    @OnCreate
    public void anotherCall() {
        Log.i("test", "test");
        secondOnCreateCalled = true;
    }

    @OnTouch
    @LogInterception
    protected void touch() {
    }

    @Inject
    private void setLateReturnListener(LateReturnListener lateReturnListener) {
        this.lateReturnListener = lateReturnListener;
    }

    public boolean isOnCreateCalled() {
        return onCreateCalled;
    }

    public boolean isSecondOnCreateCalled() {
        return secondOnCreateCalled;
    }

    public SimpleController getController() {
        return controller;
    }

    public boolean isConstructorInjected() {
        return value != null;
    }

    public boolean isSystemServiceInjected() {
        return systemService != null;
    }

    public ProvidedValue getProvidedValue() {
        return providedValue;
    }

    public Resources getResources() {
        return resources;
    }

    public String getTestHello() {
        return testHello;
    }
}

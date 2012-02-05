
package org.androidtransfuse.example.simple;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import org.androidtransfuse.aop.AsynchronousMethodInterceptor;
import org.androidtransfuse.aop.UIThreadMethodInterceptor;
import org.androidtransfuse.example.simple.R.integer;
import org.androidtransfuse.example.simple.R.string;
import org.androidtransfuse.util.InjectionUtil;

public class SimpleActivity
    extends Activity
{

    private LifecycleLogger lifecycleLogger_1;
    private org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_2;
    private org.androidtransfuse.example.simple.ValueProvider valueProvider_0;
    private org.androidtransfuse.example.simple.NotifyOnClickListener notifyOnClickListener_0;
    private org.androidtransfuse.example.simple.ValueProvider valueProvider_1;
    private SubComponent subComponent_0;
    private ProviderTest providerTest_0;
    private GotoSecondActivity gotoSecondActivity_0;
    private AnotherValueImpl anotherValueImpl_1;
    private org.androidtransfuse.example.simple.SingletonProvider singletonProvider_0;
    private org.androidtransfuse.example.simple.LoggingInterceptor loggingInterceptor_5;
    private VibrateOnClickListener_AOPProxy vibrateOnClickListener_AOPProxy_0;
    private org.androidtransfuse.example.simple.SimpleController simpleController_0;
    private Handler handler_0;
    private UIThreadMethodInterceptor uIThreadMethodInterceptor_2;
    private AsynchronousMethodInterceptor asynchronousMethodInterceptor_2;
    private org.androidtransfuse.example.simple.SingletonProvider singletonProvider_1;
    private LateReturnListener_AOPProxy lateReturnListener_AOPProxy_0;
    private SimpleActivityDelegate_AOPProxy simpleActivityDelegate_AOPProxy_0;

    public void onCreate(Bundle savedInstanceState) {
        long time;
        time = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        setContentView(2130903040);
        lifecycleLogger_1 = new LifecycleLogger();
        loggingInterceptor_2 = new org.androidtransfuse.example.simple.LoggingInterceptor();
        valueProvider_0 = new org.androidtransfuse.example.simple.ValueProvider();
        InjectionUtil.setField(valueProvider_0, 0, "application", this.getApplication());
        notifyOnClickListener_0 = new org.androidtransfuse.example.simple.NotifyOnClickListener();
        InjectionUtil.setField(notifyOnClickListener_0, 0, "context", this);
        InjectionUtil.setField(notifyOnClickListener_0, 0, "notificationManager", ((NotificationManager) this.getSystemService("notification")));
        valueProvider_1 = new org.androidtransfuse.example.simple.ValueProvider();
        InjectionUtil.setField(valueProvider_1, 0, "application", this.getApplication());
        AnotherValueImpl_VProxy anotherValueImpl_0;
        anotherValueImpl_0 = new AnotherValueImpl_VProxy();
        subComponent_0 = new SubComponent(anotherValueImpl_0);
        providerTest_0 = new ProviderTest();
        InjectionUtil.setField(providerTest_0, 0, "anotherValue", anotherValueImpl_0);
        gotoSecondActivity_0 = new GotoSecondActivity();
        InjectionUtil.setField(gotoSecondActivity_0, 0, "activity", this);
        InjectionUtil.setField(gotoSecondActivity_0, 0, "context", this);
        anotherValueImpl_1 = new AnotherValueImpl(subComponent_0);
        InjectionUtil.setField(anotherValueImpl_1, 0, "secondActivityButton", ((Button) this.findViewById(org.androidtransfuse.example.simple.R.id.secondActivity)));
        InjectionUtil.setField(anotherValueImpl_1, 0, "providerTestValue", providerTest_0 .get());
        InjectionUtil.setField(anotherValueImpl_1, 0, "gotoSecondActivity", gotoSecondActivity_0);
        anotherValueImpl_0 .load(anotherValueImpl_1);
        singletonProvider_0 = new org.androidtransfuse.example.simple.SingletonProvider();
        InjectionUtil.setField(singletonProvider_0, 0, "application", this.getApplication());
        loggingInterceptor_5 = new org.androidtransfuse.example.simple.LoggingInterceptor();
        vibrateOnClickListener_AOPProxy_0 = new VibrateOnClickListener_AOPProxy(loggingInterceptor_5);
        InjectionUtil.setField(vibrateOnClickListener_AOPProxy_0, 1, "vibrator", ((Vibrator) this.getSystemService("vibrator")));
        simpleController_0 = InjectionUtil.setConstructor(org.androidtransfuse.example.simple.SimpleController.class, new Class[] {org.androidtransfuse.example.simple.NotifyOnClickListener.class }, new Object[] {notifyOnClickListener_0 });
        InjectionUtil.setField(simpleController_0, 0, "providedValue", valueProvider_1 .get());
        InjectionUtil.setField(simpleController_0, 0, "activity", this);
        InjectionUtil.setField(simpleController_0, 0, "anotherValue", anotherValueImpl_1);
        InjectionUtil.setField(simpleController_0, 0, "singletonTarget", singletonProvider_0 .get());
        InjectionUtil.setField(simpleController_0, 0, "vibrateOnClickListener", vibrateOnClickListener_AOPProxy_0);
        simpleController_0 .setNotifyButton(((Button) this.findViewById(org.androidtransfuse.example.simple.R.id.notifyButton)));
        simpleController_0 .setVibrator(((Vibrator) this.getSystemService("vibrator")));
        simpleController_0 .setVibrateButton(((Button) this.findViewById(org.androidtransfuse.example.simple.R.id.vibrateButton)));
        handler_0 = new Handler();
        uIThreadMethodInterceptor_2 = new UIThreadMethodInterceptor();
        InjectionUtil.setField(uIThreadMethodInterceptor_2, 0, "handler", handler_0);
        asynchronousMethodInterceptor_2 = new AsynchronousMethodInterceptor();
        singletonProvider_1 = new org.androidtransfuse.example.simple.SingletonProvider();
        InjectionUtil.setField(singletonProvider_1, 0, "application", this.getApplication());
        lateReturnListener_AOPProxy_0 = new LateReturnListener_AOPProxy(uIThreadMethodInterceptor_2, asynchronousMethodInterceptor_2);
        InjectionUtil.setField(lateReturnListener_AOPProxy_0, 1, "context", this.getApplication());
        InjectionUtil.setField(lateReturnListener_AOPProxy_0, 1, "singletonTarget", singletonProvider_1 .get());
        simpleActivityDelegate_AOPProxy_0 = new SimpleActivityDelegate_AOPProxy(lifecycleLogger_1, loggingInterceptor_2);
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 1, "testHello", this.getApplication().getResources().getString(string.hello));
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 1, "providedValue", valueProvider_0 .get());
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 1, "button", ((Button) this.findViewById(org.androidtransfuse.example.simple.R.id.asyncActivity)));
        simpleActivityDelegate_AOPProxy_0 .testInt = this.getApplication().getResources().getInteger(integer.testInt);
        simpleActivityDelegate_AOPProxy_0 .controller = simpleController_0;
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 1, "resources", this.getApplication().getResources());
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 1, "systemService", ((Object) this.getSystemService("audio")));
        InjectionUtil.setField(simpleActivityDelegate_AOPProxy_0, 2, "vibrateButton", ((Button) this.findViewById(org.androidtransfuse.example.simple.R.id.vibrateButton)));
        InjectionUtil.setMethod(simpleActivityDelegate_AOPProxy_0, 2, "setContext", new Class[] {Context.class }, new Object[] {this });
        InjectionUtil.setMethod(simpleActivityDelegate_AOPProxy_0, 1, "setLateReturnListener", new Class[] {LateReturnListener.class }, new Object[] {lateReturnListener_AOPProxy_0 });
        anotherValueImpl_1 .registerSecondActivity();
        simpleController_0 .setupButton();
        simpleActivityDelegate_AOPProxy_0 .callMe();
        simpleActivityDelegate_AOPProxy_0 .registerLateReturnListener();
        simpleActivityDelegate_AOPProxy_0 .anotherCall();
        lifecycleLogger_1 .onCreate();
        Log.i("timer", Long.toString((System.currentTimeMillis()-time)));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        simpleController_0 .vibrate();
        simpleActivityDelegate_AOPProxy_0 .touch();
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        lifecycleLogger_1 .onDestroy();
    }

    public void onPause() {
        super.onPause();
        lifecycleLogger_1 .onPause();
    }

    public void onRestart() {
        super.onRestart();
        lifecycleLogger_1 .onRestart();
    }

    public void onResume() {
        super.onResume();
        lifecycleLogger_1 .onResume();
    }

    public void onStart() {
        super.onStart();
        lifecycleLogger_1 .onStart();
    }

    public void onStop() {
        super.onStop();
        lifecycleLogger_1 .onStop();
    }

}

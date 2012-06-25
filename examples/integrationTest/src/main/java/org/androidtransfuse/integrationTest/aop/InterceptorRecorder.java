package org.androidtransfuse.integrationTest.aop;

import android.content.Context;
import android.widget.Toast;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InterceptorRecorder implements MethodInterceptor {

    private static final int ONE_SECOND = 1000;

    private static Object retValue = null;
    private static boolean called;
    @Inject
    private Context context;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retValue = update(invocation.proceed());
        Toast.makeText(context, "Method returned: " + retValue, ONE_SECOND).show();
        return retValue;
    }

    private static Object update(Object value) {
        retValue = value;
        called = true;
        return retValue;
    }

    public static boolean isCalled() {
        return called;
    }

    public static Object getRetValue() {
        return retValue;
    }

    public static void reset() {
        retValue = null;
        called = false;
    }
}

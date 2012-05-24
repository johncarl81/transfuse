package org.androidtransfuse.example.simple;

import android.util.Log;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author John Ericksen
 */
public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Log.i("intercept", "start");

        Object output = invocation.proceed();

        Log.i("intercept", "stop duration: " + (System.currentTimeMillis() - start));

        return output;
    }
}

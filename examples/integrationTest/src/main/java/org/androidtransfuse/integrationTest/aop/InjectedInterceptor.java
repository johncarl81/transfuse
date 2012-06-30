package org.androidtransfuse.integrationTest.aop;

import android.content.Context;
import android.widget.Toast;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
public class InjectedInterceptor implements MethodInterceptor {

    @Inject
    private Stopwatch stopwatch;
    @Inject
    private Context context;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        stopwatch.start();
        Object retValue = invocation.proceed();
        Toast.makeText(context, "Call took " + stopwatch.stop() + "ms and returned " + retValue, ONE_SECOND).show();

        return retValue;
    }

    public Stopwatch getStopwatch() {
        return stopwatch;
    }
}

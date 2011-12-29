package org.androidrobotics.aop;

import android.os.AsyncTask;

/**
 * @author John Ericksen
 */
public class AsynchronousMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        AsyncTask task = new AsyncTask() {

            @Override
            protected Object doInBackground(Object... objects) {
                return invocation.proceed();
            }
        };

        task.execute();

        //asynchronous, so cannot return
        return null;
    }
}

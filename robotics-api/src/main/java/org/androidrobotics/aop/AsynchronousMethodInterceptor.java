package org.androidrobotics.aop;

import android.os.AsyncTask;

/**
 * @author John Ericksen
 */
public class AsynchronousMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        AsyncTask task = new MethodInterceptorAsyncTask(invocation);

        task.execute();

        //asynchronous, so cannot return
        return null;
    }

    private static final class MethodInterceptorAsyncTask extends AsyncTask {

        private MethodInvocation invocation;

        private MethodInterceptorAsyncTask(MethodInvocation invocation) {
            this.invocation = invocation;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            return invocation.proceed();
        }
    }
}

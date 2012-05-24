package org.androidtransfuse.aop;

import android.os.AsyncTask;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
            try {
                return invocation.proceed();
            } catch (Throwable e) {
                throw new TransfuseInjectionException("Exception while invoking background method", e);
            }
        }
    }
}

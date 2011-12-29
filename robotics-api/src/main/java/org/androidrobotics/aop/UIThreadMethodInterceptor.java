package org.androidrobotics.aop;

import android.os.Handler;

/**
 * @author John Ericksen
 */
public class UIThreadMethodInterceptor implements MethodInterceptor {

    /*todo: Provider injection
    @Inject
    protected Provider<Handler> handlerProvider;
    */

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Handler handler = new Handler();

        handler.post(new MethodInvocationRunnable(invocation));

        //asynchronous, so cannot return
        return null;
    }

    private class MethodInvocationRunnable implements Runnable {

        private MethodInvocation methodInvocation;

        private MethodInvocationRunnable(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public void run() {
            methodInvocation.proceed();
        }
    }
}

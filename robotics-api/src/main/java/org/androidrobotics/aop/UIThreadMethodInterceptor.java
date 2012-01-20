package org.androidrobotics.aop;

import android.os.Handler;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class UIThreadMethodInterceptor implements MethodInterceptor {

    @Inject
    private Handler handler;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        handler.post(new MethodInvocationRunnable(invocation));

        //asynchronous, so cannot return
        return null;
    }

    private final class MethodInvocationRunnable implements Runnable {

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

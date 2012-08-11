package org.androidtransfuse.aop;

import android.os.Handler;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class UIThreadMethodInterceptor implements MethodInterceptor {

    private final Handler handler;

    @Inject
    public UIThreadMethodInterceptor(Handler handler){
        this.handler = handler;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {

        handler.post(new MethodInvocationRunnable(invocation));

        //asynchronous, so cannot return
        return null;
    }

    private static final class MethodInvocationRunnable implements Runnable {

        private MethodInvocation methodInvocation;

        private MethodInvocationRunnable(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public void run() {
            try {
                methodInvocation.proceed();
            } catch (Throwable e) {
                throw new TransfuseInjectionException("Exception while invoking method on UI thread", e);
            }
        }
    }
}

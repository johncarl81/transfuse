package org.androidtransfuse.gen;


import org.androidtransfuse.aop.MethodInterceptor;
import org.androidtransfuse.aop.MethodInvocation;

/**
 * @author John Ericksen
 */
public class MockMethodInterceptor implements MethodInterceptor {

    private boolean triggered = false;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        triggered = true;

        return invocation.proceed();
    }

    public boolean isTriggered() {
        return triggered;
    }
}

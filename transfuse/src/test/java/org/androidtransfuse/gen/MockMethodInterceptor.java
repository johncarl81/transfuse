package org.androidtransfuse.gen;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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

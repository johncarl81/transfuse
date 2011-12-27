package org.androidrobotics.gen;


import org.androidrobotics.aop.MethodInterceptor;
import org.androidrobotics.aop.MethodInvocation;

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

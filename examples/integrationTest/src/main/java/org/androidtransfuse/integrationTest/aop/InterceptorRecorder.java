package org.androidtransfuse.integrationTest.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author John Ericksen
 */
public class InterceptorRecorder implements MethodInterceptor {

    private static Object retValue = null;
    private static boolean called;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return update(invocation.proceed());
    }

    private static Object update(Object value) {
        retValue = value;
        called = true;
        return retValue;
    }

    public static boolean isCalled() {
        return called;
    }

    public static Object getRetValue() {
        return retValue;
    }

    public static void reset() {
        retValue = null;
        called = false;
    }
}

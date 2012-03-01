package org.androidtransfuse.integrationTest.aop;

import org.androidtransfuse.aop.MethodInterceptor;
import org.androidtransfuse.aop.MethodInvocation;

/**
 * @author John Ericksen
 */
public class InterceptorRecorder implements MethodInterceptor {

    private static Object retValue = null;
    private static boolean called;

    public static boolean isCalled() {
        return called;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        retValue = invocation.proceed();
        called = true;
        return retValue;
    }

    public static Object getRetValue() {
        return retValue;
    }

    public static void reset() {
        retValue = null;
        called = false;
    }
}

package org.androidtransfuse.aop;

/**
 * @author John Ericksen
 */
public interface MethodInterceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}

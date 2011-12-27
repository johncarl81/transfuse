package org.androidrobotics.aop;

/**
 * @author John Ericksen
 */
public interface MethodInterceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;
}

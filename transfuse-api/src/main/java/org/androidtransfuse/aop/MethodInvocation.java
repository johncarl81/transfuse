package org.androidtransfuse.aop;

/**
 * todo; reimplement using org.aopalliance.intercept.MethodInterceptor
 *
 * @author John Ericksen
 */
public interface MethodInvocation {
    Object proceed();
}

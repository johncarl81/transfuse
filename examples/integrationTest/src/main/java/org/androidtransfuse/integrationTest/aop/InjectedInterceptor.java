package org.androidtransfuse.integrationTest.aop;

import org.androidtransfuse.aop.MethodInterceptor;
import org.androidtransfuse.aop.MethodInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectedInterceptor implements MethodInterceptor {
    @Inject
    private InterceptorDependency dependency;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }

    public InterceptorDependency getDependency() {
        return dependency;
    }
}

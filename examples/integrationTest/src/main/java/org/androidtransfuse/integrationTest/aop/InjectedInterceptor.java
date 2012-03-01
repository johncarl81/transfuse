package org.androidtransfuse.integrationTest.aop;

import org.androidtransfuse.aop.MethodInterceptor;
import org.androidtransfuse.aop.MethodInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class InjectedInterceptor implements MethodInterceptor {
    @Inject
    private IntercetorDepenency dependency;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return invocation.proceed();
    }

    public IntercetorDepenency getDependency() {
        return dependency;
    }
}

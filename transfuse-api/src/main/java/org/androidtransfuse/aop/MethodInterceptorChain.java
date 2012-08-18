package org.androidtransfuse.aop;


import org.androidtransfuse.util.TransfuseInjectionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class MethodInterceptorChain {

    private final MethodInterceptor[] methodInterceptors;
    private final MethodExecution methodExecution;
    private final Object proxy;

    public MethodInterceptorChain(MethodExecution methodExecution, Object proxy, MethodInterceptor... methodInterceptorChains) {
        this.methodExecution = methodExecution;
        this.methodInterceptors = methodInterceptorChains;
        this.proxy = proxy;
    }

    public Object invoke(Object[] arguments) {
        try {
            return new MethodInterceptorIterator(arguments).proceed();
        } catch (Throwable e) {
            throw new TransfuseInjectionException("Error while invoking Method Interceptor", e);
        }
    }

    private final class MethodInterceptorIterator implements MethodInvocation {

        private int i = -1;
        private final Object[] arguments;

        private MethodInterceptorIterator(Object[] arguments) {
            this.arguments = arguments;
        }

        @Override
        public Method getMethod() {
            try {
                return methodExecution.getMethod();
            } catch (Exception e) {
                throw new TransfuseInjectionException("Error while calling getMethod", e);
            }
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Object proceed() throws Throwable {
            i++;
            if (i == methodInterceptors.length) {
                return methodExecution.invoke();
            } else {
                return methodInterceptors[i].invoke(this);
            }
        }

        @Override
        public Object getThis() {
            return proxy;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return getMethod();
        }
    }

    public interface MethodExecution {

        String GET_METHOD = "getMethod";

        String INVOKE = "invoke";

        Method getMethod() throws Exception;

        Object invoke() throws Throwable;
    }
} 
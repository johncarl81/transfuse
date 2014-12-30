/**
 * Copyright 2011-2015 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidtransfuse.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author John Ericksen
 */
public class MethodInterceptorChainTest {

    private static final int INTERCEPTOR_SIZE = 5;
    private static final Object[] ARGUMENTS = {1, "test", 0.2d};

    private Object proxy;

    @Before
    public void setup() {
        proxy = new Object();
    }

    @Test
    public void testMethodChain() throws Throwable {

        MethodInterceptor[] interceptors = new MethodInterceptor[INTERCEPTOR_SIZE];

        for (int i = 0; i < INTERCEPTOR_SIZE; i++) {
            interceptors[i] = mock(MethodInterceptor.class);

            //mock chain of invocations
            //this statement allows the mocked MethodInterceptor to call the passed in MethodInvocation instances
            //proceed() method.
            final ArgumentCaptor<MethodInvocation> invocationCapture = ArgumentCaptor.forClass(MethodInvocation.class);
            when(interceptors[i].invoke(invocationCapture.capture())).thenAnswer(new Answer<Object>() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return invocationCapture.getValue().proceed();
                }
            });
        }

        MethodInterceptorChain.MethodExecution execution = mock(MethodInterceptorChain.MethodExecution.class);

        MethodInterceptorChain interceptorChain = new MethodInterceptorChain(execution, proxy, interceptors);

        interceptorChain.invoke(ARGUMENTS);

        for (MethodInterceptor interceptor : interceptors) {
            verify(interceptor).invoke(any(MethodInvocation.class));
        }

        verify(execution).invoke();
    }

    @Test
    public void testGetters() throws Exception {
        final Method mockMethod = MethodInterceptorChainTest.class.getMethod("targetMethod");

        MethodInterceptorChain.MethodExecution methodExecution = mock(MethodInterceptorChain.MethodExecution.class);

        when(methodExecution.getMethod()).thenReturn(mockMethod);

        MethodInterceptorChain interceptorChain = new MethodInterceptorChain(methodExecution, proxy, new MethodInterceptor() {

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                assertEquals(mockMethod, invocation.getMethod());
                assertArrayEquals(ARGUMENTS, invocation.getArguments());
                assertEquals(mockMethod, invocation.getStaticPart());

                return invocation.proceed();
            }
        });

        interceptorChain.invoke(ARGUMENTS);

        verify(methodExecution, times(2)).getMethod();
    }

    public void targetMethod() {
        //used to avoid mocking issues
    }

    @Test
    public void verifyMethodNames() throws NoSuchMethodException {
        Method getMethod = MethodInterceptorChain.MethodExecution.class.getMethod(MethodInterceptorChain.MethodExecution.GET_METHOD);
        assertNotNull(getMethod);
        Method invokeMethod = MethodInterceptorChain.MethodExecution.class.getMethod(MethodInterceptorChain.MethodExecution.INVOKE);
        assertNotNull(invokeMethod);
    }
}

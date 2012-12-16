/**
 * Copyright 2012 John Ericksen
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

import android.os.Handler;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
public class UIThreadMethodInterceptor implements MethodInterceptor {

    private final Handler handler;

    @Inject
    public UIThreadMethodInterceptor(Handler handler){
        this.handler = handler;
    }

    @Override
    public Object invoke(MethodInvocation invocation) {

        handler.post(new MethodInvocationRunnable(invocation));

        //asynchronous, so cannot return
        return null;
    }

    private static final class MethodInvocationRunnable implements Runnable {

        private final MethodInvocation methodInvocation;

        private MethodInvocationRunnable(MethodInvocation methodInvocation) {
            this.methodInvocation = methodInvocation;
        }

        @Override
        public void run() {
            try {
                methodInvocation.proceed();
            } catch (Throwable e) {
                throw new TransfuseInjectionException("Exception while invoking method on UI thread", e);
            }
        }
    }
}

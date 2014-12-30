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
package org.androidtransfuse.integrationTest.aop;

import android.content.Context;
import android.widget.Toast;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.inject.Inject;

import static org.androidtransfuse.integrationTest.SharedVariables.ONE_SECOND;

/**
 * @author John Ericksen
 */
public class InterceptorRecorder implements MethodInterceptor {

    private static Object storedValue = null;
    private static boolean called;
    @Inject
    private Context context;


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object retValue = update(invocation.proceed());
        Toast.makeText(context, "Method returned: " + retValue, ONE_SECOND).show();
        return retValue;
    }

    private static Object update(Object value) {
        storedValue = value;
        called = true;
        return storedValue;
    }

    public static boolean isCalled() {
        return called;
    }

    public static Object getValue() {
        return storedValue;
    }

    public static void reset() {
        storedValue = null;
        called = false;
    }
}

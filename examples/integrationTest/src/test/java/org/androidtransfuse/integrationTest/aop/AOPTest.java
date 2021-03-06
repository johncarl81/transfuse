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

import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class AOPTest {

    private AOP aop;
    private InjectedInterceptor injectedInterceptor;

    @Before
    public void setup() {
        InterceptorRecorder.reset();
        AOPActivity aopActivity = Robolectric.buildActivity(AOPActivity.class).create().get();

        aop = DelegateUtil.getDelegate(aopActivity, AOP.class);
        injectedInterceptor = DelegateUtil.getDelegate(aop, InjectedInterceptor.class);
    }

    @Test
    public void testNoReturnInterception() {
        assertFalse(InterceptorRecorder.isCalled());
        assertNull(InterceptorRecorder.getValue());
        aop.interceptMe();
        assertTrue(InterceptorRecorder.isCalled());
        assertNull(InterceptorRecorder.getValue());
    }

    @Test
    public void testReturnInterception() {
        assertFalse(InterceptorRecorder.isCalled());
        assertNull(InterceptorRecorder.getValue());
        aop.interceptMeWithReturn();
        assertTrue(InterceptorRecorder.isCalled());
        assertEquals(AOP.INTERCEPT_VALUE, InterceptorRecorder.getValue());
    }

    @Test
    public void testInterceptionDependency() {
        assertNotNull(injectedInterceptor.getStopwatch());
    }
}

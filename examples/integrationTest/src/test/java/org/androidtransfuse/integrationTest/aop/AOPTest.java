package org.androidtransfuse.integrationTest.aop;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.*;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class AOPTest {

    private AOP aop;
    private InjectedInterceptor injectedInterceptor;

    @Before
    public void setup() {
        InterceptorRecorder.reset();
        AOPActivity aopActivity = new AOPActivity();
        aopActivity.onCreate(null);

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

package org.androidtransfuse.integrationTest.externalGenerator;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class TestTest {

    private Test test;

    @Before
    public void setUp() throws Exception {
        TestActivity testActivity = new TestActivity();
        testActivity.onCreate(new Bundle());

        test = DelegateUtil.getDelegate(testActivity, Test.class);
    }

    @org.junit.Test
    public void testGeneratedProxy() throws Exception {
        assertNotNull(test.getTarget().getProxy());

    }

    @org.junit.Test
    public void testInjectedProxied() throws Exception {
        assertNotNull(test.getProxied());
        assertEquals(ProxiedProxy.class, test.getProxied().getClass());
    }
}

package org.androidtransfuse.integrationTest.scope;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ScopeTest {

    private ScopeOne one;
    private ScopeTwo two;

    @Before
    public void setup() {
        ScopeOneActivity scopeOneActivity = new ScopeOneActivity();
        scopeOneActivity.onCreate(null);
        ScopeTwoActivity scopeTwoActivity = new ScopeTwoActivity();
        scopeTwoActivity.onCreate(null);

        one = DelegateUtil.getDelegate(scopeOneActivity, ScopeOne.class);
        two = DelegateUtil.getDelegate(scopeTwoActivity, ScopeTwo.class);
    }

    @Test
    public void testSingleton() {
        assertEquals(one.getSingleton(), two.getSingleton());
        assertNotNull(one.getSingleton().getSingletonDependency());
        assertNotNull(one.getSingleton().getSingletonDependency());
    }
}

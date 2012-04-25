package org.androidtransfuse.integrationTest.layout;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class VariableLayoutTest {

    private VariableLayoutHandler variableLayoutHandler;
    private VariableLayoutActivity variableLayoutActivity;

    @Before
    public void setup() {
        variableLayoutActivity = new VariableLayoutActivity();
        variableLayoutActivity.onCreate(new Bundle());

        variableLayoutHandler = DelegateUtil.getDelegate(variableLayoutActivity, VariableLayoutHandler.class);
    }

    @Test
    public void testHandlerDependencyInjection() {
        assertNotNull(variableLayoutHandler.getDependency());
    }

    @Test
    public void testActivityInjection() {
        assertSame(variableLayoutActivity, variableLayoutHandler.getActivity());
    }

    @Test
    public void testGetLayoutCall() {
        assertTrue(variableLayoutHandler.isGetLayoutCalled());
    }
}

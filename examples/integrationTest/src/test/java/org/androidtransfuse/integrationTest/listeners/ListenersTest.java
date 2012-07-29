package org.androidtransfuse.integrationTest.listeners;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ListenersTest {

    private Listeners listeners;
    private ListenersActivity listenersActivity;

    @Before
    public void setup(){
        listenersActivity = new ListenersActivity();
        listenersActivity.onCreate(null);

        listeners = DelegateUtil.getDelegate(listenersActivity, Listeners.class);
    }

    @Test
    public void testOnTouchEventCall(){
        listenersActivity.onTouchEvent(null);
        assertTrue(listeners.isOnTouchEventOccurred());
    }
}

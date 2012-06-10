package org.androidtransfuse.integrationTest.broadcastReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ToasterTest {


    private ToasterBroadcastReceiver startupReceiver;

    @Before
    public void setup(){
        startupReceiver = new ToasterBroadcastReceiver();
    }

    @Test
    public void testOnReceive(){
        assertFalse(Toaster.isOnReceive());
        startupReceiver.onReceive(null, null);
        assertTrue(Toaster.isOnReceive());

    }
}

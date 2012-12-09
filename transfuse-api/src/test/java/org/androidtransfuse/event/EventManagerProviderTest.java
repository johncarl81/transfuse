package org.androidtransfuse.event;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class EventManagerProviderTest {

    private EventManagerProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new EventManagerProvider();
    }

    @Test
    public void testProviderGet() {
        assertNotNull(provider.get());
    }
}

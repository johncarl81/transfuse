package org.androidtransfuse.util;

import android.os.Bundle;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
public class ExtraUtilTest {

    private ExtraUtil extraUtil;
    private static final String TEST_NAME = "test extra";
    private static final String TEST_EXTRA_TARGET = "target";

    @Before
    public void setup() {
        extraUtil = ExtraUtil.getInstance();
    }

    @Test
    public void testNullExtras() {
        extraUtil.getExtra(null, TEST_NAME, true);
    }

    @Test(expected = TransfuseInjectionException.class)
    public void testUnexpectedNullExtras() {
        extraUtil.getExtra(null, TEST_NAME, false);
    }

    @Test
    public void getExtra() {

        Bundle mockBundle = PowerMock.createMock(Bundle.class);

        EasyMock.expect(mockBundle.containsKey(TEST_NAME)).andReturn(true);
        EasyMock.expect(mockBundle.get(TEST_NAME)).andReturn(TEST_EXTRA_TARGET);

        PowerMock.replay(mockBundle);

        Object extraOutput = extraUtil.getExtra(mockBundle, TEST_NAME, false);

        assertEquals(TEST_EXTRA_TARGET, extraOutput);

        PowerMock.verify(mockBundle);
    }

    @Test(expected = TransfuseInjectionException.class)
    public void getExtraThatIsExpectedToExist() {

        Bundle mockBundle = PowerMock.createMock(Bundle.class);

        EasyMock.expect(mockBundle.containsKey(TEST_NAME)).andReturn(false);

        PowerMock.replay(mockBundle);

        assertNull(extraUtil.getExtra(mockBundle, TEST_NAME, false));

        PowerMock.verify(mockBundle);
    }

    @Test
    public void getExtraThatIsOptional() {

        Bundle mockBundle = PowerMock.createMock(Bundle.class);

        EasyMock.expect(mockBundle.containsKey(TEST_NAME)).andReturn(false);

        PowerMock.replay(mockBundle);

        extraUtil.getExtra(mockBundle, TEST_NAME, true);

        PowerMock.verify(mockBundle);
    }

    @Test
    public void verifyExtraMethodNames() throws NoSuchMethodException {
        Method getInstanceMethod = ExtraUtil.class.getMethod(ExtraUtil.GET_INSTANCE);
        assertNotNull(getInstanceMethod);
        Method getExtraMethod = ExtraUtil.class.getMethod(ExtraUtil.GET_EXTRA, Bundle.class, String.class, boolean.class);
        assertNotNull(getExtraMethod);
    }
}

/**
 * Copyright 2013 John Ericksen
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
package org.androidtransfuse.util;

import android.os.Bundle;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Method;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

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

        Bundle mockBundle = PowerMockito.mock(Bundle.class);

        when(mockBundle.containsKey(TEST_NAME)).thenReturn(true);
        when(mockBundle.get(TEST_NAME)).thenReturn(TEST_EXTRA_TARGET);

        Object extraOutput = extraUtil.getExtra(mockBundle, TEST_NAME, false);

        assertEquals(TEST_EXTRA_TARGET, extraOutput);
    }

    @Test(expected = TransfuseInjectionException.class)
    public void getExtraThatIsExpectedToExist() {

        Bundle mockBundle = PowerMockito.mock(Bundle.class);

        when(mockBundle.containsKey(TEST_NAME)).thenReturn(false);

        assertNull(extraUtil.getExtra(mockBundle, TEST_NAME, false));
    }

    @Test
    public void getExtraThatIsOptional() {

        Bundle mockBundle = PowerMockito.mock(Bundle.class);

        when(mockBundle.containsKey(TEST_NAME)).thenReturn(false);

        extraUtil.getExtra(mockBundle, TEST_NAME, true);
    }

    @Test
    public void verifyExtraMethodNames() throws NoSuchMethodException {
        Method getInstanceMethod = ExtraUtil.class.getMethod(ExtraUtil.GET_INSTANCE);
        assertNotNull(getInstanceMethod);
        Method getExtraMethod = ExtraUtil.class.getMethod(ExtraUtil.GET_EXTRA, Bundle.class, String.class, boolean.class);
        assertNotNull(getExtraMethod);
    }
}

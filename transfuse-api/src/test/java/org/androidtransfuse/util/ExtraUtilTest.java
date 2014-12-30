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
package org.androidtransfuse.util;

import android.os.Bundle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ExtraUtilTest {

    private static final String TEST_NAME = "test extra";
    private static final String TEST_EXTRA_TARGET = "target";

    @Test
    public void testNullExtras() {
        ExtraUtil.getExtra(null, TEST_NAME, true);
    }

    @Test(expected = TransfuseInjectionException.class)
    public void testUnexpectedNullExtras() {
        ExtraUtil.getExtra(null, TEST_NAME, false);
    }

    @Test
    public void getExtra() {

        Bundle mockBundle = new Bundle();

        mockBundle.putString(TEST_NAME, TEST_EXTRA_TARGET);

        Object extraOutput = ExtraUtil.getExtra(mockBundle, TEST_NAME, false);

        assertEquals(TEST_EXTRA_TARGET, extraOutput);
    }

    @Test(expected = TransfuseInjectionException.class)
    public void getExtraThatIsExpectedToExist() {

        Bundle mockBundle = new Bundle();

        ExtraUtil.getExtra(mockBundle, TEST_NAME, false);
    }

    @Test
    public void getExtraThatIsOptional() {

        Bundle mockBundle = new Bundle();

        assertNull(ExtraUtil.getExtra(mockBundle, TEST_NAME, true));
    }

    @Test
    public void verifyExtraMethodNames() throws NoSuchMethodException {
        Method getExtraMethod = ExtraUtil.class.getMethod(ExtraUtil.GET_EXTRA, Bundle.class, String.class, boolean.class);
        assertNotNull(getExtraMethod);
    }
}

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
package org.androidtransfuse.integrationTest.inject;

import android.content.Intent;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.integrationTest.SerializableValue;
import org.androidtransfuse.intentFactory.IntentFactory;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ExtraInjectionTest {

    private static final String EXTRA_ONE_VALUE = "hello extra";
    private static final long EXTRA_TWO_VALUE = 42L;
    private static final String EXTRA_THREE_VALUE = "hello optional";
    private static final String EXTRA_FOUR_VALUE = "hello serializable";
    private static final boolean[] BOOLEANS = {true, false, true};

    private ParcelExample parcelExample;

    @Before
    public void setup() {
        parcelExample = new ParcelExample();

        parcelExample.setName(EXTRA_ONE_VALUE);
        parcelExample.setValue(EXTRA_TWO_VALUE);
        parcelExample.setInnerParcel(new ParcelTwo(EXTRA_THREE_VALUE));
        parcelExample.setBooleans(BOOLEANS);
        parcelExample.setRealParcelable(new RealParcelable(EXTRA_ONE_VALUE));
    }

    @Test
    public void testExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertEquals(EXTRA_THREE_VALUE, extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
        assertEquals(parcelExample, extraInjection.getParcelExample());
    }

    @Test
    public void testOptionalExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, null, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertNull(extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
        assertEquals(parcelExample, extraInjection.getParcelExample());
    }

    @Test(expected = TransfuseInjectionException.class)
    public void testNonOptionalExtra() {
        setupExtraInjection(null, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);
    }

    private ExtraInjection setupExtraInjection(String extraOneValue, Long extraTwoValue, String extraThreeValue, SerializableValue serializableValue, ParcelExample inputParcelExample) {

        ExtraInjectionActivity extraInjectionActivity = new ExtraInjectionActivity();

        IntentFactory intentFactory = new IntentFactory(extraInjectionActivity);
        Intent callingIntent = intentFactory.buildIntent(new ExtraInjectionActivityStrategy(serializableValue, extraOneValue, inputParcelExample, extraTwoValue)
                .setExtraThree(extraThreeValue));

        extraInjectionActivity.setIntent(callingIntent);
        extraInjectionActivity.onCreate(null);

        return DelegateUtil.getDelegate(extraInjectionActivity, ExtraInjection.class);
    }
}
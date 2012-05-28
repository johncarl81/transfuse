package org.androidtransfuse.integrationTest.inject;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.integrationTest.SerializableValue;
import org.androidtransfuse.intentFactory.IntentFactory;
import org.androidtransfuse.util.TransfuseInjectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ExtraInjectionTest {

    private static final String EXTRA_ONE_VALUE = "hello extra";
    private static final long EXTRA_TWO_VALUE = 42L;
    private static final String EXTRA_THREE_VALUE = "hello optional";
    private static final String EXTRA_FOUR_VALUE = "hello serializable";

    private ParcelExample parcelExample;

    @Before
    public void setup() {
        parcelExample = new ParcelExample();

        ParcelTwo parcelTwo = new ParcelTwo();

        parcelTwo.setValue(EXTRA_THREE_VALUE);

        parcelExample.setName(EXTRA_ONE_VALUE);
        parcelExample.setValue(EXTRA_TWO_VALUE);
        //parcelExample.setInnerParcel(parcelTwo);
    }

    @Test
    public void testExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertEquals(EXTRA_THREE_VALUE, extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
        //assertEquals(parcelExample, extraInjection.getParcelExample());
    }

    @Test
    public void testOptionalExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, null, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertNull(extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
        //assertEquals(parcelExample, extraInjection.getParcelExample());
    }

    @Test(expected = TransfuseInjectionException.class)
    public void testNonOptionalExtra() {
        setupExtraInjection(null, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE), parcelExample);
    }

    private ExtraInjection setupExtraInjection(String extraOneValue, Long extraTwoValue, String extraThreeValue, SerializableValue serializableValue, ParcelExample inputParcelExample) {

        ExtraInjectionActivity extraInjectionActivity = new ExtraInjectionActivity();

        IntentFactory intentFactory = new IntentFactory(extraInjectionActivity);
        Intent callingIntent = intentFactory.buildIntent(new ExtraInjectionActivityStrategy(serializableValue, extraOneValue, extraTwoValue)
                .setExtraThree(extraThreeValue));

        extraInjectionActivity.setIntent(callingIntent);
        extraInjectionActivity.onCreate(null);

        return DelegateUtil.getDelegate(extraInjectionActivity, ExtraInjection.class);
    }
}
package org.androidtransfuse.integrationTest.inject;

import android.content.Intent;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.androidtransfuse.util.TransfuseInjectionException;
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
    private static final Long EXTRA_TWO_VALUE = 42L;
    private static final String EXTRA_THREE_VALUE = "hello optional";
    private static final String EXTRA_FOUR_VALUE = "hello serializable";

    @Test
    public void testExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE));

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertEquals(EXTRA_THREE_VALUE, extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
    }

    @Test
    public void testOptionalExtra() {
        ExtraInjection extraInjection = setupExtraInjection(EXTRA_ONE_VALUE, EXTRA_TWO_VALUE, null, new SerializableValue(EXTRA_FOUR_VALUE));

        assertEquals(EXTRA_ONE_VALUE, extraInjection.getExtraOne());
        assertEquals(EXTRA_TWO_VALUE, extraInjection.getExtraTwo());
        assertNull(extraInjection.getExtraThree());
        assertEquals(EXTRA_FOUR_VALUE, extraInjection.getExtraFour().getValue());
    }

    @Test(expected = TransfuseInjectionException.class)
    public void testNonOptionalExtra() {
        setupExtraInjection(null, EXTRA_TWO_VALUE, EXTRA_THREE_VALUE, new SerializableValue(EXTRA_FOUR_VALUE));
    }

    private ExtraInjection setupExtraInjection(String extraOneValue, Long extraTwoValue, String extraThreeValue, SerializableValue seriazableValue) {
        Intent callingIntent = new Intent("test");
        callingIntent.putExtra(ExtraInjection.EXTRA_ONE, extraOneValue);
        callingIntent.putExtra(ExtraInjection.EXTRA_TWO, extraTwoValue);
        callingIntent.putExtra(ExtraInjection.EXTRA_THREE, extraThreeValue);
        callingIntent.putExtra(ExtraInjection.EXTRA_FOUR, seriazableValue);

        ExtraInjectionActivity extraInjectionActivity = new ExtraInjectionActivity();
        extraInjectionActivity.setIntent(callingIntent);
        extraInjectionActivity.onCreate(null);

        return DelegateUtil.getDelegate(extraInjectionActivity, ExtraInjection.class);
    }
}

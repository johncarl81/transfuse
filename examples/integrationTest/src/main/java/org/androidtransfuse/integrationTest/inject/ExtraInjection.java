package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity
@Layout(R.layout.main)
public class ExtraInjection {

    public static final String EXTRA_ONE = "extraOne";
    public static final String EXTRA_TWO = "extraTwo";
    public static final String EXTRA_THREE = "extraThree";
    public static final String EXTRA_FOUR = "extraFour";

    @Inject
    @Extra(EXTRA_ONE)
    private String extraOne;

    @Inject
    @Extra(EXTRA_TWO)
    private Long extraTwo;

    @Inject
    @Extra(value = EXTRA_THREE, optional = true)
    private String extraThree;

    @Inject
    @Extra(value = EXTRA_FOUR)
    private SerializableValue extraFour;

    @OnPause
    public void keepInActivity() {
    }

    public String getExtraOne() {
        return extraOne;
    }

    public Long getExtraTwo() {
        return extraTwo;
    }

    public String getExtraThree() {
        return extraThree;
    }

    public SerializableValue getExtraFour() {
        return extraFour;
    }
}

package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.annotations.OnPause;
import org.androidtransfuse.integrationTest.R;
import org.androidtransfuse.integrationTest.SerializableValue;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(label = "Extras")
@Layout(R.layout.main)
public class ExtraInjection {

    public static final String EXTRA_ONE = "extraOne";
    public static final String EXTRA_TWO = "extraTwo";
    public static final String EXTRA_THREE = "extraThree";
    public static final String EXTRA_FOUR = "extraFour";
    public static final String EXTRA_PARCELABLE = "extraParcelable";

    @Inject
    @Extra(EXTRA_ONE)
    private String extraOne;

    private long extraTwo;

    @Inject
    @Extra(value = EXTRA_THREE, optional = true)
    private String extraThree;

    @Inject
    @Extra(value = EXTRA_FOUR)
    private SerializableValue extraFour;

    @Inject
    @Extra(value = EXTRA_PARCELABLE)
    private ParcelExample parcelExample;


    @OnPause
    public void keepInActivity() {
    }

    @Inject

    @Extra(EXTRA_TWO)
    public void setExtraTwo(long extraTwo) {
        this.extraTwo = extraTwo;
    }

    public String getExtraOne() {
        return extraOne;
    }

    public long getExtraTwo() {
        return extraTwo;
    }

    public String getExtraThree() {
        return extraThree;
    }

    public SerializableValue getExtraFour() {
        return extraFour;
    }

    public ParcelExample getParcelExample() {
        return parcelExample;
    }
}

package org.androidtransfuse.integrationTest.inject;

import org.androidtransfuse.annotations.Activity;
import org.androidtransfuse.annotations.Extra;
import org.androidtransfuse.annotations.Layout;
import org.androidtransfuse.integrationTest.R;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "ExtraInjectionActivity")
@Layout(R.layout.main)
public class ExtraInjection {

    public static final String EXTRA_ONE = "extraOne";
    public static final String EXTRA_TWO = "extraTwo";
    public static final String EXTRA_THREE = "extraThree";

    @Inject
    @Extra(EXTRA_ONE)
    private String extraOne;

    @Inject
    @Extra(EXTRA_TWO)
    private Long extraTwo;

    @Inject
    @Extra(value = EXTRA_THREE, optional = true)
    private String extraThree;

    public String getExtraOne() {
        return extraOne;
    }

    public Long getExtraTwo() {
        return extraTwo;
    }

    public String getExtraThree() {
        return extraThree;
    }
}

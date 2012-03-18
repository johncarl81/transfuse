package org.androidtransfuse.integrationTest.inject;

import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class CustomViewTest {

    private static final String RESOURCE_VALUE = "Custom View String";

    private CustomView customView;

    @Before
    public void setup() {
        CustomViewActivity customViewActivity = new CustomViewActivity();
        customViewActivity.onCreate(null);

        customView = DelegateUtil.getDelegate(customViewActivity, CustomView.class);
    }

    @Test
    public void testCustomViewInjection() {
        assertEquals(RESOURCE_VALUE, customView.getLabelView().getText());
    }
}

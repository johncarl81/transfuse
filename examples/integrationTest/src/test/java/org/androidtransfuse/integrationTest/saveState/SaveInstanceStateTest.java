package org.androidtransfuse.integrationTest.saveState;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class SaveInstanceStateTest {

    private static final CharSequence TEST_TEXT = "tester";

    private SaveInstanceStateActivity saveInstanceStateActivity;
    private SaveInstanceState saveInstanceState;
    private Bundle bundle;

    @Before
    public void setup(){
        saveInstanceStateActivity = new SaveInstanceStateActivity();
        saveInstanceStateActivity.onCreate(null);

        bundle = new Bundle();

        saveInstanceState = DelegateUtil.getDelegate(saveInstanceStateActivity, SaveInstanceState.class);

    }

    @Test
    public void testOnSaveInstanceState(){
        saveInstanceState.getState1().setText(TEST_TEXT);
        saveInstanceStateActivity.onSaveInstanceState(bundle);
        //todo: figure out how to make robolectric work here:
        //assertEquals(TEST_TEXT, bundle.getCharSequence(SaveInstanceState.TEXT_KEY));
    }

    @Test
    public void testOnRestoreInstanceState(){
        bundle.putCharSequence(SaveInstanceState.TEXT_KEY, TEST_TEXT);
        saveInstanceStateActivity.onRestoreInstanceState(bundle);
        //assertEquals(TEST_TEXT, saveInstanceState.getState1().getText());
    }
}

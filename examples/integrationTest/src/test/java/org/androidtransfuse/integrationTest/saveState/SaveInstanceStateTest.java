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
        saveInstanceState.getStateInput().setText(TEST_TEXT);
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

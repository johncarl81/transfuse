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
package org.androidtransfuse.integrationTest.saveState;

import android.os.Bundle;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class SaveInstanceStateTest {

    private static final String TEST_TEXT = "tester";

    private SaveInstanceStateActivity saveInstanceStateActivity;
    private SaveInstanceState saveInstanceState;
    private Bundle bundle;

    @Before
    public void setup(){
        saveInstanceStateActivity = Robolectric.buildActivity(SaveInstanceStateActivity.class).create().get();

        bundle = new Bundle();

        saveInstanceState = DelegateUtil.getDelegate(saveInstanceStateActivity, SaveInstanceState.class);

    }

    @Test
    public void testOnSaveInstanceState(){
        saveInstanceState.getStateInput().setText(TEST_TEXT);
        // mimics button click
        saveInstanceState.stateSaveListener.onClick(null);
        saveInstanceStateActivity.onSaveInstanceState(bundle);
        assertEquals(TEST_TEXT, bundle.getString(SaveInstanceState.TEXT_KEY));
    }

    @Test
    public void testOnRestoreInstanceState(){
        bundle.putString(SaveInstanceState.TEXT_KEY, TEST_TEXT);
        saveInstanceStateActivity.onRestoreInstanceState(bundle);
        assertEquals(TEST_TEXT, saveInstanceState.getStateOutput().getText().toString());
    }
}

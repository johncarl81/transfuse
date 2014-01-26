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
package org.androidtransfuse.integrationTest.register;

import android.widget.Button;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class RegisterTest {

    private Register register;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;


    @Before
    public void setup() {
        RegisterActivity registerActivity = Robolectric.buildActivity(RegisterActivity.class).create().get();

        register = DelegateUtil.getDelegate(registerActivity, Register.class);

        button1 = (Button) registerActivity.findViewById(org.androidtransfuse.integrationTest.R.id.button1);
        button2 = (Button) registerActivity.findViewById(org.androidtransfuse.integrationTest.R.id.button2);
        button3 = (Button) registerActivity.findViewById(org.androidtransfuse.integrationTest.R.id.button3);
        button4 = (Button) registerActivity.findViewById(org.androidtransfuse.integrationTest.R.id.button4);
        button5 = (Button) registerActivity.findViewById(org.androidtransfuse.integrationTest.R.id.button5);
    }

    @Test
    public void testOnClickListener1() {
        assertFalse(register.getListener1().isClicked());
        assertFalse(register.getListener1().isLongClicked());

        button1.performClick();

        assertTrue(register.getListener1().isClicked());
        assertFalse(register.getListener1().isLongClicked());

        button1.performLongClick();

        assertTrue(register.getListener1().isClicked());
        assertFalse(register.getListener1().isLongClicked());
    }

    @Test
    public void testOnClickListener2() {
        assertFalse(register.getListener2().isClicked());
        assertFalse(register.getListener2().isLongClicked());

        button2.performClick();

        assertTrue(register.getListener2().isClicked());
        assertFalse(register.getListener2().isLongClicked());

        button2.performLongClick();

        assertTrue(register.getListener2().isClicked());
        assertTrue(register.getListener2().isLongClicked());
    }

    @Test
    public void testOnClickListener3() {
        assertFalse(register.getListener3().isClicked());

        button3.performClick();

        assertTrue(register.getListener3().isClicked());
    }

    @Test
    public void testOnClickListener4() {
        assertFalse(register.getListener4().isClicked());

        button4.performClick();

        assertTrue(register.getListener4().isClicked());
    }

    @Test
    public void testOnClickListener5() {
        assertFalse(register.isListener5Clicked());

        button5.performClick();

        assertTrue(register.isListener5Clicked());
    }


}

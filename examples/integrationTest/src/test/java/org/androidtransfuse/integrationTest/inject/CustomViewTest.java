/**
 * Copyright 2012 John Ericksen
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

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
package org.androidtransfuse.integrationTest.layout;

import android.os.Bundle;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class VariableLayoutTest {

    private VariableLayoutHandler variableLayoutHandler;
    private VariableLayoutActivity variableLayoutActivity;

    @Before
    public void setup() {
        variableLayoutActivity = new VariableLayoutActivity();
        variableLayoutActivity.onCreate(new Bundle());

        variableLayoutHandler = DelegateUtil.getDelegate(variableLayoutActivity, VariableLayoutHandler.class);
    }

    @Test
    public void testHandlerDependencyInjection() {
        assertNotNull(variableLayoutHandler.getDependency());
    }

    @Test
    public void testActivityInjection() {
        assertSame(variableLayoutActivity, variableLayoutHandler.getActivity());
    }

    @Test
    public void testGetLayoutCall() {
        assertTrue(variableLayoutHandler.isGetLayoutCalled());
    }
}

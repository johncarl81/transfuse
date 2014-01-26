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
package org.androidtransfuse.integrationTest.scope;

import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ScopeTest {

    private ScopeOne one;
    private ScopeTwo two;

    @Before
    public void setup() {
        ScopeOneActivity scopeOneActivity = Robolectric.buildActivity(ScopeOneActivity.class).create().get();
        ScopeTwoActivity scopeTwoActivity = Robolectric.buildActivity(ScopeTwoActivity.class).create().get();

        one = DelegateUtil.getDelegate(scopeOneActivity, ScopeOne.class);
        two = DelegateUtil.getDelegate(scopeTwoActivity, ScopeTwo.class);
    }

    @Test
    public void testSingleton() {
        assertEquals(one.getSingleton(), two.getSingleton());
        assertNotNull(one.getSingleton().getSingletonDependency());
        assertNotNull(one.getSingleton().getSingletonDependency());
    }
}

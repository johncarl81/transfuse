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
package org.androidtransfuse.integrationTest.listeners;

import org.androidtransfuse.integrationTest.DelegateUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertTrue;

/**
 * @author John Ericksen
 */
@RunWith(RobolectricTestRunner.class)
public class ListenersTest {

    private Listeners listeners;
    private ListenersActivity listenersActivity;

    @Before
    public void setup(){
        listenersActivity = new ListenersActivity();
        listenersActivity.onCreate(null);

        listeners = DelegateUtil.getDelegate(listenersActivity, Listeners.class);
    }

    @Test
    public void testOnTouchEventCall(){
        listenersActivity.onTouchEvent(null);
        assertTrue(listeners.isOnTouchEventOccurred());
    }
}
